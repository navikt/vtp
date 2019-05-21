package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpprettSak {

    protected String journalpostId;
    protected String behandlingstemaOffisiellKode;
    protected String aktørId;
    
    public OpprettSak(String journalpostId, String behandlingstemaOffisiellKode, String aktørId) {
        super();
        this.journalpostId = journalpostId;
        this.behandlingstemaOffisiellKode = behandlingstemaOffisiellKode;
        this.aktørId = aktørId;
    }
}
