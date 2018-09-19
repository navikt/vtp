package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpprettSak {

    public String journalpostId;
    public String behandlingstemaOffisiellKode;
    public String aktørId;
    
    public OpprettSak(String journalpostId, String behandlingstemaOffisiellKode, String aktørId) {
        super();
        this.journalpostId = journalpostId;
        this.behandlingstemaOffisiellKode = behandlingstemaOffisiellKode;
        this.aktørId = aktørId;
    }
}
