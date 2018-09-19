package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5058")
public class VurderBeregnetInntektsAvvikBekreftelse extends AksjonspunktBekreftelse {
    
    protected List<InntektPrAndel> inntektPrAndelList = new ArrayList<>();
    
    public VurderBeregnetInntektsAvvikBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public void leggTilInntekt(int inntekt) {
        inntektPrAndelList.add(new InntektPrAndel(inntekt, 1));
    }
    
    class InntektPrAndel {
        int inntekt;
        int andelsnr;
        
        public InntektPrAndel(int inntekt, int andelsnr) {
            super();
            this.inntekt = inntekt;
            this.andelsnr = andelsnr;
        }
    }
}
