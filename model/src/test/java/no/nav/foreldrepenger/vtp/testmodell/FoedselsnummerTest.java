package no.nav.foreldrepenger.vtp.testmodell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;

public class FoedselsnummerTest {

    @Test
    public void fiktiv_vilkaarlig_kjonn_lager_fnr(){
        FiktiveFnr fiktiveFnr = new FiktiveFnr();
        String fnr = fiktiveFnr.tilfeldigFnr();
        assertThat(fnr).isNotEmpty();
        assertThat(fnr).hasSize(11);
    }

    @Test
    public void fiktiv_fnr_kjonn_mann() {
        FiktiveFnr fiktiveFnr = new FiktiveFnr();
        String fnr = fiktiveFnr.tilfeldigMannFnr();
        assertThat(fnr).hasSize(11);

        assertTrue(Integer.parseInt(fnr.substring(6,9)) % 2 == 1);
    }

    @Test
    public void fiktiv_fnr_kjonn_kvinne() {
        FiktiveFnr fiktiveFnr = new FiktiveFnr();
        String fnr = fiktiveFnr.tilfeldigKvinneFnr();
        assertThat(fnr).hasSize(11);
        assertTrue(Integer.parseInt(fnr.substring(6,9)) % 2 == 0);
    }

    @Test
    public void fiktiv_dnr() {
        FiktiveFnr fiktiveFnr = new FiktiveFnr();
        String fnr = fiktiveFnr.tilfeldigKvinneDnr();
        assertThat(fnr).hasSize(11);
        assertTrue(Integer.parseInt((fnr.substring(0,1))) >= 4);
    }

    @Test
    public void fiktiv_fnr_barn(){
        FiktiveFnr fiktiveFnr = new FiktiveFnr();
        String fnr = fiktiveFnr.tilfeldigBarnUnderTreAarFnr();
        assertThat(fnr).hasSize(11);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy");
        LocalDate barnetsFoedselsdag = LocalDate.parse(fnr.substring(0,6), dateTimeFormatter);
        long yearDiff = barnetsFoedselsdag.until(LocalDate.now(), ChronoUnit.YEARS);

        assertThat(yearDiff).isBetween(0L, 3L);

    }
}
