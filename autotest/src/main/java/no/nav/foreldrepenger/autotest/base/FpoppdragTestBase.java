package no.nav.foreldrepenger.autotest.base;

import org.junit.jupiter.api.BeforeEach;

import no.nav.foreldrepenger.autotest.aktoerer.fpoppdrag.Saksbehandler;
import no.nav.foreldrepenger.autotest.klienter.vtp.testscenario.TestscenarioKlient;
import no.nav.foreldrepenger.autotest.util.http.BasicHttpSession;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingErketype;

public class FpoppdragTestBase extends TestBase {

    protected Saksbehandler saksbehandler;
    protected TestscenarioKlient testscenarioKlient;

    protected InntektsmeldingErketype inntektsmeldingErketype;

    @BeforeEach
    void setUp() throws Exception {
        saksbehandler = new Saksbehandler();
        testscenarioKlient = new TestscenarioKlient(BasicHttpSession.session());
        inntektsmeldingErketype = new InntektsmeldingErketype();
    }

}
