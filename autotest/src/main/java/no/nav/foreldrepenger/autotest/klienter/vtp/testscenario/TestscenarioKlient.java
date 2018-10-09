package no.nav.foreldrepenger.autotest.klienter.vtp.testscenario;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;

public class TestscenarioKlient extends VTPKlient{

    private static final String TESTSCENARIO_URL = "/testscenario";
    private static final String TESTSCENARIO_POST_URL = "/testscenario/%s";
    private static final String TESTSCENARIO_TEMPLATES_URL = TESTSCENARIO_URL + "/templates";
    private static final String TESTSCENARIO_TEMPLATES_SINGLE_URL = TESTSCENARIO_URL + "/templates/%s";
    
    
    public TestscenarioKlient(HttpSession session) {
        super(session);
    }

    
    public TestscenarioDto opprettTestscenario(String key) throws IOException {
        String url = hentRestRotUrl() + String.format(TESTSCENARIO_POST_URL, key);
        return postOgHentJson(url, null, TestscenarioDto.class, StatusRange.STATUS_SUCCESS);
    }
    
}
