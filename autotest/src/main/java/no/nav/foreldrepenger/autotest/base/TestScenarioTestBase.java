package no.nav.foreldrepenger.autotest.base;

import java.io.IOException;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.ExpectKlient;
import no.nav.foreldrepenger.autotest.klienter.vtp.testscenario.TestscenarioKlient;
import no.nav.foreldrepenger.autotest.util.http.BasicHttpSession;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;

public abstract class TestScenarioTestBase extends TestBase {
    
    protected TestscenarioKlient testscenarioKlient;
    protected ExpectKlient expectKlient;
    
    public TestScenarioTestBase() {
        testscenarioKlient = new TestscenarioKlient(BasicHttpSession.session());
        expectKlient = new ExpectKlient(BasicHttpSession.session());
    }

    @Step("Oppretter testscenario {id}")
    protected TestscenarioDto opprettScenario(String id) throws IOException {
        return testscenarioKlient.opprettTestscenario(id);
    }

    @Step("Oppretter testscenario {id}")
    protected TestscenarioDto opprettScenarioMedPrivatArbeidsgiver(String id, String aktorId) throws IOException {
        return testscenarioKlient.opprettTestscenarioMedAktorId(id, aktorId);
    }
}
