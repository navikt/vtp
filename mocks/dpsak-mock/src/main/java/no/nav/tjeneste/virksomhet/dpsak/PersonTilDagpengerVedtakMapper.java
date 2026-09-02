package no.nav.tjeneste.virksomhet.dpsak;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import no.nav.tjeneste.virksomhet.dpsak.DagpengerRettighetsperioderDto.Rettighetsperiode;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

public class PersonTilDagpengerVedtakMapper {

    private PersonTilDagpengerVedtakMapper() {
    }

    public static List<Rettighetsperiode> tilPerioder(Person person, LocalDate fraOgMedDato, LocalDate tilOgMedDato) {
        return tilDagpengerYtelser(person, fraOgMedDato, tilOgMedDato).stream()
                .map(ytelse -> new Rettighetsperiode(fom(ytelse, fraOgMedDato), tom(ytelse, tilOgMedDato),
                        DagpengerRettighetsperioderDto.DagpengerKilde.DP_SAK))
                .toList();
    }

    public static List<DagpengerUtbetalingDto> tilUtbetalinger(Person person, LocalDate fraOgMedDato, LocalDate tilOgMedDato) {
        return tilDagpengerYtelser(person, fraOgMedDato, tilOgMedDato).stream()
                .flatMap(ytelse -> tilUtbetalinger(ytelse, fraOgMedDato, tilOgMedDato))
                .toList();
    }

    private static List<Ytelse> tilDagpengerYtelser(Person person, LocalDate fraOgMedDato, LocalDate tilOgMedDato) {
        if (person == null || person.ytelser() == null) {
            return List.of();
        }
        return person.ytelser().stream()
                .filter(ytelse -> ytelse.ytelse() == YtelseType.DAGPENGER)
                .filter(ytelse -> overlapper(ytelse, fraOgMedDato, tilOgMedDato))
                .toList();
    }

    private static Stream<DagpengerUtbetalingDto> tilUtbetalinger(Ytelse ytelse, LocalDate fraOgMedDato, LocalDate tilOgMedDato) {
        var periodeFom = fom(ytelse, fraOgMedDato);
        var periodeTom = tom(ytelse, tilOgMedDato);
        var dagsats = ytelse.dagsats() != null ? ytelse.dagsats() : 0;
        var utbetalingsgrad = ytelse.utbetalingsgrad() != null ? ytelse.utbetalingsgrad() : 100;
        var utbetaltPerVirkedag = (int) Math.round(dagsats * utbetalingsgrad / 100.0);
        return periodeFom.datesUntil(periodeTom.plusDays(1))
                .map(dato -> new DagpengerUtbetalingDto(
                        dato,
                        dato,
                        DagpengerUtbetalingDto.DagpengerKilde.DP_SAK,
                        dagsats,
                        erVirkedag(dato) ? utbetaltPerVirkedag : 0,
                        null // gjenståendeDager - ikke i bruk av DpsakMapper
                ));
    }

    private static boolean overlapper(Ytelse ytelse, LocalDate fraOgMedDato, LocalDate tilOgMedDato) {
        return (fraOgMedDato == null || !ytelse.tom().isBefore(fraOgMedDato))
                && (tilOgMedDato == null || !ytelse.fom().isAfter(tilOgMedDato));
    }

    private static LocalDate fom(Ytelse ytelse, LocalDate grense) {
        return grense == null || ytelse.fom().isAfter(grense) ? ytelse.fom() : grense;
    }

    private static LocalDate tom(Ytelse ytelse, LocalDate grense) {
        return grense == null || ytelse.tom().isBefore(grense) ? ytelse.tom() : grense;
    }

    private static boolean erVirkedag(LocalDate dato) {
        return dato.getDayOfWeek() != DayOfWeek.SATURDAY && dato.getDayOfWeek() != DayOfWeek.SUNDAY;
    }
}
