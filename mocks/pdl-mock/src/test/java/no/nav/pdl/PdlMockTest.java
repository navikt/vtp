package no.nav.pdl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.TestscenarioHenter;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pdl.graphql.GraphQLRequest;

public class PdlMockTest {

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
    public void hent_person() {
        Object testscenarioObjekt = testscenarioHenter.hentScenario("1");
        String testscenarioJson = testscenarioObjekt == null ? "{}" : testscenarioHenter.toJson(testscenarioObjekt);
        Testscenario testscenario = testScenarioRepository.opprettTestscenario(testscenarioJson, Collections.emptyMap());

        // Arrange
        String ident = testscenario.getPersonopplysninger().getSøker().getIdent();

        PdlMock pdlMock = new PdlMock(testScenarioRepository);

        GraphQLRequest request = GraphQLRequest.builder()
                .withQuery("some query")
                .withOperationName("hentPerson")
                .withVariables(Map.of("ident", ident))
                .build();

        // Act
        var response = pdlMock.graphQLRequest(null, null, null, null, request);

        // Assert
        assertThat(response.get("data")).isNotNull();
    }

}
