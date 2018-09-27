package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BehandlingResourceRequest {

    protected int behandlingId;
    protected long saksnummer;
    
    
    public BehandlingResourceRequest(int behandlingId, long saksnummer) {
        this.behandlingId = behandlingId;
        this.saksnummer = saksnummer;
    }
}
