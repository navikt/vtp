package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BehandlingByttEnhet extends BehandlingIdPost{
    
    protected String enhetNavn;
    protected String enhetId;
    protected String begrunnelse;
    
    
    public BehandlingByttEnhet(int behandlingId, int behandlingVersjon, String enhetNavn, String enhetId,
            String begrunnelse) {
        super(behandlingId, behandlingVersjon);
        this.enhetNavn = enhetNavn;
        this.enhetId = enhetId;
        this.begrunnelse = begrunnelse;
    }
}
