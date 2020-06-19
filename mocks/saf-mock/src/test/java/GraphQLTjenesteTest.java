
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.saf.graphql.GraphQLRequest;
import no.nav.saf.graphql.GraphQLTjeneste;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

public class GraphQLTjenesteTest {

    @Test
    public void test() {
        // Arrange
        String jpId = "12345678";
        String sakId = "sakId";

        GraphQLTjeneste graphQLTjeneste = new GraphQLTjeneste();
        graphQLTjeneste.init();

        GraphQLRequest request = GraphQLRequest.builder()
                .withQuery("query Journalpost($journalpostId: String!) {journalpost(journalpostId: $journalpostId) {journalpostId sak {arkivsaksystem arkivsaksnummer datoOpprettet}}}")
                .withVariables(Map.of("journalpostId", jpId))
                .build();

        // Mock repo
        JournalpostModell journalpostModell = new JournalpostModell();
        journalpostModell.setSakId(sakId);
        journalpostModell.setJournalpostId(jpId);

        JournalRepository journalRepo = Mockito.mock(JournalRepository.class);
        when(journalRepo.finnJournalpostMedJournalpostId(jpId)).thenReturn(Optional.of(journalpostModell));

        // Act
        Map<String, Object> result = graphQLTjeneste.executeStatement(request, journalRepo)
                .toSpecification();

        // Assert
        assertThat(result.get("data"))
                .extracting("journalpost")
                .satisfies(journalpostdata -> ((LinkedHashMap) journalpostdata).containsKey(sakId));
    }

}
