package no.nav.axsys;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

class Enhetstilgang {
    @JsonProperty("enheter")
    private final List<OrganisasjonsEnhet> enheter;

    public Enhetstilgang(List<OrganisasjonsEnhet> enheter) {
        this.enheter = enheter;
    }

    List<OrganisasjonsEnhet> getEnheter() {
        return enheter;
    }

}
