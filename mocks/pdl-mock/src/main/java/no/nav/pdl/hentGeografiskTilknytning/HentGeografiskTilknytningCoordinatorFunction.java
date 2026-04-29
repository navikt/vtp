package no.nav.pdl.hentGeografiskTilknytning;

import no.nav.pdl.mapper.GeografiskTilknytningMapper;
import no.nav.vtp.person.PersonRepository;

public class HentGeografiskTilknytningCoordinatorFunction {

    private HentGeografiskTilknytningCoordinatorFunction() {
    }

    public static HentGeografiskTilknytningCoordinator opprettCoordinator() {
        return ident -> {
            var person = PersonRepository.hentPerson(ident);
            return GeografiskTilknytningMapper.tilGeografiskTilknytning(person);
        };
    }
}
