package no.nav.foreldrepenger.vtp.autotest.testscenario.personopplysning.brukermodell;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;


@JsonTypeName("søker")
public class SøkerModell extends VoksenModell {

    public SøkerModell() {
    }

    public SøkerModell(String lokalIdent, String navn, LocalDate fødselsdato, Kjønn kjønn) {
        super(lokalIdent, navn, fødselsdato, kjønn);
    }

}
