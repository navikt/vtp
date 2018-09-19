package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BehandlingHenlegg extends BehandlingIdPost{
    String årsakKode;
    String begrunnelse;
    
    
    public BehandlingHenlegg(int behandlingId, int behandlingVersjon, String årsakKode, String begrunnelse) {
        super(behandlingId, behandlingVersjon);
        this.årsakKode = årsakKode;
        this.begrunnelse = begrunnelse;
    }
}
