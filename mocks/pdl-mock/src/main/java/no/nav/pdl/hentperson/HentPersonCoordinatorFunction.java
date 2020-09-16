package no.nav.pdl.hentperson;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.oversetter.FamilieRelasjonBygger;
import no.nav.pdl.oversetter.PersonAdapter;

public class HentPersonCoordinatorFunction {

    public static HentPersonCoordinator opprettCoordinator(TestscenarioBuilderRepository scenarioRepo) {
        return ident -> {
            var personModell = (PersonModell) scenarioRepo.getPersonIndeks().finnByIdent(ident);
            var personPdl    = PersonAdapter.oversettPerson(personModell);

            var personopplysningerModell = scenarioRepo.getPersonIndeks().finnPersonopplysningerByIdent(ident);
            var aktørIdent = personModell.getAktørIdent();
            personPdl = FamilieRelasjonBygger.byggFamilierelasjoner(aktørIdent, personopplysningerModell, personPdl);

            return personPdl;
        };
    }
}
