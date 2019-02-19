package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.ArbeidstakerandelUtenIMMottarYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.MottarYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5058")
public class VurderFaktaOmBeregningBekreftelse extends AksjonspunktBekreftelse {

    protected FastsettMaanedsinntektFL fastsettMaanedsinntektFL;
    protected List<String> faktaOmBeregningTilfeller = new ArrayList<>();
    protected MottarYtelse mottarYtelse;

    public VurderFaktaOmBeregningBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public VurderFaktaOmBeregningBekreftelse leggTilFaktaOmBeregningTilfeller(String kode) {
        this.faktaOmBeregningTilfeller.add(kode);
        return this;
    }

    public VurderFaktaOmBeregningBekreftelse leggTilMottarYtelse(boolean mottarYtelse, List<ArbeidstakerandelUtenIMMottarYtelse> arbeidstakerandelUtenIMMottarYtelses){
        this.mottarYtelse = new MottarYtelse(mottarYtelse, arbeidstakerandelUtenIMMottarYtelses);
        return this;
    }

    public VurderFaktaOmBeregningBekreftelse leggTilMaanedsinntekt(int maanedsinntekt) {
        fastsettMaanedsinntektFL = new FastsettMaanedsinntektFL(maanedsinntekt);
        return this;
    }

}
