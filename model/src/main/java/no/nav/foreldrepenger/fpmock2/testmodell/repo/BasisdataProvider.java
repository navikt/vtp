package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.identer.IdentGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.VirksomhetIndeks;

public interface BasisdataProvider {

    VirksomhetIndeks getVirksomhetIndeks();

    EnheterIndeks getEnheterIndeks();

    AdresseIndeks getAdresseIndeks();

    /** Genererer nye personidenter. */
    IdentGenerator getIdentGenerator();

    
}
