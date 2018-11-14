package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5038")
public class VurderBeregnetInntektsAvvikBekreftelse extends AksjonspunktBekreftelse {
    
    protected List<InntektPrAndel> inntektPrAndelList = new ArrayList<>();
    // TODO MV: sjekke om frilanser skal være her
    protected Integer inntektFrilanser;
    
    public VurderBeregnetInntektsAvvikBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public VurderBeregnetInntektsAvvikBekreftelse leggTilInntekt(Integer inntekt) {
        inntektPrAndelList.add(new InntektPrAndel(inntekt, 1L));
        return this;
    }

    public VurderBeregnetInntektsAvvikBekreftelse leggTilInntektFrilans(Integer inntektFrilanser) {
        this.inntektFrilanser = inntektFrilanser;
        return this;
    }
    
    class InntektPrAndel {

        protected Integer inntekt;
        protected Long andelsnr;
        
        public InntektPrAndel(Integer inntekt, Long andelsnr) {
            super();
            this.inntekt = inntekt;
            this.andelsnr = andelsnr;
        }
    }
}
