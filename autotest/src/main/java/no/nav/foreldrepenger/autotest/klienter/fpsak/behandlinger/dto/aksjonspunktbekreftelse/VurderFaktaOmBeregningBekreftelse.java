package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.ArbeidstakerandelUtenIMMottarYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.FaktaOmBeregningTilfelle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.MottarYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5058")
public class VurderFaktaOmBeregningBekreftelse extends AksjonspunktBekreftelse {

    protected List<FaktaOmBeregningTilfelle> faktaOmBeregningTilfeller = new ArrayList<>();
    protected MottarYtelse mottarYtelse;

    public VurderFaktaOmBeregningBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public VurderFaktaOmBeregningBekreftelse leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle faktaOmBeregningTilfelle) {
        this.faktaOmBeregningTilfeller.add(faktaOmBeregningTilfelle);
        return this;
    }

    public VurderFaktaOmBeregningBekreftelse leggTilMottarYtelse(boolean mottarYtelse, List<ArbeidstakerandelUtenIMMottarYtelse> arbeidstakerandelUtenIMMottarYtelses){
        this.mottarYtelse = new MottarYtelse(mottarYtelse, arbeidstakerandelUtenIMMottarYtelses);
        return this;
    }

}
