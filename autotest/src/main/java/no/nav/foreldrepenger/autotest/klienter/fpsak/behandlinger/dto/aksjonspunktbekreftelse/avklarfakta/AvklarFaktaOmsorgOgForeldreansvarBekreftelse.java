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
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5008")
public class AvklarFaktaOmsorgOgForeldreansvarBekreftelse extends AksjonspunktBekreftelse{

    protected int antallBarn;
    protected int originalAntallBarn;
    protected LocalDate omsorgsovertakelseDato;
    protected String vilkarType;
    protected String farSokerType;
    protected List<OmsorgovertakelseBarn> barn = new ArrayList<>();
    protected List<OmsorgovertakelseForelder> foreldre = new ArrayList<>();
    protected List<Object> ytelser = new ArrayList<>();
    
    public AvklarFaktaOmsorgOgForeldreansvarBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        //Set antall barn fra søknad
        antallBarn = behandling.soknad.getAntallBarn();
        
        //Set antall barn originalt fra søknad
        originalAntallBarn = behandling.soknad.getAntallBarn();
        
        //Set omsorgsovertakelsedato fra søknad
        omsorgsovertakelseDato = behandling.soknad.getOmsorgsovertakelseDato();
        
        //Legg til foreler fra søknad
        foreldre.add(new OmsorgovertakelseForelder(behandling.personopplysning));
        
        //TODO kan hentes fra kodeverk når det er på plass
        farSokerType = "Far har overtatt omsorgen for barnet mindre enn 56 uker etter adopsjon, med sikte på å overta foreldreansvaret alene";
        
        //Legg til bern fra søknad
        if(behandling.soknad.getAdopsjonFodelsedatoer() != null){
            Map<Integer, LocalDate> fodselsdatoer = behandling.soknad.getAdopsjonFodelsedatoer();
            
            for(int i = 0; i < fodselsdatoer.size(); i++){
                barn.add(new OmsorgovertakelseBarn(fodselsdatoer.get(i+1), "SAKSBEH", (i+1)));
            }
        }
        else{
            for(int i = 0; i < behandling.soknad.getAntallBarn(); i++){
                barn.add(new OmsorgovertakelseBarn(behandling.soknad.getFodselsdatoer().get(i+1), "SAKSBEH", (i+1)));
            }
        }
    }
    
    
    
    public void setDødsdato(LocalDate dato){
        foreldre.get(0).dodsdato = dato;
    }
    
    public void setVilkårType(Kode vilkarType) {
        this.vilkarType = vilkarType.kode;
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
            id = person.getId();
            dodsdato = person.getDoedsdato();
            erMor = person.getNavBrukerKjonn().kode.equals("K");
            navn = person.getNavn();
            opplysningsKilde = "TPS";
            aktorId = "" + person.getAktoerId();
        }
    }
}
