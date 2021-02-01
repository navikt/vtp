package no.nav.pdl.oversetter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pdl.DoedfoedtBarn;
import no.nav.pdl.Person;

public class DoedfoedtBarnAdapter {

    private static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int PERSONNR_LENGDE = 5;

    public static List<DoedfoedtBarn> tilDoedfoedtBarn(String aktørIdent, Personopplysninger personopplysningerModell, Person person) {
        var erBarnet = erIdentenBarnet(aktørIdent, personopplysningerModell);
        return leggTilDoedfoedtBarnForRelasjonerMedFDATIdent(personopplysningerModell, person, erBarnet);
    }

    private static boolean erIdentenBarnet(String aktørIdent, Personopplysninger personopplysningerModell) {
        for (FamilierelasjonModell relasjon : personopplysningerModell.getFamilierelasjoner()) {
            if (relasjon.getRolle().equals(FamilierelasjonModell.Rolle.BARN) && relasjon.getTil().getAktørIdent().equals(aktørIdent)) {
                return true;
            }
        }
        return false;
    }

    private static List<DoedfoedtBarn> leggTilDoedfoedtBarnForRelasjonerMedFDATIdent(Personopplysninger personopplysningerModell,
                                                                                     Person person,
                                                                                     boolean erBarnet) {
        List<DoedfoedtBarn> doedfoedtBarnList = new ArrayList<>();
        if (!erBarnet) {
            for (FamilierelasjonModell relasjon : personopplysningerModell.getFamilierelasjoner()) {
                if (relasjon.getRolle().equals(FamilierelasjonModell.Rolle.BARN)) {
                    var doedfoedtBarn = new DoedfoedtBarn();
                    var barnModell = (BarnModell) relasjon.getTil();
                    var fødselsdato = barnModell.getFødselsdato();
                    if (fødselsdato != null && erFdatNummer(barnModell.getIdent())) {
                        doedfoedtBarn.setDato(fødselsdato.format(DATO_FORMATTERER));
                    }
                    doedfoedtBarnList.add(doedfoedtBarn);
                }
            }
        }
        return doedfoedtBarnList;
    }

    private static String getPersonnummer(String str) {
        return (str == null || str.length() < PERSONNR_LENGDE)
                ? null
                : str.substring(str.length() - PERSONNR_LENGDE);
    }

    private static boolean isFdatNummer(String personnummer) {
        return personnummer != null && personnummer.length() == PERSONNR_LENGDE && personnummer.startsWith("0000");
    }

    private static boolean erFdatNummer(String ident) {
        return isFdatNummer(getPersonnummer(ident));
    }
}
