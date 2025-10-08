import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostBruker;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivtema;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.BrukerType;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalposttyper;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Mottakskanal;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.saf.graphql.GraphQLRequest;
import no.nav.saf.graphql.GraphQLTjeneste;

class GraphQLTjenesteTest {

    private final static String jpId = "12345678";
    private final static String sakId = "sakId";
    private final static String fnr = "12345678901";

    @Test
    void testSaf() {
        // Arrange
        GraphQLTjeneste graphQLTjeneste = GraphQLTjeneste.getInstance();
        graphQLTjeneste.init();

        GraphQLRequest request = GraphQLRequest.builder()
                .withQuery("query Journalpost($journalpostId: String!) {journalpost(journalpostId: $journalpostId) {journalpostId tilleggsopplysninger {nokkel verdi} sak {arkivsaksystem arkivsaksnummer datoOpprettet}}}")
                .withVariables(Map.of("journalpostId", jpId))
                .build();

        // Mock repo
        JournalRepository journalRepo = Mockito.mock(JournalRepository.class);
        when(journalRepo.finnJournalpostMedJournalpostId(jpId)).thenReturn(Optional.of(journalpostModell()));

        // Act
        Map<String, Object> result = graphQLTjeneste.executeStatement(request, journalRepo)
                .toSpecification();

        // Assert
        assertThat(result.get("data"))
                .extracting("journalpost")
                .extracting("sak")
                .extracting("arkivsaksnummer")
                .matches(sakId::equals);

        assertThat(result.get("data"))
                .extracting("journalpost")
                .extracting("tilleggsopplysninger")
                .isInstanceOf(List.class);
    }

    @Test
    void testSafSelvbetjening() {

        GraphQLTjeneste graphQLTjeneste = GraphQLTjeneste.getInstance();
        graphQLTjeneste.init();

        var alternativQuery = "query Dokumentoversikt($ident: String!) {dokumentoversiktSelvbetjening(ident: $ident, tema: [FOR]) {journalposter{journalpostId}}}";

        GraphQLRequest request = GraphQLRequest.builder()
                .withQuery(alternativQuery)
                .withVariables(Map.of("ident", fnr))
                .build();

        JournalRepository journalRepo = Mockito.mock(JournalRepository.class);
        when(journalRepo.finnJournalposterMedFnr(any())).thenReturn(List.of(journalpostModell()));

        var resultat = graphQLTjeneste.executeStatement(request, journalRepo).toSpecification();

        assertThat(resultat.get("data"))
                .extracting("dokumentoversiktSelvbetjening")
                .extracting("journalposter")
                .extracting(jp -> ((List<?>) jp).getFirst())
                .extracting("journalpostId")
                .matches(jpId::equals);
    }

    private JournalpostModell journalpostModell() {
        JournalpostModell modell = new JournalpostModell();
        modell.setSakId(sakId);
        modell.setJournalpostId(jpId);
        var bruker = new JournalpostBruker(fnr, BrukerType.FNR);
        modell.setBruker(bruker);
        modell.setAvsenderMottaker(bruker);
        modell.setJournalposttype(Journalposttyper.INNGAAENDE_DOKUMENT);
        modell.setArkivtema(Arkivtema.FOR);
        modell.setMottakskanal(Mottakskanal.NAV_NO);
        modell.setMottattDato(LocalDateTime.now());
        return modell;
    }

}
