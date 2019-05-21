package no.nav.foreldrepenger.fpmock.server;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioTemplateRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Regelverker;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerResponse;

public class ArbeidsforholdMockTest {

    private TestscenarioRepository testRepo;

    private TestscenarioTemplateRepository templateRepository;

    @Before
    public void setup() throws IOException {
        System.setProperty("scenarios.dir", "../model/scenarios");
        TestscenarioTemplateRepositoryImpl templateRepositoryImpl = TestscenarioTemplateRepositoryImpl.getInstance();
        templateRepositoryImpl.load();

        templateRepository = new DelegatingTestscenarioTemplateRepository(templateRepositoryImpl);
        DelegatingTestscenarioRepository testScenarioRepository = new DelegatingTestscenarioRepository(TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        testRepo = testScenarioRepository;

    }

    @Test
    public void finnArbeidsforholdPrArbeidstakerTest(){
        TestscenarioTemplate template = templateRepository.finn("50");

        Testscenario testscenario = testRepo.opprettTestscenario(template);

        ArbeidsforholdMockImpl arbeidsforholdMock = new ArbeidsforholdMockImpl(testRepo);

        FinnArbeidsforholdPrArbeidstakerRequest finnArbeidsforholdPrArbeidstakerRequest = new FinnArbeidsforholdPrArbeidstakerRequest();

        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(testscenario.getVariabelContainer().getVar("for1"));
        finnArbeidsforholdPrArbeidstakerRequest.setIdent(norskIdent);

        Regelverker regelverk = new Regelverker();
        regelverk.setKodeverksRef("A_ORDNINGEN");
        finnArbeidsforholdPrArbeidstakerRequest.setRapportertSomRegelverk(regelverk);

        InntektYtelseModell søkerInntektYtelse = testscenario.getSøkerInntektYtelse();


        try {
            FinnArbeidsforholdPrArbeidstakerResponse finnArbeidsforholdPrArbeidstakerResponse = arbeidsforholdMock.finnArbeidsforholdPrArbeidstaker(finnArbeidsforholdPrArbeidstakerRequest);

            String s = "";
        } catch (FinnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning finnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning) {
            finnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning.printStackTrace();
        } catch (FinnArbeidsforholdPrArbeidstakerUgyldigInput finnArbeidsforholdPrArbeidstakerUgyldigInput) {
            finnArbeidsforholdPrArbeidstakerUgyldigInput.printStackTrace();
        }
    }
}
