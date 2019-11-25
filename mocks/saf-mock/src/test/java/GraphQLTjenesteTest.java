
import java.util.LinkedHashMap;
import java.util.Map;

import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.saf.graphql.GraphQLRequest;
import no.nav.saf.graphql.GraphQLTjeneste;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.mockito.Mockito;

public class GraphQLTjenesteTest {

    @Test
    public void test() {
        GraphQLTjeneste graphQLTjeneste = new GraphQLTjeneste();
        graphQLTjeneste.init();

        GraphQLRequest request = GraphQLRequest.builder()
                .withQuery("query Journalpost($journalpostId: String!) {journalpost(journalpostId: $journalpostId) {journalpostId sak {arkivsaksystem arkivsaksnummer datoOpprettet}}}")
                .withVariables(Map.of("journalpostId", "12345678"))
                .build();

        Map<String, Object> result = graphQLTjeneste.executeStatement(request, Mockito.mock(JournalRepository.class))
                .toSpecification();

        assertThat(result.get("data"))
                .extracting("journalpost")
                .satisfies(journalpostdata -> ((LinkedHashMap) journalpostdata).containsKey("sak"));
    }

}
