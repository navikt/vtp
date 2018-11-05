package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5035")
public class VurderingAvKlageNfpBekreftelse extends VurderingAvKlageBekreftelse {
    public VurderingAvKlageNfpBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
}
