package no.nav.axsys;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class OrganisasjonsEnhet {
    @JsonProperty("enhetId")
    private final String enhetId;
    @JsonProperty("navn")
    private final String enhetNavn;
    @JsonProperty("fagomrader")
    private final Set<String> fagområder;

    public OrganisasjonsEnhet(String enhetId, String enhetNavn, Set<String> fagområder) {
        this.enhetId = enhetId;
        this.enhetNavn = enhetNavn;
        this.fagområder = fagområder;
    }

    public String getEnhetId() { return enhetId; }

    public String getEnhetNavn(){ return enhetNavn; }

}
