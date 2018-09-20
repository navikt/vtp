package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Personopplysning;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5008")
public class AvklarFaktaOmsorgOgForeldreansvar extends AksjonspunktBekreftelse{

    protected int antallBarn;
    protected int originalAntallBarn;
    protected LocalDate omsorgsovertakelseDato;
    protected String vilkarType;
    protected String farSokerType;
    protected List<OmsorgovertakelseBarn> barn = new ArrayList<>();
    protected List<OmsorgovertakelseForelder> foreldre = new ArrayList<>();
    protected List<Object> ytelser = new ArrayList<>();
    
    public AvklarFaktaOmsorgOgForeldreansvar(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        //Set antall barn fra søknad
        antallBarn = behandling.soknad.antallBarn;
        
        //Set antall barn originalt fra søknad
        originalAntallBarn = behandling.soknad.antallBarn;
        
        //Set omsorgsovertakelsedato fra søknad
        omsorgsovertakelseDato = behandling.soknad.omsorgsovertakelseDato;
        
        //Legg til foreler fra søknad
        foreldre.add(new OmsorgovertakelseForelder(behandling.personopplysning));
        
        //TODO kan hentes fra kodeverk når det er på plass
        farSokerType = "Far har overtatt omsorgen for barnet mindre enn 56 uker etter adopsjon, med sikte på å overta foreldreansvaret alene";
        
        //Legg til bern fra søknad
        if(behandling.soknad.adopsjonFodelsedatoer != null){
            Map<Integer, LocalDate> fodselsdatoer = behandling.soknad.adopsjonFodelsedatoer;
            
            for(int i = 0; i < fodselsdatoer.size(); i++){
                barn.add(new OmsorgovertakelseBarn(fodselsdatoer.get(i+1), "SAKSBEH", (i+1)));
            }
        }
        else{
            for(int i = 0; i < behandling.soknad.antallBarn; i++){
                barn.add(new OmsorgovertakelseBarn(behandling.soknad.fodselsdatoer.get(i+1), "SAKSBEH", (i+1)));
            }
        }
    }
    
    
    
    public void setDødsdato(LocalDate dato){
        foreldre.get(0).dodsdato = dato;
    }
    
    public void setVilkårType(String vilkarType) {
        this.vilkarType = vilkarType;
    }
    
    protected class OmsorgovertakelseBarn
    {
        protected LocalDate fodselsdato;
        protected String opplysningsKilde;
        protected int nummer;
        
        public OmsorgovertakelseBarn(LocalDate fodselsdato, String opplysningsKilde, int nummer){
            this.fodselsdato = fodselsdato;
            this.opplysningsKilde = opplysningsKilde;
            this.nummer = nummer;
        }
    }
    
    protected class OmsorgovertakelseForelder
    {
        protected int id;
        protected LocalDate dodsdato;
        protected boolean erMor;
        protected String navn;
        protected String opplysningsKilde;
        protected String aktorId;
        
        public OmsorgovertakelseForelder(Personopplysning person){
            id = person.id;
            dodsdato = person.doedsdato;
            erMor = person.navBrukerKjonn.kode.equals("K");
            navn = person.navn;
            opplysningsKilde = "TPS";
            aktorId = "" + person.aktoerId;
        }
    }
}
