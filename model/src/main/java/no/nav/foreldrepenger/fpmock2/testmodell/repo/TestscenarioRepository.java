package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import java.util.Collection;
import java.util.Map;

public interface TestscenarioRepository extends TestscenarioBuilderRepository {

    @Override
    Collection<Testscenario> getTestscenarios();

    Testscenario opprettTestscenario(TestscenarioTemplate template);
    
    Testscenario opprettTestscenario(TestscenarioTemplate template, Map<String, String> userSuppliedVariables);

}
