package no.nav.foreldrepenger.sokos.simulering;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Stream;

import no.nav.foreldrepenger.sokos.simulering.SokosPeriodeGenerator.Periode;

final class SokosSimuleringMapper {

    private SokosSimuleringMapper() {
    }

    static SokosSimuleringRespons simuler(SokosSimuleringRequest request) {
        return simuler(request, LocalDate.now());
    }

    static SokosSimuleringRespons simuler(SokosSimuleringRequest request, LocalDate idag) {
        var simulering = request.operation().container().requestSimulering();
        var oppdrag = simulering.oppdrag();
        var mottaker = simulering.oppdragslinje().stream()
            .flatMap(linje -> linje.refusjonsInfo().stream().findFirst().stream())
            .map(SokosSimuleringRequest.RefusjonsInfo::refunderesId)
            .findFirst()
            .orElse(oppdrag.oppdragGjelderId());
        var sisteMåned = YearMonth.from(idag.getDayOfMonth() <= 19 ? idag : idag.plusMonths(1));
        var perioder = SokosPeriodeGenerator.genererPerioder(simulering.oppdragslinje()).stream()
            .filter(periode -> !YearMonth.from(periode.fom()).isAfter(sisteMånedFor(periode, sisteMåned)) && periode.virkedager() != 0)
            .flatMap(periode -> mapPeriode(periode, oppdrag.kodeFagomraade(), mottaker, sisteMåned, idag))
            .toList();
        var beregning = perioder.isEmpty() ? null : new SokosSimuleringRespons.Beregning(
            oppdrag.oppdragGjelderId(), "DUMMY", idag.toString(), "KORTTID", BigDecimal.valueOf(1234), perioder);
        return new SokosSimuleringRespons(new SokosSimuleringRespons.Operation(new SokosSimuleringRespons.Container(
            new SokosSimuleringRespons.Respons(0, "", beregning))));
    }

    private static YearMonth sisteMånedFor(Periode periode, YearMonth sisteMåned) {
        return periode.opphør() ? sisteMåned.minusMonths(1) : sisteMåned;
    }

    private static Stream<SokosSimuleringRespons.PeriodeStoppnivaa> mapPeriode(Periode periode, String fagområde,
                                                                           String mottaker, YearMonth sisteMåned, LocalDate idag) {
        var tom = tidligste(periode.tom(), sisteMånedFor(periode, sisteMåned).atEndOfMonth());
        var beregningsPeriode = new SokosSimuleringRespons.BeregningsPeriode(periode.fom().toString(), tom.toString());
        return Stream.iterate(periode.fom(), fom -> !fom.isAfter(tom), fom -> YearMonth.from(fom).plusMonths(1).atDay(1))
            .map(fom -> periode.medDatoer(fom, tidligste(YearMonth.from(fom).atEndOfMonth(), tom)))
            .map(måned -> new SokosSimuleringRespons.PeriodeStoppnivaa(beregningsPeriode,
                new SokosSimuleringRespons.Stoppnivaa(fagområde, mottaker,
                    (YearMonth.from(måned.fom()).equals(sisteMåned) ? idag.withDayOfMonth(20) : idag).toString(),
                    posteringer(måned, sisteMåned))));
    }

    private static List<SokosSimuleringRespons.Detaljer> posteringer(Periode periode, YearMonth sisteMåned) {
        // Som den eksisterende mocken beregnes alle satstyper per virkedag, også ENG.
        var antall = BigDecimal.valueOf(periode.virkedager());
        var beløp = periode.sats().multiply(antall);
        if (periode.opphør()) {
            return List.of(detaljer(periode, beløp, "YTEL"), detaljer(periode, beløp, "FEIL"), detaljer(periode, beløp.negate(), "YTEL"));
        }
        if (periode.gammelSats() != null && YearMonth.from(periode.fom()).isBefore(sisteMåned)) {
            var gammeltBeløp = periode.gammelSats().multiply(antall);
            if (periode.sats().compareTo(periode.gammelSats()) < 0) {
                return List.of(detaljer(periode, gammeltBeløp.subtract(beløp), "FEIL"), detaljer(periode, gammeltBeløp.negate(), "YTEL"));
            }
            return List.of(detaljer(periode, gammeltBeløp.negate(), "YTEL"), detaljer(periode, beløp, "YTEL"));
        }
        return List.of(detaljer(periode, beløp, "YTEL"));
    }

    private static SokosSimuleringRespons.Detaljer detaljer(Periode periode, BigDecimal beløp, String typeKlasse) {
        return new SokosSimuleringRespons.Detaljer(periode.fom().toString(), periode.tom().toString(), beløp, typeKlasse);
    }

    private static LocalDate tidligste(LocalDate første, LocalDate andre) {
        return første.isBefore(andre) ? første : andre;
    }
}
