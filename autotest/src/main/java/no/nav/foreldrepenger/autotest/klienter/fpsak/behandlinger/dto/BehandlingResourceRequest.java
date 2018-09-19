package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

public class BehandlingResourceRequest {

    int behandlingId;
    long saksnummer;
    
    
    public BehandlingResourceRequest(int behandlingId, long saksnummer) {
        this.behandlingId = behandlingId;
        this.saksnummer = saksnummer;
    }
}
