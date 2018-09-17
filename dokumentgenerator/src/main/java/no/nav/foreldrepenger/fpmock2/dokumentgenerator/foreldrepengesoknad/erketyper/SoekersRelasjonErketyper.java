package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.integrasjon.felles.ws.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Foedsel;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.SoekersRelasjonTilBarnet;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Termin;

import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;

public class SoekersRelasjonErketyper {
    public static Foedsel søkerFødselFørFødsel(){
        return fødsel(1, LocalDate.now().plusMonths(1));
    }

    public static Foedsel SøkerFøldselEtterFødsel(){
        return fødsel(1, LocalDate.now().minusMonths(1));
    }


    private static Foedsel fødsel(int antall, LocalDate foedselsDato){
        Foedsel soekersRelasjonTilBarnet = new Foedsel();
        soekersRelasjonTilBarnet.setAntallBarn(antall);
        try {
            soekersRelasjonTilBarnet.setFoedselsdato(DateUtil.convertToXMLGregorianCalendar(foedselsDato));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return soekersRelasjonTilBarnet;
    }

    public static Termin søkerTerminFørTermin() {
        return termin(1,true);
    }

    public static Termin søkerTerminEtterTermin() {
        return termin(1, false);
    }


    private static Termin termin(int antall, boolean førTermin){
        Termin termin = new Termin();
        termin.setAntallBarn(antall);
        try {
            if (førTermin) {
                termin.setTermindato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().plusMonths(1)));
                termin.setUtstedtdato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusMonths(1)));
            } else {
                termin.setTermindato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusMonths(1)));
                termin.setUtstedtdato(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusMonths(2)));
            }

        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return termin;
    }



}
