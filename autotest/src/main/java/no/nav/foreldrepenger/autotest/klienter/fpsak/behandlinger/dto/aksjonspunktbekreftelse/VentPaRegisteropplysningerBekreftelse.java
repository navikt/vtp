package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="7010")
public class VentPaRegisteropplysningerBekreftelse extends AksjonspunktBekreftelse {

    public VentPaRegisteropplysningerBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

}
