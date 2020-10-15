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
import no.nav.pdl.Person;

public class FamilieRelasjonBygger {

    static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Person byggFamilierelasjoner(String aktørIdent, Personopplysninger personopplysningerModell, Person person) {
        person.setFamilierelasjoner(new ArrayList<>()); // init array

        boolean erBarnet = false;
        List<DoedfoedtBarn> doedfoedtBarnList = new ArrayList<>();
        for (FamilierelasjonModell relasjon : personopplysningerModell.getFamilierelasjoner()) {
            if(relasjon.getRolle().equals(FamilierelasjonModell.Rolle.BARN) && relasjon.getTil().getAktørIdent().equals(aktørIdent)) {
                erBarnet = true;
            } else if (relasjon.getRolle().equals(FamilierelasjonModell.Rolle.BARN)) {
                DoedfoedtBarn doedfoedtBarn = new DoedfoedtBarn();
                BarnModell barnModell = (BarnModell) relasjon.getTil();
                LocalDate fødselsdato = barnModell.getFødselsdato();
                if (barnModell.getFødselsdato() != null && barnModell.getIdent().substring(6,10).equalsIgnoreCase("0000")) {
                    doedfoedtBarn.setDato(fødselsdato.format(DATO_FORMATTERER));
                }
                doedfoedtBarnList.add(doedfoedtBarn);
            }
            person.setDoedfoedtBarn(doedfoedtBarnList);
        }

        List<Familierelasjon> familierelasjoner;
        if(personopplysningerModell.getAnnenPart() != null && personopplysningerModell.getAnnenPart().getAktørIdent().equals(aktørIdent)) { //TODO HACK for annenpart (annenpart burde ha en egen personopplysning fil eller liknende)
            familierelasjoner = FamilierelasjonAdapter.tilFamilerelasjon(personopplysningerModell.getFamilierelasjonerForAnnenPart());
            familierelasjoner.forEach(fr -> person.getFamilierelasjoner().add(fr));
        }
        else if(erBarnet) { //TODO HACK Familierelasjon for barnet
            familierelasjoner = FamilierelasjonAdapter.tilFamilerelasjon(personopplysningerModell.getFamilierelasjonerForBarnet());
            familierelasjoner.forEach(fr -> person.getFamilierelasjoner().add(fr));
        }
        else {
            familierelasjoner = FamilierelasjonAdapter.tilFamilerelasjon(personopplysningerModell.getFamilierelasjoner());
            familierelasjoner.forEach(fr -> person.getFamilierelasjoner().add(fr));
        }

        for (Familierelasjon familierelasjon : familierelasjoner) {
            System.out.println("    " + familierelasjon.getRelatertPersonsIdent());
        }
        return person;
    }
}
