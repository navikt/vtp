package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("søker")
public class SøkerModell extends VoksenModell {

    public SøkerModell() {
    }

    public SøkerModell(String lokalIdent, String navn, LocalDate fødselsdato, Kjønn kjønn) {
        super(lokalIdent, navn, fødselsdato, kjønn);
    }

}
