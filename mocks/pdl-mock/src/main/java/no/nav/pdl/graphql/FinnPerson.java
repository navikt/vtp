package no.nav.pdl.graphql;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

public class FinnPerson {

    private TestscenarioBuilderRepository repo;

    public FinnPerson(TestscenarioBuilderRepository repo) {
        this.repo = repo;
    }

    public PersonModell finnPerson(String ident) {
        BrukerModell bruker = repo.getPersonIndeks().finnByIdent(ident);
        // TODO: Exception dersom bruker ikke finnes osv
        return (PersonModell)bruker;
    }

}
