package no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fagsak {

    
    public long saksnummer;
    protected Kode status;

    public Kode hentStatus() {
        return status;
    }
}
