package no.nav.pdl.hentperson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.mapper.PersonMapper;
import no.nav.pdl.oversetter.PersonAdapter;
import no.nav.vtp.PersonRepository;
import no.nav.vtp.personopplysninger.Familierelasjon;
import no.nav.vtp.personopplysninger.Navn;

public class HentPersonCoordinatorFunction {

    private static final Logger LOG = LoggerFactory.getLogger(HentPersonCoordinatorFunction.class);

    private HentPersonCoordinatorFunction() {
    }

    public static HentPersonCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo,
                                                           PersonRepository personRepository) {
        return (ident, historikk) -> {
            PersonModell person;
            try {
                person = scenarioRepo.getPersonIndeks().finnByIdent(ident);
            } catch (IllegalArgumentException e) {
                return null;
            }
            var personopplysningerModell = scenarioRepo.getPersonIndeks().finnPersonopplysningerByIdent(ident);
            var personGammel = PersonAdapter.tilPerson(person, personopplysningerModell, historikk);

            try {
                var navn = new Navn(person.getFornavn(), null, person.getEtternavn());
                var personen = personRepository.hentPerson(ident);
                var barneneTilPersonen = personen.personopplysninger().familierelasjoner().stream()
                        .filter(barnerelasjon -> Familierelasjon.Relasjon.BARN.equals(barnerelasjon.relasjon()))
                        .map(barnerelasjon -> personRepository.hentPerson(barnerelasjon.relatertTilId().fnr()))
                        .toList();
                var personNy = PersonMapper.tilPerson(personen, navn, barneneTilPersonen);
                if (!personGammel.toString().equals(personNy.toString())) {
                    LOG.warn("Person er forskjellig, gammel: {}, ny: {}", personGammel, personNy);
                } else {
                    LOG.info("PEROSN ER LIK");
                }
            } catch (Exception e) {
                LOG.warn("FEIL I MAPPING !!!", e);
            }

            return personGammel;
        };
    }
}
