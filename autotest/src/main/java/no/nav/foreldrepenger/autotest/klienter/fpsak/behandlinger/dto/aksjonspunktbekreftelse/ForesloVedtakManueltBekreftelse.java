package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="")
public class ForesloVedtakManueltBekreftelse extends AksjonspunktBekreftelse {

    public ForesloVedtakManueltBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
}
