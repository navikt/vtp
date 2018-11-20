package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

import java.time.LocalDate;

public abstract class VurderingAvKlageBekreftelse extends AksjonspunktBekreftelse {

    private static final String VURDERING_STADFEST = "STADFESTE_YTELSESVEDTAK";
    //private static final String VURDERING_AVVIS = "AVVIS_KLAGE";
    private static final String VURDERING_MEDHOLD = "MEDHOLD_I_KLAGE";
    
    protected String klageVurdering;
    protected String klageMedholdArsak;
    protected String klageAvvistArsak;
    protected String klageVurderingOmgjoer;
    protected String fritekstTilBrev;
    protected LocalDate vedtaksdatoPaklagdBehandling;
    
    public VurderingAvKlageBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        this.vedtaksdatoPaklagdBehandling = LocalDate.now(); //TODO: hent dato til påklagd vedtak
    }

    // Omgjør vedtaket
    public VurderingAvKlageBekreftelse bekreftMedhold(String årsak) {
        klageVurdering  = VURDERING_MEDHOLD;
        klageMedholdArsak = årsak;
        return this;
    }

    /*public VurderingAvKlageBekreftelse bekreftAvvist(String årsak) {
        klageVurdering  = VURDERING_AVVIS;
        klageAvvistArsak = årsak;
        return this;
    }*/

    // oppretthold vedtaket
    public VurderingAvKlageBekreftelse bekreftStadfestet() {
        klageVurdering  = VURDERING_STADFEST;
        return this;
    }

    public VurderingAvKlageBekreftelse fritekstBrev(String fritekst) {
        fritekstTilBrev = fritekst;
        return this;
    }

}
