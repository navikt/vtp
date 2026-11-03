package no.nav.foreldrepenger.sokos.simulering;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

final class SokosPeriodeGenerator {

    private SokosPeriodeGenerator() {
    }

    static List<Periode> genererPerioder(List<SokosSimuleringRequest.Oppdragslinje> linjer) {
        var perioder = linjer.stream().map(SokosPeriodeGenerator::fraLinje).sorted(Comparator.comparing(Periode::fom)).toList();
        var opphør = perioder.stream().filter(Periode::opphør).toList();
        var ytelse = perioder.stream().filter(periode -> !periode.opphør()).toList();
        return Stream.concat(
                ytelse.stream().flatMap(periode -> splittOverlapp(periode, opphør).stream()),
                opphør.stream().flatMap(periode -> splittOverlapp(periode, ytelse).stream()))
            .sorted(Comparator.comparing(Periode::fom))
            .toList();
    }

    private static Periode fraLinje(SokosSimuleringRequest.Oppdragslinje linje) {
        return switch (linje.kodeEndringLinje()) {
            case "NY" -> new Periode(linje.datoVedtakFom(), linje.datoVedtakTom(), linje.sats(), null, false);
            case "ENDR" -> new Periode(Optional.ofNullable(linje.datoStatusFom()).orElse(linje.datoVedtakFom()),
                linje.datoVedtakTom(), linje.sats(), null, true);
            default -> throw new IllegalArgumentException("Forventet kodeEndringLinje NY eller ENDR. Verdi var: " + linje.kodeEndringLinje());
        };
    }

    private static List<Periode> splittOverlapp(Periode periode, List<Periode> motperioder) {
        var overlapp = motperioder.stream()
            .filter(motperiode -> !motperiode.tom().isBefore(periode.fom()) && !motperiode.fom().isAfter(periode.tom()))
            .findFirst();
        if (overlapp.isEmpty()) {
            return List.of(periode);
        }

        var motperiode = overlapp.orElseThrow();
        var fom = periode.fom().isAfter(motperiode.fom()) ? periode.fom() : motperiode.fom();
        var tom = periode.tom().isBefore(motperiode.tom()) ? periode.tom() : motperiode.tom();
        var resultat = new ArrayList<Periode>();
        if (periode.fom().isBefore(fom)) {
            resultat.add(periode.medDatoer(periode.fom(), fom.minusDays(1)));
        }
        // Overlapp blir en satsendring for ytelsen og fjernes fra opphøret.
        if (!periode.opphør()) {
            resultat.add(new Periode(fom, tom, periode.sats(), motperiode.sats(), false));
        }
        if (tom.isBefore(periode.tom())) {
            resultat.addAll(splittOverlapp(periode.medDatoer(tom.plusDays(1), periode.tom()), motperioder));
        }
        return resultat;
    }

    record Periode(LocalDate fom, LocalDate tom, BigDecimal sats, BigDecimal gammelSats, boolean opphør) {

        Periode medDatoer(LocalDate nyFom, LocalDate nyTom) {
            return new Periode(nyFom, nyTom, sats, gammelSats, opphør);
        }

        long virkedager() {
            if (tom.isBefore(fom)) {
                return 0;
            }
            return fom.datesUntil(tom.plusDays(1))
                .map(LocalDate::getDayOfWeek)
                .filter(dag -> dag != DayOfWeek.SATURDAY && dag != DayOfWeek.SUNDAY)
                .count();
        }
    }
}
