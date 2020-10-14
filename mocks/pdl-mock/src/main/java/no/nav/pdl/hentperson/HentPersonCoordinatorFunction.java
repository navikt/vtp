package no.nav.pdl.hentperson;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.oversetter.FamilieRelasjonBygger;
import no.nav.pdl.oversetter.PersonOversetter;

public class HentPersonCoordinatorFunction {

    public static HentPersonCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo) {
        return (ident, historikk) -> {
            var personModell = (PersonModell) scenarioRepo.getPersonIndeks().finnByIdent(ident);
            var personPdl    = PersonOversetter.oversettPerson(personModell, historikk);

            var personopplysningerModell = scenarioRepo.getPersonIndeks().finnPersonopplysningerByIdent(ident);
            var aktørIdent = personModell.getAktørIdent();

            return FamilieRelasjonBygger.byggFamilierelasjoner(aktørIdent, personopplysningerModell, personPdl);
        };
    }
}
