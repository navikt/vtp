package no.nav.pdl.oversetter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.pdl.Familierelasjon;
import no.nav.pdl.Familierelasjonsrolle;

public class FamilierelasjonAdapter {

    static List<Familierelasjon> tilFamilerelasjon(Collection<FamilierelasjonModell> relasjoner){

        List<Familierelasjon> resultat = new ArrayList<>();
        for(FamilierelasjonModell rel: relasjoner) {
            // TODO: Er Rolle.EKTE utfaset?
            if (rel.getRolle().equals(FamilierelasjonModell.Rolle.EKTE)) {
                continue;
            }
            Familierelasjon familierelasjon = new Familierelasjon();
            familierelasjon.setRelatertPersonsIdent(rel.getTil().getIdent());

            FamilierelasjonModell.Rolle rolle = rel.getRolle();
            switch (rolle) {
                case FARA:
                    familierelasjon.setRelatertPersonsRolle(Familierelasjonsrolle.FAR);
                    break;
                case MORA:
                    familierelasjon.setRelatertPersonsRolle(Familierelasjonsrolle.MOR);
                    break;
                case MMOR:
                    familierelasjon.setRelatertPersonsRolle(Familierelasjonsrolle.MEDMOR);
                    break;
                case BARN:
                    familierelasjon.setRelatertPersonsRolle(Familierelasjonsrolle.BARN);
            }

            // TODO: Vurdere om vi skal sette familierelasjon#setMinRolleForPerson
            // Testmodellen legger ikke dette riktig inn i dag. Usikkert om det brukes i k9.

            resultat.add(familierelasjon);

        }
        return resultat;
    }
}
