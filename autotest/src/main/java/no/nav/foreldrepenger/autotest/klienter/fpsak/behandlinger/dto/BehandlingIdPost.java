package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;

public class BehandlingIdPost {
    int behandlingId;
    int behandlingVersjon;
    
    
    public BehandlingIdPost(int behandlingId, int behandlingVersjon) {
        super();
        this.behandlingId = behandlingId;
        this.behandlingVersjon = behandlingVersjon;
    }
    
    public BehandlingIdPost(Behandling behandling) {
        this(behandling.id, behandling.versjon);
    }
}
