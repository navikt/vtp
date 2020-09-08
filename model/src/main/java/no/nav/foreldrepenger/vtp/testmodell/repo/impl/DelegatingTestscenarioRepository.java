package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.util.Map;

import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;

/** Delegerer alle kall til en 'reell' implementasjon. Denne kan således endres under kjøring. */
public class DelegatingTestscenarioRepository extends DelegatingTestscenarioBuilderRepository implements TestscenarioRepository {

    private final TestscenarioRepository delegate;

    public DelegatingTestscenarioRepository(TestscenarioRepository delegate) {
        super(delegate);
        this.delegate = delegate;
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
    public Testscenario opprettTestscenario(String testscenarioJson, Map<String, String> userSuppliedVariables) {
        return delegate.opprettTestscenario(testscenarioJson, userSuppliedVariables);
    }

    @Override
    public Boolean slettScenario(String id) {
        return delegate.slettScenario(id);
    }
}
