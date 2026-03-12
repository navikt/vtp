package no.nav.pdl.hentGeografiskTilknytning;

import no.nav.pdl.mapper.GeografiskTilknytningMapper;
import no.nav.vtp.PersonRepository;

public class HentGeografiskTilknytningCoordinatorFunction {

    private HentGeografiskTilknytningCoordinatorFunction() {
    }

    public static HentGeografiskTilknytningCoordinator opprettCoordinator(PersonRepository personRepository) {
        return ident -> {
            var person = personRepository.hentPerson(ident);
            return GeografiskTilknytningMapper.tilGeografiskTilknytning(person);
        };
    }
}
