package no.nav.foreldrepenger.vtp.testmodell.repo;

import java.util.Collection;

public interface TestscenarioTemplateRepository {

    Collection<TestscenarioTemplate> getTemplates();

    TestscenarioTemplate finn(String templateKey);

    TestscenarioTemplate finnMedTemplatenavn(String templateNavn);

}
