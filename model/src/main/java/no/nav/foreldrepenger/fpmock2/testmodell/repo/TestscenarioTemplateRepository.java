package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import java.util.Collection;

public interface TestscenarioTemplateRepository {

    Collection<TestscenarioTemplate> getTemplates();

    TestscenarioTemplate finn(String templateKey);

}
