package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.util.Collection;
import java.util.Objects;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;

public class DelegatingTestscenarioTemplateRepository implements TestscenarioTemplateRepository {
    
    private volatile TestscenarioTemplateRepository delegate;

    public DelegatingTestscenarioTemplateRepository(TestscenarioTemplateRepository delegate) {
        Objects.requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public Collection<TestscenarioTemplate> getTemplates() {
        return delegate.getTemplates();
    }

    @Override
    public TestscenarioTemplate finn(String templateKey) {
       return delegate.finn(templateKey); 
    }
    
}
