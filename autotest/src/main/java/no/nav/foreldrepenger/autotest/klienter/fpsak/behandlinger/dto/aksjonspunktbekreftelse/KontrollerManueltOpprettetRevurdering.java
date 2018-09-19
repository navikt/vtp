package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5056")
public class KontrollerManueltOpprettetRevurdering extends AksjonspunktBekreftelse {

    public KontrollerManueltOpprettetRevurdering(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    
}
