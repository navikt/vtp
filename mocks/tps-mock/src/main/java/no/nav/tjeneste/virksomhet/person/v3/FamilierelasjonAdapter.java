package no.nav.tjeneste.virksomhet.person.v3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjon;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjoner;

public class FamilierelasjonAdapter {

    public List<Familierelasjon> tilFamilerelasjon(Collection<FamilierelasjonModell> relasjoner){
        
        List<Familierelasjon> resultat = new ArrayList<>();
        for(FamilierelasjonModell rel: relasjoner) {
            Familierelasjon familierelasjon = new Familierelasjon();
            familierelasjon.setHarSammeBosted(true);
            Familierelasjoner familierelasjoner = new Familierelasjoner();
            familierelasjoner.setValue(rel.getRolleKode());
            familierelasjon.setTilRolle(familierelasjoner);
            
            Bruker tilBruker = new PersonAdapter().fra(rel.getTil());
            familierelasjon.setTilPerson(tilBruker);
            resultat.add(familierelasjon);
            
        }
        return resultat;
    }
}
