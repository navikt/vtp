package no.nav.pdl.hentperson;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.oversetter.PersonAdapter;

public class HentPersonCoordinatorFunction {

    private HentPersonCoordinatorFunction() {
    }

    public static HentPersonCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo) {
        return (ident, historikk) -> {
            PersonModell person;
            try {
                person = scenarioRepo.getPersonIndeks().finnByIdent(ident);
            } catch (IllegalArgumentException e) {
                return null;
            }
            var personopplysningerModell = scenarioRepo.getPersonIndeks().finnPersonopplysningerByIdent(ident);
            return PersonAdapter.tilPerson(person, personopplysningerModell, historikk);
        };
    }
}
