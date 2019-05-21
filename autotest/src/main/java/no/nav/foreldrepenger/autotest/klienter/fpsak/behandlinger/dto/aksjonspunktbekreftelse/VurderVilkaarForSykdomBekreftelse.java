package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;


@BekreftelseKode(kode="5044")
public class VurderVilkaarForSykdomBekreftelse extends AksjonspunktBekreftelse {

    public VurderVilkaarForSykdomBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    //TODO Stub

}
