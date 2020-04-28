package no.nav.foreldrepenger.vtp.testmodell.enheter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Norg2Modell {

    @JsonProperty("diskresjonskode")
    private String diskresjonskode;

    @JsonProperty("enhetId")
    private String enhetId;

    @JsonProperty("navn")
    private String navn;

    @JsonProperty("type")
    private String type;

    @JsonProperty("status")
    private String status;
    
    @JsonProperty("tema")
    private String tema;

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
    
    public void setTema(String tema) {
        this.tema = tema;
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
    
    public String getTema() {
        return tema;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
