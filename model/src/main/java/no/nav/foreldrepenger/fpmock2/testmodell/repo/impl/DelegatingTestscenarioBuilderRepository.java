package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;

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
    public Collection<Testscenario> getTestscenarios() {
        return delegate.getTestscenarios();
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
}
