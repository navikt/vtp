package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.ArbeidstakerandelUtenIMMottarYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.MottarYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5058")
public class VurderFaktaOmBeregningBekreftelse extends AksjonspunktBekreftelse {

    protected FastsettMaanedsinntektFL fastsettMaanedsinntektFL;
    protected List<String> faktaOmBeregningTilfeller = new ArrayList<>();
    protected MottarYtelse mottarYtelse;
    
    protected YtelseForedeling kunYtelseFordeling;

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
    
    public VurderFaktaOmBeregningBekreftelse leggTilAndelerYtesle(double beløp, Kode inntektskategori) {
        kunYtelseFordeling = new YtelseForedeling();
        kunYtelseFordeling.leggTilYtelseAndeler(new YtelseAndeler(beløp, inntektskategori.kode));
        return this;
    }
    

    public class YtelseForedeling{
        
        public List<YtelseAndeler> andeler = new ArrayList<>();
        
        public YtelseForedeling() {
            // TODO Auto-generated constructor stub
        }
        
        public void leggTilYtelseAndeler(YtelseAndeler andel) {
            andel.setAndelsnr(andeler.size() + 1);
            andeler.add(andel);
        }
        
    }
    
    public class YtelseAndeler{
        public int andelsnr;
        public double fastsattBeløp;
        public String inntektskategori;
        public boolean lagtTilAvSaksbehandler;
        public boolean nyAndel;
        
        public YtelseAndeler() {
            // TODO Auto-generated constructor stub
        }
        
        public YtelseAndeler(double fastsattBeløp, String inntektskategori) {
            super();
            this.fastsattBeløp = fastsattBeløp;
            this.inntektskategori = inntektskategori;
        }
        
        public void setAndelsnr(int andelsnr) {
            this.andelsnr = andelsnr;
        }
        
    }

}
