package no.nav.foreldrepenger.fpmock.server;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioTemplateRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Regelverker;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerRequest;

public class ArbeidsforholdMockTest {

    private TestscenarioRepository testRepo;

    private TestscenarioTemplateRepository templateRepository;

    @Before
    public void setup() throws IOException {
        System.setProperty("scenarios.dir", "../model/scenarios");
        TestscenarioTemplateRepositoryImpl templateRepositoryImpl = TestscenarioTemplateRepositoryImpl.getInstance();
        templateRepositoryImpl.load();

        templateRepository = new DelegatingTestscenarioTemplateRepository(templateRepositoryImpl);
        var testScenarioRepository = new DelegatingTestscenarioRepository(
            TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        testRepo = testScenarioRepository;

    }

    @SuppressWarnings("unused")
    @Test
    public void finnArbeidsforholdPrArbeidstakerTest() throws Exception {
        TestscenarioTemplate template = templateRepository.finn("50");

        Testscenario testscenario = testRepo.opprettTestscenario(template);

        ArbeidsforholdMockImpl arbeidsforholdMock = new ArbeidsforholdMockImpl(testRepo);

        FinnArbeidsforholdPrArbeidstakerRequest request = new FinnArbeidsforholdPrArbeidstakerRequest();

        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(testscenario.getVariabelContainer().getVar("for1"));
        request.setIdent(norskIdent);

        Regelverker regelverk = new Regelverker();
        regelverk.setKodeverksRef("A_ORDNINGEN");
        request.setRapportertSomRegelverk(regelverk);

        var modell = testscenario.getSøkerInntektYtelse();

        var response = arbeidsforholdMock.finnArbeidsforholdPrArbeidstaker(request);
    }
}
