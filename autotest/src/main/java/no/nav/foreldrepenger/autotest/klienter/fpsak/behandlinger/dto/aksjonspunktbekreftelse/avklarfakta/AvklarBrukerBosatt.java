package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5020")
public class AvklarBrukerBosatt extends AksjonspunktBekreftelse {

    protected boolean bosattVurdering;
    
    public AvklarBrukerBosatt(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public void bekreftBrukerErBosatt() {
        bosattVurdering = true;
    }
    
    public void bekreftBrukerErIkkeBosatt() {
        bosattVurdering = false;
    }
    
    

}
