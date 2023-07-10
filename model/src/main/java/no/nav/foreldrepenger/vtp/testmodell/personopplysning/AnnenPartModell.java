package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell.Rolle;

@JsonTypeName("annenPart")
public class AnnenPartModell extends VoksenModell {

    @JsonProperty("rolle")
    private Rolle rolle;

    public AnnenPartModell() {
    }

    public AnnenPartModell(String lokalIdent, String navn, LocalDate fødselsdato, boolean harUføretrygd, Kjønn kjønn) {
        super(lokalIdent, navn, fødselsdato, harUføretrygd, kjønn);
    }

    public Rolle rolle() {
        return rolle;
    }

    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }
}
