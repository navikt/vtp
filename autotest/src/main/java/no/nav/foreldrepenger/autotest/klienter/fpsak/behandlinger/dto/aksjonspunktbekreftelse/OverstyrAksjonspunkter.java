package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

public class OverstyrAksjonspunkter {

    protected int behandlingId;
    protected String saksnummer;
    protected int behandlingVersjon;
    protected List<AksjonspunktBekreftelse> overstyrteAksjonspunktDtoer;
    
    public OverstyrAksjonspunkter(Fagsak fagsak, Behandling behandling, List<AksjonspunktBekreftelse> aksjonspunktBekreftelser) {
        this(behandling.id, "" + fagsak.saksnummer, behandling.versjon, aksjonspunktBekreftelser);
    }
    
    public OverstyrAksjonspunkter(int behandlingId, String saksnummer, int behandlingVersjon,
            List<AksjonspunktBekreftelse> bekreftedeAksjonspunktDtoer) {
        super();
        this.behandlingId = behandlingId;
        this.saksnummer = saksnummer;
        this.behandlingVersjon = behandlingVersjon;
        this.overstyrteAksjonspunktDtoer = bekreftedeAksjonspunktDtoer;
    }
}
