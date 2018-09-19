package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

public class BehandlingByttEnhet extends BehandlingIdPost{
    
    String enhetNavn;
    String enhetId;
    String begrunnelse;
    
    
    public BehandlingByttEnhet(int behandlingId, int behandlingVersjon, String enhetNavn, String enhetId,
            String begrunnelse) {
        super(behandlingId, behandlingVersjon);
        this.enhetNavn = enhetNavn;
        this.enhetId = enhetId;
        this.begrunnelse = begrunnelse;
    }
}
