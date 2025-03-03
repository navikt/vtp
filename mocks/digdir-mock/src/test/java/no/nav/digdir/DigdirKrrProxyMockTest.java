package no.nav.digdir;

import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DigdirKrrProxyMockTest {

    private static DigdirKrrProxyMock digdirKrrProxyMock;
    private static SøkerModell søker;

    @BeforeAll
    public static void setup() {
        var testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        var testscenarioHenter = TestscenarioHenter.getInstance();
        digdirKrrProxyMock = new DigdirKrrProxyMock();

        var testscenarioObjekt = testscenarioHenter.hentScenario("1");
        var testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        var testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        søker = testscenario.getPersonopplysninger().getSøker();
    }

    @Test
    void hentSpråkFraDigdirKrrProxy() {
        var response = digdirKrrProxyMock.hentKontaktinformasjon(søker.getIdent());
        var kontaktinformasjon = (Kontaktinformasjon) response.getEntity();

        assertThat(kontaktinformasjon).isNotNull();
        assertThat(kontaktinformasjon.getSpraak()).isEqualTo("NB");
    }


    @Test
    void hentSpråkFraDigdirKrrProxyNårPersonIkkeFinnesKaster404() {
        var response = digdirKrrProxyMock.hentKontaktinformasjon("11111122222");
        assertThat(response.getStatus()).isEqualTo(404);
    }

}
