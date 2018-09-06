package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioRepository;

/** Delegerer alle kall til en 'reell' implementasjon. Denne kan således endres under kjøring. */
public class DelegatingTestscenarioRepository implements TestscenarioRepository {

    private volatile TestscenarioRepository delegate;

    public DelegatingTestscenarioRepository(TestscenarioRepository delegate) {
        setDelegate(delegate);
    }

    @Override
    public EnheterIndeks getEnheterIndeks() {
        return delegate.getEnheterIndeks();
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
    public Collection<Testscenario> getTestscenarios() {
        return delegate.getTestscenarios();
    }

    public void setDelegate(TestscenarioRepository delegate) {
        Objects.requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public LokalIdentIndeks getIdenter(String unikScenarioId) {
        return delegate.getIdenter(unikScenarioId);
    }

}
