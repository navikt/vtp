package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.SøkersRolle;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Bruker;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Brukerroller;
@Deprecated
public class SoekerErketyper {

    public static Bruker morSoeker(String aktoerId){
        return soekerAvType(aktoerId, SøkersRolle.MOR);
    }
    public static Bruker medSoeker(String aktoerId){
        return soekerAvType(aktoerId, SøkersRolle.FAR);
    }
    public static Bruker farSoeker(String aktoerId){
        return soekerAvType(aktoerId, SøkersRolle.MEDMOR);
    }
    public static Bruker annenOmsorgspersonSoeker(String aktoerId){
        return soekerAvType(aktoerId, SøkersRolle.ANDRE);
    }

    public static Bruker soekerAvType(String aktoerId, SøkersRolle søkersRolle) {
        Bruker bruker = new Bruker();
        bruker.setAktoerId(aktoerId);
        Brukerroller brukerroller = new Brukerroller();
        if (søkersRolle ==  SøkersRolle.MOR) {
            brukerroller.setKode("MOR");
        }
        else if (søkersRolle ==  SøkersRolle.FAR) {
            brukerroller.setKode("FAR");
        }
        else if (søkersRolle == SøkersRolle.MEDMOR) {
            brukerroller.setKode("MEDMOR");
        }
        else {
            brukerroller.setKode("ANDRE");
        }
        brukerroller.setKodeverk("FORELDRE_TYPE"); //todo identifiser korrekt kodeverk
        bruker.setSoeknadsrolle(brukerroller);
        return bruker;
    }
}
