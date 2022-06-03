package no.nav.pdl.oversetter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.pdl.ForelderBarnRelasjon;
import no.nav.pdl.ForelderBarnRelasjonRolle;

public class ForelderBarnRelasjonAdapter {

    static List<ForelderBarnRelasjon> tilForelderBarnRelasjon(Collection<FamilierelasjonModell> relasjoner, ForelderBarnRelasjonRolle minRolle){

        List<ForelderBarnRelasjon> resultat = new ArrayList<>();
        for(FamilierelasjonModell rel: relasjoner) {

            var rolleTilRelasjon = rel.getRolle();
            if (rolleTilRelasjon.equals(FamilierelasjonModell.Rolle.EKTE)) {
                continue; // Familierelasjon til ektefelle skal ikke registreres.
            }

            var forelderBarnRelasjon = new ForelderBarnRelasjon();
            if (minRolle.equals(ForelderBarnRelasjonRolle.BARN)) {
                switch (rolleTilRelasjon) {
                    case FARA:
                        forelderBarnRelasjon.setRelatertPersonsRolle(ForelderBarnRelasjonRolle.FAR);
                        break;
                    case MORA:
                        forelderBarnRelasjon.setRelatertPersonsRolle(ForelderBarnRelasjonRolle.MOR);
                        break;
                    case MMOR:
                        forelderBarnRelasjon.setRelatertPersonsRolle(ForelderBarnRelasjonRolle.MEDMOR);
                        break;
                    default:
                        continue; // Skal ikke registrere relasjon til søsken (tvillinger, trillinger, ...)
                }
                forelderBarnRelasjon.setRelatertPersonsIdent(rel.getTil().getIdent());
                forelderBarnRelasjon.setMinRolleForPerson(minRolle);
                resultat.add(forelderBarnRelasjon);
            } else {
                if (rolleTilRelasjon.equals(FamilierelasjonModell.Rolle.BARN)) {
                    forelderBarnRelasjon.setRelatertPersonsIdent(rel.getTil().getIdent());
                    forelderBarnRelasjon.setRelatertPersonsRolle(ForelderBarnRelasjonRolle.BARN);
                    forelderBarnRelasjon.setMinRolleForPerson(minRolle);
                    resultat.add(forelderBarnRelasjon);
                }
            }

        }
        return resultat;
    }
}
