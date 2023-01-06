package no.nav.saf.graphql;

import static java.util.stream.Collectors.toList;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.SimpleDataFetcherExceptionHandler;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalposttyper;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.saf.Dokumentoversikt;
import no.nav.saf.Journalpost;
import no.nav.saf.JournalpostBuilder;
import no.nav.saf.JournalpostSelvbetjeningBuilder;
import no.nav.saf.SideInfo;
import no.nav.saf.exceptions.SafFunctionalException;
import no.nav.saf.selvbetjening.Fagsak;
import no.nav.saf.selvbetjening.Sak;
import no.nav.saf.selvbetjening.Sakstema;

public class GraphQLTjeneste {

    private static final String SAF_SCHEME_PATH = "schemas/saf.graphql";
    private static final String SAF_SELVBETJENING_SCHEME_PATH = "schemas/schema.graphqls";

    private TypeDefinitionRegistry typeRegistry;
    private TypeDefinitionRegistry typeRegistrySelvbetjening;
    private SchemaGenerator schemaGenerator;

    private static GraphQLTjeneste instance;

    public static synchronized GraphQLTjeneste getInstance(){
        if (instance == null) {
            instance = new GraphQLTjeneste();
        }
        return instance;
    }

    private GraphQLTjeneste() {
        init();
    }

    public void init() {
        SchemaParser schemaParser = new SchemaParser();
        schemaGenerator = new SchemaGenerator();

        InputStreamReader schemaSaf = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(SAF_SCHEME_PATH)));
        typeRegistry = schemaParser.parse(schemaSaf);

        InputStreamReader schemaSelvbetjening = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(SAF_SELVBETJENING_SCHEME_PATH)));
        typeRegistrySelvbetjening = schemaParser.parse(schemaSelvbetjening);

    }

    public ExecutionResult executeStatement(GraphQLRequest request, JournalRepository testscenarioRepository) {
        if (request.getQuery().contains("dokumentoversiktFagsak(fagsak:")) {
            return executeDokumentOversiktFagsak(request, testscenarioRepository);
        } else if (request.getQuery().contains("journalpost(journalpostId:")) {
            return executeJournalpost(request, testscenarioRepository);
        } else if (request.getQuery().contains("dokumentoversiktSelvbetjening")) {
            return executeDokumentOversiktSelvbetjening(request, testscenarioRepository);
        }
        throw new UnsupportedOperationException("Query er ikke støttet i mock");
    }

    private ExecutionResult executeDokumentOversiktSelvbetjening(GraphQLRequest request, JournalRepository testscenarioRepository) {
        DokumentoversiktSelvbetjening coordinator = opprettDokumentoversiktSelvbetjeningCoordinator(testscenarioRepository);
        RuntimeWiring runtimeWiring = DokumentWiringSelvbetjeningDokumentoversikt.lagRuntimeWiring(coordinator);
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistrySelvbetjening, runtimeWiring);
        return byggExecutionResult(request, graphQLSchema);
    }

    private ExecutionResult executeDokumentOversiktFagsak(GraphQLRequest request, JournalRepository testscenarioRepository) {
        DokumentoversiktFagsakCoordinator coordinator = opprettDokumentoversiktFagsakCoordinator(testscenarioRepository);
        RuntimeWiring runtimeWiring = DokumentWiringDokumentoversikt.lagRuntimeWiring(coordinator);
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        return byggExecutionResult(request, graphQLSchema);
    }


    private ExecutionResult executeJournalpost(GraphQLRequest request, JournalRepository testscenarioRepository) {
        JournalpostCoordinator coordinator = opprettJournalpostCoordinator(testscenarioRepository);
        RuntimeWiring runtimeWiring = DokumentWiringJournalpost.lagRuntimeWiring(coordinator);
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        return byggExecutionResult(request, graphQLSchema);
    }

    private ExecutionResult byggExecutionResult(GraphQLRequest request, GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema)
                .mutationExecutionStrategy(new AsyncSerialExecutionStrategy(new SimpleDataFetcherExceptionHandler()))
                .queryExecutionStrategy(new AsyncExecutionStrategy(new SimpleDataFetcherExceptionHandler()))
                .build()
                .execute(ExecutionInput.newExecutionInput()
                        .query(request.getQuery())
                        .operationName(request.getOperationName())
                        .variables(request.getVariables() == null ? Collections.emptyMap() : request.getVariables())
                        .build());
    }

    private DokumentoversiktFagsakCoordinator opprettDokumentoversiktFagsakCoordinator(JournalRepository journalRepository) {

        return (fagsakId, fagsaksystem) -> {
            // Fagsaksystem sees foreløpig bort fra i oppslaget. Kan vurderes å filtrere på kun "K9"
            List<JournalpostModell> modeller = journalRepository.finnJournalposterMedSakId(fagsakId);
            List<Journalpost> journalposter = modeller.stream()
                    .map(JournalpostBuilder::buildFrom)
                    .collect(toList());

            return new Dokumentoversikt(journalposter, new SideInfo("sluttpeker", false));
        };
    }

    private DokumentoversiktSelvbetjening opprettDokumentoversiktSelvbetjeningCoordinator(JournalRepository journalRepository) {

        return fødselsnummer -> {
            List<JournalpostModell> modeller = journalRepository.finnJournalposterMedFnr(fødselsnummer);
            List<no.nav.saf.selvbetjening.Journalpost> journalposter = modeller.stream()
                   .filter(jp -> jp.getJournalposttype() != Journalposttyper.NOTAT)
                   .map(JournalpostSelvbetjeningBuilder::buildFrom)
                   .collect(toList());
            var førsteFagsakId = journalposter.stream().map(no.nav.saf.selvbetjening.Journalpost::getSak)
                  .map(Sak::getFagsakId)
                  .filter(Objects::nonNull)
                  .findFirst().orElse(null);
            var builder = no.nav.saf.selvbetjening.Dokumentoversikt.builder();
            builder.setJournalposter(journalposter);
            // Hardkoder foreldrepenger-verdier
            builder.setFagsak(List.of(new Fagsak(journalposter, førsteFagsakId, "FS36", "FOR")));
            builder.setTema(List.of(new Sakstema(journalposter, "FOR", "Foreldrepenger")));
            return builder.build();
        };
    }

    private JournalpostCoordinator opprettJournalpostCoordinator(JournalRepository journalRepository) {

        return journalpostId -> journalRepository.finnJournalpostMedJournalpostId(journalpostId)
                .map(JournalpostBuilder::buildFrom)
                .orElseThrow(() ->  new SafFunctionalException("Fant ingen journalpost for journalpostId=" + journalpostId));
    }

}
