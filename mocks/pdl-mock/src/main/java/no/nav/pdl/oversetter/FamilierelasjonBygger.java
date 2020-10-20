package no.nav.pdl.oversetter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pdl.DoedfoedtBarn;
import no.nav.pdl.Familierelasjon;
import no.nav.pdl.Familierelasjonsrolle;
import no.nav.pdl.Person;

public class FamilierelasjonBygger {

    private static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int PERSONNR_LENGDE = 5;

    public static Person byggFamilierelasjoner(String aktørIdent, Personopplysninger personopplysningerModell, Person person) {
        person.setFamilierelasjoner(new ArrayList<>()); // init array

        boolean erBarnet = erIdentenBarnet(aktørIdent, personopplysningerModell);
        leggTilDoedfoedtBarnForRelasjonerMedFDATIdent(personopplysningerModell, person, erBarnet);

        List<Familierelasjon> familierelasjoner;
        if(erBarnet) { //TODO HACK Familierelasjon for barnet
            familierelasjoner = FamilierelasjonAdapter.tilFamilerelasjon(personopplysningerModell.getFamilierelasjonerForBarnet());
            familierelasjoner.forEach(fr -> person.getFamilierelasjoner().add(fr));
        } else if(personopplysningerModell.getAnnenPart() != null && personopplysningerModell.getAnnenPart().getAktørIdent().equals(aktørIdent)) { //TODO HACK for annenpart (annenpart burde ha en egen personopplysning fil eller liknende)
            familierelasjoner = FamilierelasjonAdapter.tilFamilerelasjon(personopplysningerModell.getFamilierelasjonerForAnnenPart());
            leggTilFamilierelasjonHvisRelasjonenIkkeErDødfødtBarn(person, familierelasjoner);
        }
        else {
            familierelasjoner = FamilierelasjonAdapter.tilFamilerelasjon(personopplysningerModell.getFamilierelasjoner());
            leggTilFamilierelasjonHvisRelasjonenIkkeErDødfødtBarn(person, familierelasjoner);
        }

        for (Familierelasjon familierelasjon : familierelasjoner) {
            System.out.println("    " + familierelasjon.getRelatertPersonsIdent());
        }
        return person;
    }

    private static void leggTilFamilierelasjonHvisRelasjonenIkkeErDødfødtBarn(Person person, List<Familierelasjon> familierelasjoner) {
        familierelasjoner.stream()
                .filter(familierelasjon -> !(familierelasjon.getRelatertPersonsRolle().equals(Familierelasjonsrolle.BARN) && erFdatNummer(familierelasjon.getRelatertPersonsIdent())))
                .forEach(familierelasjon -> person.getFamilierelasjoner().add(familierelasjon));
    }

    private static boolean erIdentenBarnet(String aktørIdent, Personopplysninger personopplysningerModell) {
        for (FamilierelasjonModell relasjon : personopplysningerModell.getFamilierelasjoner()) {
            if (relasjon.getRolle().equals(FamilierelasjonModell.Rolle.BARN) && relasjon.getTil().getAktørIdent().equals(aktørIdent)) {
                return true;
            }
        }
        return false;
    }

    private static void leggTilDoedfoedtBarnForRelasjonerMedFDATIdent(Personopplysninger personopplysningerModell, Person person, boolean erBarnet) {
        List<DoedfoedtBarn> doedfoedtBarnList = new ArrayList<>();
        if (!erBarnet) {
            for (FamilierelasjonModell relasjon : personopplysningerModell.getFamilierelasjoner()) {
                if (relasjon.getRolle().equals(FamilierelasjonModell.Rolle.BARN)) {
                    DoedfoedtBarn doedfoedtBarn = new DoedfoedtBarn();
                    BarnModell barnModell = (BarnModell) relasjon.getTil();
                    LocalDate fødselsdato = barnModell.getFødselsdato();
                    if (barnModell.getFødselsdato() != null && erFdatNummer(barnModell.getIdent())) {
                        doedfoedtBarn.setDato(fødselsdato.format(DATO_FORMATTERER));
                    }
                    doedfoedtBarnList.add(doedfoedtBarn);
                }
            }
        }
        person.setDoedfoedtBarn(doedfoedtBarnList);
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
