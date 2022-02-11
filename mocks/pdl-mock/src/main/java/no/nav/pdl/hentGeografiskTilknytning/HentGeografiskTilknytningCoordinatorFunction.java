package no.nav.pdl.hentGeografiskTilknytning;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.oversetter.GeografiskTilknytningAdapter;

public class HentGeografiskTilknytningCoordinatorFunction {

    private HentGeografiskTilknytningCoordinatorFunction() {
    }

    public static HentGeografiskTilknytningCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo) {
        return ident -> {
            PersonModell person;
            try {
                person = scenarioRepo.getPersonIndeks().finnByIdent(ident);
            } catch (IllegalArgumentException e) {
                return null;
            }
            return GeografiskTilknytningAdapter.tilGeografiskTilknytning(person);
        };
    }
}
