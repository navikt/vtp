package no.nav.pdl.hentGeografiskTilknytning;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.mapper.GeografiskTilknytningMapper;
import no.nav.pdl.oversetter.GeografiskTilknytningAdapter;
import no.nav.vtp.Person;
import no.nav.vtp.PersonRepository;

public class HentGeografiskTilknytningCoordinatorFunction {

    private HentGeografiskTilknytningCoordinatorFunction() {
    }

    public static HentGeografiskTilknytningCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo, PersonRepository personRepository) {
        return ident -> {
            PersonModell person;
            Person nyPerson;
            try {
                person = scenarioRepo.getPersonIndeks().finnByIdent(ident);
                nyPerson = personRepository.hentPerson(ident);
            } catch (IllegalArgumentException e) {
                return null;
            }

            var geografiskTilknytningGammel = GeografiskTilknytningAdapter.tilGeografiskTilknytning(person);
            var geografiskTilknytningNy = GeografiskTilknytningMapper.tilGeografiskTilknytning(nyPerson);

            //todo: sammenlign

            return geografiskTilknytningGammel;
        };
    }
}
