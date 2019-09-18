package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrganisasjonModeller {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("modeller")
    private List<OrganisasjonModell> modeller = new ArrayList<>();

    public OrganisasjonModeller() {
    }

    public OrganisasjonModeller(List<OrganisasjonModell> modeller) {
        this.modeller = modeller;
    }

    public List<OrganisasjonModell> getModeller() {
        return modeller;
    }

    public void leggTil(OrganisasjonModell modell) {
        modeller.add(modell);
    }
}
