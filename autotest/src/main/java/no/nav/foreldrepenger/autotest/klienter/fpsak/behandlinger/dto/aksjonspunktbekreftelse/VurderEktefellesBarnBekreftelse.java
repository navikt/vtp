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
    
    public VurderEktefellesBarnBekreftelse bekreftBarnErEktefellesBarn() {
        ektefellesBarn = true;
        return this;
    }
    
    public VurderEktefellesBarnBekreftelse bekreftBarnErIkkeEktefellesBarn() {
        ektefellesBarn = false;
        return this;
    }
}
