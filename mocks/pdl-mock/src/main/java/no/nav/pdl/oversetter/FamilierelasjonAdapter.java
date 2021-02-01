package no.nav.pdl.oversetter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.pdl.Familierelasjon;
import no.nav.pdl.Familierelasjonsrolle;

public class FamilierelasjonAdapter {

    static List<Familierelasjon> tilFamilerelasjon(Collection<FamilierelasjonModell> relasjoner, Familierelasjonsrolle minRolle){

        List<Familierelasjon> resultat = new ArrayList<>();
        for(FamilierelasjonModell rel: relasjoner) {

            var rolleTilRelasjon = rel.getRolle();
            if (rolleTilRelasjon.equals(FamilierelasjonModell.Rolle.EKTE)) {
                continue; // Familierelasjon til ektefelle skal ikke registreres.
            }

            Familierelasjon familierelasjon = new Familierelasjon();
            if (minRolle.equals(Familierelasjonsrolle.BARN)) {
                switch (rolleTilRelasjon) {
                    case FARA:
                        familierelasjon.setRelatertPersonsRolle(Familierelasjonsrolle.FAR);
                        break;
                    case MORA:
                        familierelasjon.setRelatertPersonsRolle(Familierelasjonsrolle.MOR);
                        break;
                    case MMOR:
                        familierelasjon.setRelatertPersonsRolle(Familierelasjonsrolle.MEDMOR);
                        break;
                    default:
                        continue; // Skal ikke registrere relasjon til søsken (tvillinger, trillinger, ...)
                }
                familierelasjon.setRelatertPersonsIdent(rel.getTil().getIdent());
                familierelasjon.setMinRolleForPerson(minRolle);
                resultat.add(familierelasjon);
            } else {
                if (rolleTilRelasjon.equals(FamilierelasjonModell.Rolle.BARN)) {
                    familierelasjon.setRelatertPersonsIdent(rel.getTil().getIdent());
                    familierelasjon.setRelatertPersonsRolle(Familierelasjonsrolle.BARN);
                    familierelasjon.setMinRolleForPerson(minRolle);
                    resultat.add(familierelasjon);
                }
            }

        }
        return resultat;
    }
}
