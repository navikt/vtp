package no.nav.foreldrepenger.vtp.testmodell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;

public class FoedselsnummerTest {

    @Test
    public void fiktiv_vilkaarlig_kjonn_lager_fnr(){
        var fiktiveFnr = new FiktiveFnr();
        var fnr = fiktiveFnr.tilfeldigFnr();
        assertThat(fnr).isNotEmpty();
        assertThat(fnr).hasSize(11);
    }

    @Test
    public void fiktiv_fnr_kjonn_mann() {
        var fiktiveFnr = new FiktiveFnr();
        var fnr = fiktiveFnr.tilfeldigMannFnr();
        assertThat(fnr).hasSize(11);

        assertTrue(Integer.parseInt(fnr.substring(6,9)) % 2 == 1);
    }

    @Test
    public void fiktiv_fnr_kjonn_kvinne() {
        var fiktiveFnr = new FiktiveFnr();
        var fnr = fiktiveFnr.tilfeldigKvinneFnr();
        assertThat(fnr).hasSize(11);
        assertTrue(Integer.parseInt(fnr.substring(6,9)) % 2 == 0);
    }

    @Test
    public void fiktiv_dnr() {
        var fiktiveFnr = new FiktiveFnr();
        var fnr = fiktiveFnr.tilfeldigKvinneDnr();
        assertThat(fnr).hasSize(11);
        assertTrue(Integer.parseInt((fnr.substring(0,1))) >= 4);
    }

    @Test
    public void fiktiv_fnr_barn(){
        var fiktiveFnr = new FiktiveFnr();
        var fnr = fiktiveFnr.tilfeldigBarnUnderTreAarFnr();
        assertThat(fnr).hasSize(11);
        var fødselsdato = Integer.parseInt(fnr.substring(0, 6)) - 4_000;
        var barnetsFoedselsdag = LocalDate.parse(String.format("%06d",fødselsdato), DateTimeFormatter.ofPattern("ddMMyy"));
        assertThat(barnetsFoedselsdag).isAfter(LocalDate.now().minusYears(3));
    }
}
