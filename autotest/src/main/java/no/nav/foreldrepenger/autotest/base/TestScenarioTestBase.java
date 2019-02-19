package no.nav.foreldrepenger.autotest.base;

import java.io.IOException;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.klienter.vtp.testscenario.TestscenarioKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;

public abstract class TestScenarioTestBase extends TestBase {

    TestscenarioKlient testscenarioKlient;

    public TestScenarioTestBase() {
        testscenarioKlient = new TestscenarioKlient(new HttpSession());
    }

    @Step("Oppretter testscenario {id}")
    protected TestscenarioDto opprettScenario(String id) throws IOException {
        return testscenarioKlient.opprettTestscenario(id);
    }
}
