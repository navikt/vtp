package no.nav.pdl.oversetter;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pdl.Familierelasjon;
import no.nav.pdl.Familierelasjonsrolle;
import no.nav.pdl.ForelderBarnRelasjon;
import no.nav.pdl.ForelderBarnRelasjonRolle;
import no.nav.pdl.Person;

public class FamilierelasjonBygger {

    private static final int PERSONNR_LENGDE = 5;

    public static Person byggFamilierelasjoner(String aktørIdent, Personopplysninger personopplysningerModell, Person person) {
        person.setFamilierelasjoner(new ArrayList<>()); // init array
        person.setForelderBarnRelasjon(new ArrayList<>()); // init array

        var erBarnet = erIdentenBarnet(aktørIdent, personopplysningerModell);

        List<Familierelasjon> familierelasjoner;
        if(erBarnet) { //TODO HACK Familierelasjon for barnet
            // BARN
            familierelasjoner = FamilierelasjonAdapter.tilFamilerelasjon(personopplysningerModell.getFamilierelasjonerForBarnet(), Familierelasjonsrolle.BARN);
            familierelasjoner.forEach(fr -> person.getFamilierelasjoner().add(fr));
        } else if(personopplysningerModell.getAnnenPart() != null && personopplysningerModell.getAnnenPart().getAktørIdent().equals(aktørIdent)) { //TODO HACK for annenpart (annenpart burde ha en egen personopplysning fil eller liknende)
            // ANNENPART
            var minRolleTilBarn = hentAnnenpartsRolleTilEventuelleBarn(personopplysningerModell);
            familierelasjoner = FamilierelasjonAdapter.tilFamilerelasjon(personopplysningerModell.getFamilierelasjonerForAnnenPart(), minRolleTilBarn);
            leggTilFamilierelasjonHvisRelasjonenIkkeErDødfødtBarn(person, familierelasjoner);
        }
        else {
            // SØKER
            var minRolleTilBarn = hentSøkersRolleTilEventuelleBarn(personopplysningerModell);
            familierelasjoner = FamilierelasjonAdapter.tilFamilerelasjon(personopplysningerModell.getFamilierelasjoner(), minRolleTilBarn);
            leggTilFamilierelasjonHvisRelasjonenIkkeErDødfødtBarn(person, familierelasjoner);
        }

        leggTilForelderBarnRelasjoner(person, familierelasjoner);

        for (Familierelasjon familierelasjon : familierelasjoner) {
            System.out.println("    " + familierelasjon.getRelatertPersonsIdent());
        }
        return person;
    }

    private static void leggTilForelderBarnRelasjoner(Person person, List<Familierelasjon> familierelasjoner) {
        familierelasjoner.stream()
                .filter(familierelasjon -> !(familierelasjon.getRelatertPersonsRolle().equals(Familierelasjonsrolle.BARN) && erFdatNummer(familierelasjon.getRelatertPersonsIdent())))
                .map(f -> new ForelderBarnRelasjon(f.getRelatertPersonsIdent(), ForelderBarnRelasjonRolle.valueOf(f.getRelatertPersonsRolle().toString()),
                        ForelderBarnRelasjonRolle.valueOf(f.getMinRolleForPerson().toString()), f.getFolkeregistermetadata(), f.getMetadata()))
                .forEach(forelderBarnRelasjon -> person.getForelderBarnRelasjon().add(forelderBarnRelasjon));
    }


    private static Familierelasjonsrolle hentSøkersRolleTilEventuelleBarn(Personopplysninger personopplysningerModell) {
        return personopplysningerModell.getSøker().getKjønn().equals(BrukerModell.Kjønn.K) ?
                Familierelasjonsrolle.MOR : Familierelasjonsrolle.FAR;
    }

    private static Familierelasjonsrolle hentAnnenpartsRolleTilEventuelleBarn(Personopplysninger personopplysningerModell) {
        Familierelasjonsrolle minRolleTilBarn;
        if (personopplysningerModell.getSøker().getKjønn().equals(BrukerModell.Kjønn.K)) {
            if (personopplysningerModell.getAnnenPart().getKjønn().equals(BrukerModell.Kjønn.K)) {
                minRolleTilBarn = Familierelasjonsrolle.MEDMOR;
            } else {
                minRolleTilBarn = Familierelasjonsrolle.FAR;
            }
        } else {
            minRolleTilBarn = Familierelasjonsrolle.MOR;
        }
        return minRolleTilBarn;
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
