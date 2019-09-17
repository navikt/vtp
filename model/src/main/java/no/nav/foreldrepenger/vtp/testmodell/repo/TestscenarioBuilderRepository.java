package no.nav.foreldrepenger.vtp.testmodell.repo;

import java.util.Collection;
import java.util.Optional;

import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonIndeks;

public interface TestscenarioBuilderRepository {

    EnheterIndeks getEnheterIndeks();

    PersonIndeks getPersonIndeks();

    Optional<InntektYtelseModell> getInntektYtelseModell(String ident);

    Optional<InntektYtelseModell> getInntektYtelseModellFraAktørId(String aktørId);

    Optional<OrganisasjonModell> getOrganisasjon(String orgnr);

    LokalIdentIndeks getIdenter(String unikScenarioId);

    BasisdataProvider getBasisdata();

    Collection<Testscenario> getTestscenarios();

    Boolean slettScenario(String id);

    Boolean endreTestscenario(Testscenario testscenario);

}
