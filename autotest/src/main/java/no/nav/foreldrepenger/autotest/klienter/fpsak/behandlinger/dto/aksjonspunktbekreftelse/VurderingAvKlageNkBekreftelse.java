package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5036")
public class VurderingAvKlageNkBekreftelse extends VurderingAvKlageBekreftelse {

    private static final String VURDERING_OPPHEVE = "OPPHEVE_YTELSESVEDTAK";

    public VurderingAvKlageNkBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public VurderingAvKlageNkBekreftelse bekreftOpphevet() {
        klageVurdering  = VURDERING_OPPHEVE;
        return this;
    }


}
