package no.nav.foreldrepenger.fpmock2.testmodell.enheter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Norg2Modell {

    @JsonProperty("diskresjonskode")
    private String diskresjonskode;
    
    @JsonProperty("enhetId")
    private String enhetId;
    
    @JsonProperty("navn")
    private String navn;
    
    @JsonProperty("organisasjonsnummer")
    private String organisasjonsnummer;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("status")
    private String status;

    public String getDiskresjonskode() {
        return diskresjonskode;
    }

    public void setDiskresjonskode(String diskresjonskode) {
        this.diskresjonskode = diskresjonskode;
    }

    public String getEnhetId() {
        return enhetId;
    }

    public void setEnhetId(String enhetid) {
        this.enhetId = enhetid;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public void setOrganisasjonsnummer(String organisasjonsnummer) {
        this.organisasjonsnummer = organisasjonsnummer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
