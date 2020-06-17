package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class OrganisasjonAdresseModeller {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("modells")
    private List<OrganisasjonAdresseModell> modells = new ArrayList<>();
    public OrganisasjonAdresseModeller(){
    }

    public OrganisasjonAdresseModeller(List<OrganisasjonAdresseModell> modells){
        this.modells = modells;
    }
    public List<OrganisasjonAdresseModell> getModells() {
        return modells;
    }

    public void addAddressModells(OrganisasjonAdresseModell modell) {
        modells.add(modell);
    }
}
