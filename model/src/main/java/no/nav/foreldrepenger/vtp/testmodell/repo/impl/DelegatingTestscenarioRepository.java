package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioTemplate;

import java.util.Map;

/** Delegerer alle kall til en 'reell' implementasjon. Denne kan således endres under kjøring. */
public class DelegatingTestscenarioRepository extends DelegatingTestscenarioBuilderRepository implements TestscenarioRepository {

    private final TestscenarioRepository delegate;

    public DelegatingTestscenarioRepository(TestscenarioRepository delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @Override
    public Map<String, TestscenarioImpl> getTestscenarios() {
        return delegate.getTestscenarios();
    }

    @Override
    public Testscenario getTestscenario(String id) {
        return delegate.getTestscenario(id);
    }

    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template) {
        return delegate.opprettTestscenario(template);
    }

    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template, Map<String, String> userSuppliedVariables) {
        return delegate.opprettTestscenario(template, userSuppliedVariables);
    }

    @Override
    public Testscenario opprettTestscenarioFraJsonString(String testscenarioJson) {
        return delegate.opprettTestscenarioFraJsonString(testscenarioJson);
    }

    @Override
    public Boolean slettScenario(String id) {
        return delegate.slettScenario(id);
    }
}
