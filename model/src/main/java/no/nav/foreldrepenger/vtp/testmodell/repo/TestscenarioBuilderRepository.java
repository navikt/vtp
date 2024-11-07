package no.nav.foreldrepenger.vtp.testmodell.repo;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;

public interface TestscenarioBuilderRepository {

    EnheterIndeks getEnheterIndeks();

    PersonIndeks getPersonIndeks();

    Optional<InntektYtelseModell> getInntektYtelseModell(String ident);

    Optional<InntektYtelseModell> getInntektYtelseModellFraAktørId(String aktørId);

    Optional<OrganisasjonModell> getOrganisasjon(String orgnr);

    LokalIdentIndeks getIdenter(String unikScenarioId);

    BasisdataProvider getBasisdata();

    Map<String, Testscenario> getTestscenarios();

    Testscenario getTestscenario(String id);

    Boolean slettScenario(String id);

    Boolean endreTestscenario(String id, Testscenario testscenario);

    void indekserPersonopplysninger(Personopplysninger personopplysninger);

    Set<String> hentAlleOrganisasjonsnummer();

}
