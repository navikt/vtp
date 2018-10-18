package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Medlemskap;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.OppholdNorge;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.OppholdUtlandet;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Periode;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Land;

public class MedlemskapErketyper {
    public static Medlemskap medlemskapNorge(){
        Medlemskap medlemskap = new Medlemskap();
        medlemskap.setBoddINorgeSiste12Mnd(true);
        medlemskap.setBorINorgeNeste12Mnd(true);
        medlemskap.setINorgeVedFoedselstidspunkt(true);
        medlemskap.getOppholdNorge().add(oppholdNorge(LocalDate.now().minusYears(2), LocalDate.now()));
        medlemskap.getOppholdNorge().add(oppholdNorge(LocalDate.now(), LocalDate.now().plusYears(2)));
        return medlemskap;
    }

    public static OppholdNorge oppholdNorge(LocalDate fom, LocalDate tom){
        OppholdNorge oppholdNorge = new OppholdNorge();
        Periode periode = new Periode();
        try {
            periode.setFom(DateUtil.convertToXMLGregorianCalendar(fom));
            periode.setTom(DateUtil.convertToXMLGregorianCalendar(tom));

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
