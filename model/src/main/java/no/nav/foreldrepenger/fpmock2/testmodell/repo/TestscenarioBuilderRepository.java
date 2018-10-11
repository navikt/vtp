package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import java.util.Collection;
import java.util.Optional;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonIndeks;

public interface TestscenarioBuilderRepository {

    EnheterIndeks getEnheterIndeks();

    PersonIndeks getPersonIndeks();

    Optional<InntektYtelseModell> getInntektYtelseModell(String ident);

    Optional<OrganisasjonModell> getOrganisasjon(String orgnr);

    LokalIdentIndeks getIdenter(String unikScenarioId);

    BasisdataProvider getBasisdata();

    Collection<Testscenario> getTestscenarios();

}
