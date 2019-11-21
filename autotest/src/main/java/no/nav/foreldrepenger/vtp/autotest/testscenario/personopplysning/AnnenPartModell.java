package no.nav.foreldrepenger.vtp.autotest.testscenario.personopplysning;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;


@JsonTypeName("annenPart")
public class AnnenPartModell extends VoksenModell {

    @JsonProperty("rolle")
    private FamilierelasjonModell.Rolle rolle;

    public AnnenPartModell() {
    }

    public AnnenPartModell(String lokalIdent, String navn, LocalDate fødselsdato, BrukerModell.Kjønn kjønn) {
        super(lokalIdent, navn, fødselsdato, kjønn);
    }

}
