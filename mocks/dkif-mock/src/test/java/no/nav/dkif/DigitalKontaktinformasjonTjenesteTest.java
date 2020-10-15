package no.nav.dkif;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

public class DigitalKontaktinformasjonTjenesteTest {

    private static TestscenarioRepository testScenarioRepository;
    private static TestscenarioHenter testscenarioHenter;
    private static DigitalKontaktinformasjonMock digitalKontaktinformasjonMock;

    @BeforeAll
    public static void setup() throws IOException {
        testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        testscenarioHenter = TestscenarioHenter.getInstance();
        digitalKontaktinformasjonMock = new DigitalKontaktinformasjonMock();
    }

    @Test
    public void hent_person() {
        var testscenarioObjekt = testscenarioHenter.hentScenario("1");
        var testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        var testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());
        SøkerModell søker = testscenario.getPersonopplysninger().getSøker();
        var response = digitalKontaktinformasjonMock.hentKontaktinformasjon(søker.getIdent(), null, null, null);

        assertThat(response).isNotNull();
        assertThat(response.getSpraak(søker.getIdent()).get()).isEqualTo("NB");
    }

}
