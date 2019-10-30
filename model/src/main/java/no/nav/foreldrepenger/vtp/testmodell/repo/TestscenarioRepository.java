package no.nav.foreldrepenger.vtp.testmodell.repo;

import java.util.Map;

public interface TestscenarioRepository extends TestscenarioBuilderRepository {

    @Override
    Map<String, Testscenario> getTestscenarios();

    @Override
    Testscenario getTestscenario(String id);

    Testscenario opprettTestscenario(TestscenarioTemplate template);

    Testscenario opprettTestscenario(TestscenarioTemplate template, Map<String, String> userSuppliedVariables);

    Testscenario opprettTestscenarioFraJsonString(String testscenarioJson);

}
