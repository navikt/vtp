package no.nav.pdl.oversetter;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pdl.ForelderBarnRelasjon;
import no.nav.pdl.ForelderBarnRelasjonRolle;
import no.nav.pdl.Person;

public class ForelderBarnRelasjonBygger {

    private static final int PERSONNR_LENGDE = 5;

    private ForelderBarnRelasjonBygger() {
    }

    public static Person byggForelderBarnRelasjoner(String aktørIdent, Personopplysninger personopplysningerModell, Person person) {
        person.setForelderBarnRelasjon(new ArrayList<>()); // init array
        person.setForelderBarnRelasjon(new ArrayList<>()); // init array

        var erBarnet = erIdentenBarnet(aktørIdent, personopplysningerModell);

        List<ForelderBarnRelasjon> forelderBarnRelasjoner;
        if(erBarnet) { //TODO HACK Familierelasjon for barnet
            // BARN
            forelderBarnRelasjoner = ForelderBarnRelasjonAdapter.tilForelderBarnRelasjon(personopplysningerModell.getFamilierelasjonerForBarnet(), ForelderBarnRelasjonRolle.BARN);
            forelderBarnRelasjoner.forEach(fr -> person.getForelderBarnRelasjon().add(fr));
        } else if(personopplysningerModell.getAnnenPart() != null && personopplysningerModell.getAnnenPart().getAktørIdent().equals(aktørIdent)) { //TODO HACK for annenpart (annenpart burde ha en egen personopplysning fil eller liknende)
            // ANNENPART
            var minRolleTilBarn = hentAnnenpartsRolleTilEventuelleBarn(personopplysningerModell);
            forelderBarnRelasjoner = ForelderBarnRelasjonAdapter.tilForelderBarnRelasjon(personopplysningerModell.getFamilierelasjonerForAnnenPart(), minRolleTilBarn);
            leggTilForelderBarnRelasjonHvisRelasjonenIkkeErDødfødtBarn(person, forelderBarnRelasjoner);
        }
        else {
            // SØKER
            var minRolleTilBarn = hentSøkersRolleTilEventuelleBarn(personopplysningerModell);
            forelderBarnRelasjoner = ForelderBarnRelasjonAdapter.tilForelderBarnRelasjon(personopplysningerModell.getFamilierelasjoner(), minRolleTilBarn);
            leggTilForelderBarnRelasjonHvisRelasjonenIkkeErDødfødtBarn(person, forelderBarnRelasjoner);
        }

        return person;
    }

    private static ForelderBarnRelasjonRolle hentSøkersRolleTilEventuelleBarn(Personopplysninger personopplysningerModell) {
        return personopplysningerModell.getSøker().getKjønn().equals(BrukerModell.Kjønn.K) ?
            ForelderBarnRelasjonRolle.MOR : ForelderBarnRelasjonRolle.FAR;
    }

    private static ForelderBarnRelasjonRolle hentAnnenpartsRolleTilEventuelleBarn(Personopplysninger personopplysningerModell) {
        ForelderBarnRelasjonRolle minRolleTilBarn;
        if (personopplysningerModell.getSøker().getKjønn().equals(BrukerModell.Kjønn.K)) {
            if (personopplysningerModell.getAnnenPart().getKjønn().equals(BrukerModell.Kjønn.K)) {
                minRolleTilBarn = ForelderBarnRelasjonRolle.MEDMOR;
            } else {
                minRolleTilBarn = ForelderBarnRelasjonRolle.FAR;
            }
        } else {
            minRolleTilBarn = ForelderBarnRelasjonRolle.MOR;
        }
        return minRolleTilBarn;
    }

    private static void leggTilForelderBarnRelasjonHvisRelasjonenIkkeErDødfødtBarn(Person person, List<ForelderBarnRelasjon> forelderBarnRelasjoner) {
        forelderBarnRelasjoner.stream()
                .filter(fbr -> !(fbr.getRelatertPersonsRolle().equals(ForelderBarnRelasjonRolle.BARN) && erFdatNummer(fbr.getRelatertPersonsIdent())))
                .forEach(fbr -> person.getForelderBarnRelasjon().add(fbr));
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
