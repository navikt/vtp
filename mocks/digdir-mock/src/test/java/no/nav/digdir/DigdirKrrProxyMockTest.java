package no.nav.digdir;

import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DigdirKrrProxyMockTest {

    private static DigdirKrrProxyMock digdirKrrProxyMock;
    private static SøkerModell søker;

    @BeforeAll
    static void setup() {
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
        var response = digdirKrrProxyMock.hentKontaktinformasjon(new DigdirKrrProxyMock.Personidenter(List.of(søker.getIdent())));
        var kontaktinformasjon = (Kontaktinformasjoner) response.getEntity();

        assertThat(kontaktinformasjon).isNotNull();
        assertThat(kontaktinformasjon.personer()).hasSize(1);
        assertThat(kontaktinformasjon.personer().get(søker.getIdent()).spraak()).isEqualTo("NB");
    }


    @Test
    void hentSpråkFraDigdirKrrProxyNårPersonIkkeFinnesKaster404() {
        var identSomIkkeFinnes = "11111122222";
        var response = digdirKrrProxyMock.hentKontaktinformasjon(new DigdirKrrProxyMock.Personidenter(List.of(identSomIkkeFinnes)));
        var kontaktinformasjon = (Kontaktinformasjoner) response.getEntity();

        assertThat(kontaktinformasjon.personer()).isEmpty();
        assertThat(kontaktinformasjon.feil()).hasSize(1);
        assertThat(kontaktinformasjon.feil().get(identSomIkkeFinnes)).isEqualTo(Kontaktinformasjoner.FeilKode.person_ikke_funnet);

    }

}
