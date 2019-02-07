package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class FaktaOmBeregningTilfelle extends Kode {

    private static final String DISCRIMINATOR = "FAKTA_OM_BEREGNING_TILFELLE";

    public static final FaktaOmBeregningTilfelle VURDER_MOTTAR_YTELSE = new FaktaOmBeregningTilfelle("VURDER_MOTTAR_YTELSE", "vurder_mottar_ytelse");
    public static final FaktaOmBeregningTilfelle FASTSETT_MAANEDSINNTEKT_FL = new FaktaOmBeregningTilfelle("FASTSETT_MAANEDSINNTEKT_FL", "");

    public static final FaktaOmBeregningTilfelle FASTSETT_ETTERLØNN_SLUTTPAKKE = new FaktaOmBeregningTilfelle("FASTSETT_ETTERLØNN_SLUTTPAKKE", "fastsett_etterlønn_sluttpakke");
    public static final FaktaOmBeregningTilfelle VURDER_TIDSBEGRENSET_ARBEIDSFORHOLD = new FaktaOmBeregningTilfelle("VURDER_TIDSBEGRENSET_ARBEIDSFORHOLD", "vurder_tidsbegrenset_arbeidsforhold"); //$NON-NLS-1$
    public static final FaktaOmBeregningTilfelle FASTSETT_ENDRET_BEREGNINGSGRUNNLAG = new FaktaOmBeregningTilfelle("FASTSETT_ENDRET_BEREGNINGSGRUNNLAG", "fastsettt_endret_beregningsgrunnlag"); //$NON-NLS-1$


    public FaktaOmBeregningTilfelle() {

    }

    private FaktaOmBeregningTilfelle(String kode, String navn){
        super(DISCRIMINATOR, kode, navn);
    }

}
