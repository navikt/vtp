package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5055")
public class KontrollerRevuderingsbehandling extends AksjonspunktBekreftelse {

    public KontrollerRevuderingsbehandling(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
}
