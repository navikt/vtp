package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

public class BekreftedeAksjonspunkter {
    
    protected BehandlingId behandlingId;
    protected int behandlingVersjon;
    protected List<AksjonspunktBekreftelse> bekreftedeAksjonspunktDtoer;
    
    
    public BekreftedeAksjonspunkter(Fagsak fagsak, Behandling behandling, List<AksjonspunktBekreftelse> aksjonspunktBekreftelser) {
        this(new BehandlingId(fagsak.saksnummer, behandling.id), behandling.versjon, aksjonspunktBekreftelser);
    }
    
    public BekreftedeAksjonspunkter(BehandlingId behandlingId, int behandlingVersjon,
            List<AksjonspunktBekreftelse> bekreftedeAksjonspunktDtoer) {
        super();
        this.behandlingId = behandlingId;
        this.behandlingVersjon = behandlingVersjon;
        this.bekreftedeAksjonspunktDtoer = bekreftedeAksjonspunktDtoer;
    }

    static class BehandlingId {
        
        long saksnummer;
        int behandlingId;
        
        public BehandlingId(long saksnummer, int behandlingId) {
            super();
            this.saksnummer = saksnummer;
            this.behandlingId = behandlingId;
        }
    }
}
