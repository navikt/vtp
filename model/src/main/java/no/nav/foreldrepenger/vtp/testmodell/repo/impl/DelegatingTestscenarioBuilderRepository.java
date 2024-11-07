package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import no.nav.foreldrepenger.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

public class DelegatingTestscenarioBuilderRepository implements TestscenarioBuilderRepository {

    private final TestscenarioBuilderRepository delegate;

    public DelegatingTestscenarioBuilderRepository(TestscenarioBuilderRepository delegate) {
        Objects.requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public EnheterIndeks getEnheterIndeks() {
        return delegate.getEnheterIndeks();
    }

    @Override
    public Map<String, Testscenario> getTestscenarios() {
        return delegate.getTestscenarios();
    }

    @Override
    public Testscenario getTestscenario(String id) {
        return delegate.getTestscenario(id);
    }

    @Override
    public PersonIndeks getPersonIndeks() {
        return delegate.getPersonIndeks();
    }

    @Override
    public Optional<InntektYtelseModell> getInntektYtelseModell(String ident) {
        return delegate.getInntektYtelseModell(ident);
    }

    @Override
    public Optional<InntektYtelseModell> getInntektYtelseModellFraAktørId(String aktørId) {
        return delegate.getInntektYtelseModellFraAktørId(aktørId);
    }

    @Override
    public BasisdataProvider getBasisdata() {
        return delegate.getBasisdata();
    }

    @Override
    public LokalIdentIndeks getIdenter(String unikScenarioId) {
        return delegate.getIdenter(unikScenarioId);
    }

    @Override
    public Optional<OrganisasjonModell> getOrganisasjon(String orgnr) {
        return delegate.getOrganisasjon(orgnr);
    }

    @Override
    public Boolean slettScenario(String id) {
        return delegate.slettScenario(id);
    }

    @Override
    public Boolean endreTestscenario(String id, Testscenario testscenario) {
        return delegate.endreTestscenario(id, testscenario);
    }

    @Override
    public void indekserPersonopplysninger(Personopplysninger personopplysninger) {
        delegate.indekserPersonopplysninger(personopplysninger);
    }

    @Override
    public Set<String> hentAlleOrganisasjonsnummer() {
        return delegate.hentAlleOrganisasjonsnummer();
    }
}
