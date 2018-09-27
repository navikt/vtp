package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5007")
public class VurderSoknadsfristBekreftelse extends AksjonspunktBekreftelse{

    protected boolean erVilkarOk;
    protected LocalDate mottattDato;
    protected LocalDate omsorgsovertakelseDato;
    
    public VurderSoknadsfristBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        omsorgsovertakelseDato = behandling.soknad.getOmsorgsovertakelseDato();
        mottattDato = behandling.soknad.getMottattDato();
    }
    
    public void bekreftVilkårErOk() {
        erVilkarOk = true;
    }
    
    public void bekreftVilkårErIkkeOk() {
        erVilkarOk = false;
    }
}
