package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Adopsjon;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Foedsel;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Omsorgsovertakelse;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Termin;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Omsorgsovertakelseaarsaker;

public class SoekersRelasjonErketyper {
    public static Foedsel søkerFødselFørFødsel(){
        return fødsel(1, LocalDate.now().plusDays(14));
    }

    public static Foedsel søkerFødselEtterFødsel(){
        return fødsel(1, LocalDate.now().minusMonths(1));
    }
    
    public static Foedsel søkerFødselEtterFødselFlereBarn() {
        return fødsel(2, LocalDate.now().minusMonths(1));
    }
    
    public static Foedsel søkerFødselEtterSøknadsfrist() {
        return fødsel(1, LocalDate.now().minusMonths(20));
    }


    public static Foedsel fødsel(int antall, LocalDate fødselsdato){
        Foedsel soekersRelasjonTilBarnet = new Foedsel();
        soekersRelasjonTilBarnet.setAntallBarn(antall);
        soekersRelasjonTilBarnet.setFoedselsdato(DateUtil.convertToXMLGregorianCalendar(fødselsdato));

        return soekersRelasjonTilBarnet;
    }
    
    public static Termin søkerTermin(LocalDate termindato) {
        return termin(1, termindato);
    }

    public static Termin søkerTerminFørTermin() {
        return termin(1, LocalDate.now().plusWeeks(3));
    }

    public static Termin søkerTerminEtterTermin() {
        return termin(1, LocalDate.now().minusWeeks(3));
    }

    private static Termin termin(int antall, LocalDate termindato){
        Termin termin = new Termin();
        termin.setAntallBarn(antall);
        termin.setTermindato(DateUtil.convertToXMLGregorianCalendar(termindato));
        termin.setUtstedtdato(DateUtil.convertToXMLGregorianCalendar(termindato.minusMonths(1)));
        return termin;
    }


    public static Adopsjon søkerAdopsjon(){
        return adopsjon(false);
    }

    public static Adopsjon søkerAdopsjonAvEktefellesBarn(){
        return adopsjon(true);
    }

    private static Adopsjon adopsjon(boolean ektefellesBarn){
        Adopsjon adopsjon = new Adopsjon();
        adopsjon.setAntallBarn(1);
        adopsjon.setAdopsjonAvEktefellesBarn(ektefellesBarn);
        adopsjon.getFoedselsdato().add(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusYears(10)));
        adopsjon.setAnkomstdato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().plusMonths(1)));
        adopsjon.setOmsorgsovertakelsesdato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().plusMonths(1)));

        return adopsjon;
    }

    public static Omsorgsovertakelse søkerOmsorgsovertakelseGrunnetDød(){
        return omsorgsovertakelse("ANDRE_FORELDER_DØD");

        //todo implementer OVERTATT_OMSORG, OVERTATT_OMSORG_F, ADOPTERER ALENE, men avklar først hva de betyr funksjonelt
    }

    private static Omsorgsovertakelse omsorgsovertakelse(String aarsak){
        Omsorgsovertakelse omsorgsovertakelse = new Omsorgsovertakelse();
        omsorgsovertakelse.setAntallBarn(1);
        Omsorgsovertakelseaarsaker omsorgsovertakelseaarsaker = new Omsorgsovertakelseaarsaker();
        omsorgsovertakelseaarsaker.setKode(aarsak);
        omsorgsovertakelseaarsaker.setKodeverk("FAR_SOEKER_TYPE");
        omsorgsovertakelse.setOmsorgsovertakelseaarsak(omsorgsovertakelseaarsaker);
        omsorgsovertakelse.setOmsorgsovertakelsesdato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().plusMonths(1)));
        omsorgsovertakelse.getFoedselsdato().add(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusMonths(6)));

        return omsorgsovertakelse;
    }
}
