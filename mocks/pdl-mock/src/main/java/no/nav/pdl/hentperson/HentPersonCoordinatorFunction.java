package no.nav.pdl.hentperson;

import no.nav.pdl.mapper.PersonMapper;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.personopplysninger.Familierelasjon;

public class HentPersonCoordinatorFunction {

    private HentPersonCoordinatorFunction() {
    }

    public static HentPersonCoordinator opprettCoordinator() {
        return (ident, historikk) -> {
            var personen = PersonRepository.hentPerson(ident);
            if (personen == null) {
                return null;
            }
            var barneneTilPersonen = personen.personopplysninger().familierelasjoner().stream()
                    .filter(barnerelasjon -> Familierelasjon.Relasjon.BARN.equals(barnerelasjon.relasjon()))
                    .map(barnerelasjon -> PersonRepository.hentPerson(barnerelasjon.relatertTilId().fnr()))
                    .toList();
            return PersonMapper.tilPerson(personen, barneneTilPersonen, historikk);
        };
    }
}
