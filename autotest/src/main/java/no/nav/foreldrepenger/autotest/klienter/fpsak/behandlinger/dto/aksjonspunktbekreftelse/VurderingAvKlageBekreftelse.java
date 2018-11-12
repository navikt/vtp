package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

public abstract class VurderingAvKlageBekreftelse extends AksjonspunktBekreftelse {

    private static final String VURDERING_STADFEST = "STADFESTE_YTELSESVEDTAK";
    private static final String VURDERING_AVVIS = "AVVIS_KLAGE";
    private static final String VURDERING_MEDHOLD = "MEDHOLD_I_KLAGE";
    
    protected String klageVurdering;
    protected String klageMedholdArsak;
    protected String klageAvvistArsak;
    protected LocalDate vedtaksdatoPaklagdBehandling;
    
    public VurderingAvKlageBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        vedtaksdatoPaklagdBehandling = LocalDate.now(); //TODO: MV: endre naar formkrav er lagt inn?
    }

    // Omgjør vedtaket
    public VurderingAvKlageBekreftelse bekreftMedhold(String årsak) {
        klageVurdering  = VURDERING_MEDHOLD;
        klageMedholdArsak = årsak;
        return this;
    }

    public VurderingAvKlageBekreftelse bekreftAvvist(String årsak) {
        klageVurdering  = VURDERING_AVVIS;
        klageAvvistArsak = årsak;
        return this;
    }

    // oppretthold vedtaket
    public VurderingAvKlageBekreftelse bekreftStadfestet() {
        klageVurdering  = VURDERING_STADFEST;
        return this;
    }

    public VurderingAvKlageBekreftelse setVedtaksdatoPaklagdBehandling(LocalDate dato) {
        vedtaksdatoPaklagdBehandling = dato;
        return this;
    }
}
