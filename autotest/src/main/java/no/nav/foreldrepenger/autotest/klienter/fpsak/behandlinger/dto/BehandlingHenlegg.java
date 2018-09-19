package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

public class BehandlingHenlegg extends BehandlingIdPost{
    String årsakKode;
    String begrunnelse;
    
    
    public BehandlingHenlegg(int behandlingId, int behandlingVersjon, String årsakKode, String begrunnelse) {
        super(behandlingId, behandlingVersjon);
        this.årsakKode = årsakKode;
        this.begrunnelse = begrunnelse;
    }
}
