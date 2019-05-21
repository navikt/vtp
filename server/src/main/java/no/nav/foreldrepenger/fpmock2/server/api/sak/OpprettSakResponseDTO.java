package no.nav.foreldrepenger.fpmock2.server.api.sak;


import com.fasterxml.jackson.annotation.JsonProperty;


public class OpprettSakResponseDTO {

    @JsonProperty("saksnummer")
    private String saksnummer;

    public OpprettSakResponseDTO(){}

    public OpprettSakResponseDTO(String saksnummer){
        this.saksnummer = saksnummer;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    public void setSaksnummer(String saksnummer) {
        this.saksnummer = saksnummer;
    }
}
