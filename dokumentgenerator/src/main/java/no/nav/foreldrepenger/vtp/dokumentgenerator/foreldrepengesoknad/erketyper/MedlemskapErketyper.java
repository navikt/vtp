package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Medlemskap;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.OppholdNorge;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.OppholdUtlandet;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Periode;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Land;

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
        periode.setFom(fom);
        periode.setTom(tom);

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
