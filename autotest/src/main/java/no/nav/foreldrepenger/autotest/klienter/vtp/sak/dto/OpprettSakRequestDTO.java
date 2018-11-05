package no.nav.foreldrepenger.autotest.klienter.vtp.sak.dto;

import java.util.List;

public class OpprettSakRequestDTO {

    private List<String> lokalIdent;
    private String fagområde;
    private String fagsystem;
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
