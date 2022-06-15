package no.nav.dkif;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

public class DKIFOgDigdirKrrProxyTjenesteTest {

    private static DigitalKontaktinformasjonMock digitalKontaktinformasjonMock;
    private static DigdirKrrProxyMock digdirKrrProxyMock;
    private static SøkerModell søker;

    @BeforeAll
    public static void setup() {
        var testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        var testscenarioHenter = TestscenarioHenter.getInstance();
        digitalKontaktinformasjonMock = new DigitalKontaktinformasjonMock();
        digdirKrrProxyMock = new DigdirKrrProxyMock();

        var testscenarioObjekt = testscenarioHenter.hentScenario("1");
        var testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        var testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        søker = testscenario.getPersonopplysninger().getSøker();
    }

    @Test
    void hentSpråkFraDKIF() {
        var response = digitalKontaktinformasjonMock.hentKontaktinformasjon(søker.getIdent(), null, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getSpraak(søker.getIdent()).get()).isEqualTo("NB");
    }


    @Test
    void hentSpråkFraDigdirKrrProxy() {
        var response = digdirKrrProxyMock.hentKontaktinformasjon(søker.getIdent(), null);
        var kontaktinformasjon = (Kontaktinformasjon) response.getEntity();

        assertThat(kontaktinformasjon).isNotNull();
        assertThat(kontaktinformasjon.getSpraak()).isEqualTo("NB");
    }


    @Test
    void hentSpråkFraDigdirKrrProxyNårPersonIkkeFinnesKaster404() {
        var response = digdirKrrProxyMock.hentKontaktinformasjon("11111122222", null);
        assertThat(response.getStatus()).isEqualTo(404);
    }

}
