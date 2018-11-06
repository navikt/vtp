package no.nav.foreldrepenger.autotest.klienter.vtp.sak.dto;

import java.util.List;

public class OpprettSakRequestDTO {

    protected List<String> lokalIdent;
    protected String fagområde;
    protected String fagsystem;
    protected String sakstype;

    public OpprettSakRequestDTO(List<String> lokalIdent, String fagområde, String fagsystem, String sakstype){
        this.lokalIdent = lokalIdent;
        this.fagområde = fagområde;
        this.fagsystem = fagsystem;
        this.sakstype = sakstype;
    }
}
