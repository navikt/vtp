package no.nav.foreldrepenger.fpmock2.testmodell.organisasjon;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrganisasjonModeller {
    public OrganisasjonModeller() {
    }

    public OrganisasjonModeller(List<OrganisasjonModell> modeller) {
        this.modeller = modeller;
    }

    @JsonInclude(value=JsonInclude.Include.ALWAYS)
    @JsonProperty("modeller")
    private List<OrganisasjonModell> modeller = new ArrayList<>();

    public List<OrganisasjonModell> getModeller() {
        return modeller;
    }

    public void leggTil(OrganisasjonModell modell) {
        modeller.add(modell);
    }
}
