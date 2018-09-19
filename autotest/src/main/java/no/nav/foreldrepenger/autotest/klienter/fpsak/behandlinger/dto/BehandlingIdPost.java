package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

public class BehandlingIdPost {
    int behandlingId;
    int behandlingVersjon;
    
    
    public BehandlingIdPost(int behandlingId, int behandlingVersjon) {
        super();
        this.behandlingId = behandlingId;
        this.behandlingVersjon = behandlingVersjon;
    }
}
