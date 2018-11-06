package no.nav.foreldrepenger.autotest.klienter.vtp.sak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpprettSakResponseDTO {

    @JsonProperty("saksnummer")
    protected String saksnummer;

    public String getSaksnummer() {
        return saksnummer;
    }

    public void setSaksnummer(String saksnummer) {
        this.saksnummer = saksnummer;
    }
}
