package no.nav.pdl.hentperson;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.oversetter.FamilierelasjonBygger;
import no.nav.pdl.oversetter.PersonAdapter;

public class HentPersonCoordinatorFunction {

    public static HentPersonCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo) {
        return (ident, historikk) -> {
            try {
                var personModell = (PersonModell) scenarioRepo.getPersonIndeks().finnByIdent(ident);
                var personPdl = PersonAdapter.oversettPerson(personModell, historikk);

                var personopplysningerModell = scenarioRepo.getPersonIndeks().finnPersonopplysningerByIdent(ident);
                var aktørIdent = personModell.getAktørIdent();

                return FamilierelasjonBygger.byggFamilierelasjoner(aktørIdent, personopplysningerModell, personPdl);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }
}
