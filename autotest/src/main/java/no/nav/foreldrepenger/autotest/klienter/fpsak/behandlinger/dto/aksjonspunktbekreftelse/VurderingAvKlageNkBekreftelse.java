package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5036")
public class VurderingAvKlageNkBekreftelse extends VurderingAvKlageBekreftelse {

    private static final String VURDERING_OPPHEVE = "OPPHEVE_YTELSESVEDTAK";
    private static final String VURDERING_HJEMSENDE = "HJEMSENDE_UTEN_Å_OPPHEVE";

    public VurderingAvKlageNkBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public VurderingAvKlageNkBekreftelse bekreftOpphevet(String årsak) {
        klageVurdering  = VURDERING_OPPHEVE;
        klageMedholdArsak = årsak; // annet navn?
        return this;
    }

    public VurderingAvKlageNkBekreftelse bekreftHjemsende() {
        klageVurdering = VURDERING_HJEMSENDE;
        return this;
    }


}
