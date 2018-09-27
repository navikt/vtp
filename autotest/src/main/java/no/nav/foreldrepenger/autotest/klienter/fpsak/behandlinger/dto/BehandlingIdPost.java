package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BehandlingIdPost {
    
    protected int behandlingId;
    protected int behandlingVersjon;
    
    
    public BehandlingIdPost(int behandlingId, int behandlingVersjon) {
        super();
        this.behandlingId = behandlingId;
        this.behandlingVersjon = behandlingVersjon;
    }
    
    public BehandlingIdPost(Behandling behandling) {
        this(behandling.id, behandling.versjon);
    }
}
