package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5015")
public class ForesloVedtakBekreftelse extends AksjonspunktBekreftelse {

    //TODO se om dette stemmer enda
    //protected int antallBarn;
    protected Kode avslagCode;
    //protected int beregningResultat;
    
    public ForesloVedtakBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    /*
    public void setAntallBarn(int antallBarn) {
        this.antallBarn = antallBarn;
    }
    */

    public void setAvslagCode(Kode avslagCode) {
        this.avslagCode = avslagCode;
    }

    /*
    public void setBeregningResultat(int beregningResultat) {
        this.beregningResultat = beregningResultat;
    }
    */
    
    
}
