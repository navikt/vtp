package no.nav.tjeneste.virksomhet.arbeidsforhold;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Regelverker;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerRequest;

public class ArbeidsforholdMockTest {

    private static TestscenarioRepository testScenarioRepository;
    private static TestscenarioHenter testscenarioHenter;


    @BeforeAll
    public static void setup() throws IOException {
        testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        testscenarioHenter = TestscenarioHenter.getInstance();

    }

    @SuppressWarnings("unused")
    @Test
    public void finnArbeidsforholdPrArbeidstakerTest() throws Exception {
        Object testscenarioObjekt = testscenarioHenter.hentScenario("1");
        String testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        Testscenario testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());

        ArbeidsforholdMockImpl arbeidsforholdMock = new ArbeidsforholdMockImpl(testScenarioRepository);

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
