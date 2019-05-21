package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5006")
public class MannAdoptererAleneBekreftelse extends AksjonspunktBekreftelse{

    protected boolean mannAdoptererAlene;
    
    public MannAdoptererAleneBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public void bekreftMannAdoptererAlene() {
        mannAdoptererAlene = true;
    }
    
    public void bekreftMannAdoptererIkkeAlene() {
        mannAdoptererAlene = false;
    }
}
