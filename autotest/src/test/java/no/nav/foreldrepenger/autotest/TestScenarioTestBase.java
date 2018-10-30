package no.nav.foreldrepenger.autotest;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.vtp.testscenario.TestscenarioKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;

public abstract class TestScenarioTestBase extends TestBase{

    TestscenarioKlient testscenarioKlient;
    
    public TestScenarioTestBase() {
        testscenarioKlient = new TestscenarioKlient(new HttpSession());
    }
    
    protected TestscenarioDto opprettScenario(String id) throws IOException {
        return testscenarioKlient.opprettTestscenario(id);
    }
}
