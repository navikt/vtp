package no.nav.foreldrepenger.autotest.klienter.vtp.sak.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
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
