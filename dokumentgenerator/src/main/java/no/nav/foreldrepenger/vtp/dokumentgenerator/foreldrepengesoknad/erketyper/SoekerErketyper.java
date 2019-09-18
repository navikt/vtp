package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Bruker;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Brukerroller;

public class SoekerErketyper {

    public static Bruker morSoeker(String aktoerId){
        return soekerAvType(aktoerId, "MOR");
    }

    public static Bruker medSoeker(String aktoerId){
        return soekerAvType(aktoerId, "MEDMOR");
    }

    public static Bruker farSoeker(String aktoerId){
        return soekerAvType(aktoerId, "FAR");
    }

    public static Bruker annenOmsorgspersonSoeker(String aktoerId){
        return soekerAvType(aktoerId, "ANDRE");
    }

    public static Bruker soekerAvType(String aktoerId, String soekerType) {
        Bruker bruker = new Bruker();
        bruker.setAktoerId(aktoerId);
        Brukerroller brukerroller = new Brukerroller();
        brukerroller.setKode(soekerType);
        brukerroller.setKodeverk("FORELDRE_TYPE"); //todo identifiser korrekt kodeverk
        bruker.setSoeknadsrolle(brukerroller);
        return bruker;
    }
}
