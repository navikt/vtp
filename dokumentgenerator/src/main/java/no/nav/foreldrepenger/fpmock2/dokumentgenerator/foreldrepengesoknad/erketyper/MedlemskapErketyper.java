package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.integrasjon.felles.ws.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Medlemskap;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.OppholdNorge;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.OppholdUtlandet;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Periode;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Land;

import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;

public class MedlemskapErketyper {
    public static Medlemskap medlemskapNorge(){
        Medlemskap medlemskap = new Medlemskap();
        medlemskap.setBoddINorgeSiste12Mnd(true);
        medlemskap.setBorINorgeNeste12Mnd(true);
        medlemskap.setINorgeVedFoedselstidspunkt(true);
        medlemskap.getOppholdNorge().add(oppholdNorge());
        return medlemskap;
    }

    public static OppholdNorge oppholdNorge(){
        OppholdNorge oppholdNorge = new OppholdNorge();
        Periode periode = new Periode();
        try {
            periode.setFom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().minusYears(2)));
            periode.setTom(DateUtil.convertToXMLGregorianCalendar(LocalDate.now().plusYears(2)));

        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        oppholdNorge.setPeriode(periode);
        return oppholdNorge;
    }

    public static Medlemskap medlemskapUtlandetForrige12mnd(){
        Medlemskap medlemskap = new Medlemskap();
        medlemskap.setINorgeVedFoedselstidspunkt(true);
        medlemskap.setBorINorgeNeste12Mnd(true);
        medlemskap.setBoddINorgeSiste12Mnd(false);
        medlemskap.getOppholdUtlandet().add(oppholdUtlandet());
        return medlemskap;
    }

    public static OppholdUtlandet oppholdUtlandet(){
        OppholdUtlandet oppholdUtlandet = new OppholdUtlandet();
        Land land = new Land();
        land.setKode("AFG");
        land.setKodeverk("LANDKODER");
        oppholdUtlandet.setLand(land);
        return oppholdUtlandet;
    }


}
