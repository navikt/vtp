package no.nav.pdl.hentperson;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.mapper.PersonMapper;
import no.nav.pdl.oversetter.PersonAdapter;
import no.nav.vtp.PersonRepository;

import no.nav.vtp.personopplysninger.Familierelasjon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                var personen = personRepository.hentPerson(ident);
                var barneneTilPersonen = personen.personopplysninger().familierelasjoner().stream()
                        .filter(barnerelasjon -> Familierelasjon.Relasjon.BARN.equals(barnerelasjon.relasjon()))
                        .map(barnerelasjon -> personRepository.hentPerson(barnerelasjon.relatertTilId().fnr()))
                        .toList();
                var personNy = PersonMapper.tilPerson(person, barneneTilPersonen);
                if (!personGammel.toString().equals(personNy.toString())) {
                    LOG.warn("Person er forskjellig, gammel: {}, ny: {}", personGammel, personNy);
                }
            } catch (Exception e) {
                LOG.warn("FEIL I MAPPING !!!", e);
            }

            return personGammel;
        };
    }
}
