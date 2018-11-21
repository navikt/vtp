package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

import java.time.LocalDate;

public abstract class VurderingAvKlageBekreftelse extends AksjonspunktBekreftelse {

    private static final String VURDERING_STADFEST = "STADFESTE_YTELSESVEDTAK";
    private static final String VURDERING_MEDHOLD = "MEDHOLD_I_KLAGE";
    
    protected String klageVurdering;
    protected String klageMedholdArsak;
    protected String klageVurderingOmgjoer;
    protected String fritekstTilBrev;
    protected LocalDate vedtaksdatoPaklagdBehandling;
    
    public VurderingAvKlageBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        this.vedtaksdatoPaklagdBehandling = LocalDate.now(); //TODO: hent dato til påklagd vedtak. Nødvendig?
    }

    // Omgjør vedtaket
    public VurderingAvKlageBekreftelse bekreftMedhold(String årsak) {
        klageVurdering  = VURDERING_MEDHOLD;
        klageMedholdArsak = årsak;
        return this;
    }

    // oppretthold vedtaket
    public VurderingAvKlageBekreftelse bekreftStadfestet() {
        klageVurdering  = VURDERING_STADFEST;
        return this;
    }

    public VurderingAvKlageBekreftelse fritekstBrev(String fritekst) {
        fritekstTilBrev = fritekst;
        return this;
    }

    @BekreftelseKode(kode="5035")
    public static class VurderingAvKlageNfpBekreftelse extends VurderingAvKlageBekreftelse {

        public VurderingAvKlageNfpBekreftelse(Fagsak fagsak, Behandling behandling) {
            super(fagsak, behandling);
        }
    }

    @BekreftelseKode(kode="5036")
    public static class VurderingAvKlageNkBekreftelse extends VurderingAvKlageBekreftelse {

        private static final String VURDERING_OPPHEVE = "OPPHEVE_YTELSESVEDTAK";
        private static final String VURDERING_HJEMSENDE = "HJEMSENDE_UTEN_Å_OPPHEVE";

        public VurderingAvKlageNkBekreftelse(Fagsak fagsak, Behandling behandling) {
            super(fagsak, behandling);
        }

        public VurderingAvKlageNkBekreftelse bekreftOpphevet(String årsak) {
            klageVurdering  = VURDERING_OPPHEVE;
            klageMedholdArsak = årsak;
            return this;
        }

        public VurderingAvKlageNkBekreftelse bekreftHjemsende() {
            klageVurdering = VURDERING_HJEMSENDE;
            return this;
        }


    }

}
