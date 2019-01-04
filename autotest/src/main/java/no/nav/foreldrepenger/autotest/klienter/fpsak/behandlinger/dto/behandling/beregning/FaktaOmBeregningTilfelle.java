package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class FaktaOmBeregningTilfelle extends Kode {

    private static final String DISCRIMINATOR = "FAKTA_OM_BEREGNING_TILFELLE";

    public static final FaktaOmBeregningTilfelle VURDER_MOTTAR_YTELSE = new FaktaOmBeregningTilfelle("VURDER_MOTTAR_YTELSE", "vurder_mottar_ytelse");

    private FaktaOmBeregningTilfelle(String kode, String navn){
        super(DISCRIMINATOR, kode, navn);
    }

}
