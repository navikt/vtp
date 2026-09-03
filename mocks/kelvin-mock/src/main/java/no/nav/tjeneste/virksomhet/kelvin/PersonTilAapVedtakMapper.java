package no.nav.tjeneste.virksomhet.kelvin;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import no.nav.tjeneste.virksomhet.kelvin.ArbeidsavklaringspengerResponse.AAPPeriode;
import no.nav.tjeneste.virksomhet.kelvin.ArbeidsavklaringspengerResponse.AAPReduksjon;
import no.nav.tjeneste.virksomhet.kelvin.ArbeidsavklaringspengerResponse.AAPUtbetaling;
import no.nav.tjeneste.virksomhet.kelvin.ArbeidsavklaringspengerResponse.AAPVedtak;
import no.nav.tjeneste.virksomhet.kelvin.ArbeidsavklaringspengerResponse.Kildesystem;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

public class PersonTilAapVedtakMapper {

    private static final String DUMMY_SAKSNUMMER = "KELVIN-AAP-VTPSAK";

    private PersonTilAapVedtakMapper() {
    }

    public static List<AAPVedtak> tilAapVedtak(Person person, LocalDate fraOgMedDato, LocalDate tilOgMedDato) {
        if (person == null || person.ytelser() == null) {
            return List.of();
        }
        return person.ytelser().stream()
                .filter(ytelse -> ytelse.ytelse() == YtelseType.ARBEIDSAVKLARINGSPENGER)
                .filter(ytelse -> overlapper(ytelse, fraOgMedDato, tilOgMedDato))
                .map(ytelse -> tilVedtak(ytelse, fraOgMedDato, tilOgMedDato))
                .toList();
    }

    private static AAPVedtak tilVedtak(Ytelse ytelse, LocalDate fraOgMedDato, LocalDate tilOgMedDato) {
        var fom = maks(ytelse.fom(), fraOgMedDato);
        var tom = minimum(ytelse.tom(), tilOgMedDato);
        var periode = new AAPPeriode(fom, tom);
        var utbetalingsgrad = ytelse.utbetalingsgrad() == null ? 100 : ytelse.utbetalingsgrad();
        var redusertDagsats = (int) Math.round(ytelse.dagsats() * utbetalingsgrad / 100.0);
        var utbetaling = new AAPUtbetaling(
                periode,
                redusertDagsats * antallVirkedager(fom, tom),
                redusertDagsats,
                0, // abakus KELVIN-konsumenten summerer dagsats+barnetillegg uten null-håndtering
                new AAPReduksjon(null, null),
                utbetalingsgrad
        );
        return new AAPVedtak(
                null, // barnMedStonad - ikke i bruk av abakus
                null, // barnetillegg - ikke i bruk av abakus
                null, // beregningsgrunnlag - ikke i bruk av abakus
                ytelse.dagsats(),
                ytelse.dagsats(), // dagsatsEtterUføreReduksjon - alltid lik dagsats (ingen reduksjon simuleres)
                Kildesystem.KELVIN,
                periode,
                DUMMY_SAKSNUMMER,
                "LØPENDE",
                null, // vedtakId - ikke i bruk av abakus
                ytelse.fom(),
                List.of(utbetaling)
        );
    }

    private static boolean overlapper(Ytelse ytelse, LocalDate fraOgMedDato, LocalDate tilOgMedDato) {
        return (fraOgMedDato == null || !ytelse.tom().isBefore(fraOgMedDato))
                && (tilOgMedDato == null || !ytelse.fom().isAfter(tilOgMedDato));
    }

    private static LocalDate maks(LocalDate dato, LocalDate grense) {
        return grense == null || dato.isAfter(grense) ? dato : grense;
    }

    private static LocalDate minimum(LocalDate dato, LocalDate grense) {
        return grense == null || dato.isBefore(grense) ? dato : grense;
    }

    private static int antallVirkedager(LocalDate fom, LocalDate tom) {
        return (int) fom.datesUntil(tom.plusDays(1))
                .filter(dato -> dato.getDayOfWeek() != DayOfWeek.SATURDAY && dato.getDayOfWeek() != DayOfWeek.SUNDAY)
                .count();
    }
}
