package no.nav.foreldrepenger.vtp.autotest.sak;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class OpprettSakRequestDTO {

    @JsonProperty("lokalIdent")
    private List<String> lokalIdent;

    @JsonProperty("fagområde")
    private String fagområde;

    @JsonProperty("fagsystem")
    private String fagsystem;

    @JsonProperty("sakstype")
    private String sakstype;

    public List<String> getLokalIdent() {
        return lokalIdent;
    }

    public void setLokalIdent(List<String> lokalIdent) {
        this.lokalIdent = lokalIdent;
    }

    public String getFagområde() {
        return fagområde;
    }

    public void setFagområde(String fagområde) {
        this.fagområde = fagområde;
    }

    public String getFagsystem() {
        return fagsystem;
    }

    public void setFagsystem(String fagsystem) {
        this.fagsystem = fagsystem;
    }

    public String getSakstype() {
        return sakstype;
    }

    public void setSakstype(String sakstype) {
        this.sakstype = sakstype;
    }
}
