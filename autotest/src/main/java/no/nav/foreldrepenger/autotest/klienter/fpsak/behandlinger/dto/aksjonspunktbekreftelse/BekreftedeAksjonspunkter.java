package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingResourceRequest;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

public class BekreftedeAksjonspunkter {
    
    protected BehandlingResourceRequest behandlingId;
    protected int behandlingVersjon;
    protected List<AksjonspunktBekreftelse> bekreftedeAksjonspunktDtoer;
    
    
    public BekreftedeAksjonspunkter(Fagsak fagsak, Behandling behandling, List<AksjonspunktBekreftelse> aksjonspunktBekreftelser) {
        this(new BehandlingResourceRequest(behandling.id, fagsak.saksnummer), behandling.versjon, aksjonspunktBekreftelser);
    }
    
    public BekreftedeAksjonspunkter(BehandlingResourceRequest behandlingId, int behandlingVersjon,
            List<AksjonspunktBekreftelse> bekreftedeAksjonspunktDtoer) {
        super();
        this.behandlingId = behandlingId;
        this.behandlingVersjon = behandlingVersjon;
        this.bekreftedeAksjonspunktDtoer = bekreftedeAksjonspunktDtoer;
    }
}
