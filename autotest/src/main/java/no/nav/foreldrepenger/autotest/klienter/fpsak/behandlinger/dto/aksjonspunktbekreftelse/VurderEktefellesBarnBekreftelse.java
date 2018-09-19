package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5005")
public class VurderEktefellesBarnBekreftelse extends AksjonspunktBekreftelse{

    protected Boolean ektefellesBarn;
    
    public VurderEktefellesBarnBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        // TODO Auto-generated constructor stub
    }
    
    public void bekreftBarnErEktefellesBarn() {
        ektefellesBarn = true;
    }
    
    public void bekreftBarnErIkkeEktefellesBarn() {
        ektefellesBarn = false;
    }
}
