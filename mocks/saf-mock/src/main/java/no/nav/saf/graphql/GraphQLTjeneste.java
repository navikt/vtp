package no.nav.saf.graphql;

import static java.util.stream.Collectors.toList;

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
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.saf.Dokumentoversikt;
import no.nav.saf.JournalpostBuilder;
import no.nav.saf.Journalpost;
import no.nav.saf.SideInfo;
import no.nav.saf.exceptions.SafFunctionalException;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GraphQLTjeneste {

    private static final String SCHEME_PATH = "schemas/saf.graphql";

    private TypeDefinitionRegistry typeRegistry;
    private SchemaGenerator schemaGenerator;

    private static GraphQLTjeneste instance;

    public static synchronized GraphQLTjeneste getInstance(){

        if(instance == null){
            instance = new GraphQLTjeneste();
            instance.init();
        }

        return instance;
    }


    public void init() {
        SchemaParser schemaParser = new SchemaParser();
        schemaGenerator = new SchemaGenerator();

        InputStreamReader schema = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(SCHEME_PATH)));
        typeRegistry = schemaParser.parse(schema);
    }

    public ExecutionResult executeStatement(GraphQLRequest request, JournalRepository testscenarioRepository) {
        if (request.getQuery().contains("dokumentoversiktFagsak(fagsak:")) {
            return executeDokumentOversiktFagsak(request, testscenarioRepository);
        } else if (request.getQuery().contains("journalpost(journalpostId:")) {
            return executeJournalpost(request, testscenarioRepository);
        }
        throw new UnsupportedOperationException("Query er ikke støttet i mock");
    }

    private ExecutionResult executeDokumentOversiktFagsak(GraphQLRequest request, JournalRepository testscenarioRepository) {
        String fagsakId = (String) request.getVariables().get("fagsakId");
        String fagsaksystem = (String) request.getVariables().get("fagsaksystem");

        DokumentoversiktFagsakCoordinator coordinator = opprettDokumentsiktFagsakCoordinator(testscenarioRepository);
        RuntimeWiring runtimeWiring = DokumentWiringDokumentoversikt.lagRuntimeWiring(coordinator);
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        return byggExecutionResult(request, graphQLSchema);
    }


    private ExecutionResult executeJournalpost(GraphQLRequest request, JournalRepository testscenarioRepository) {
        String journalpostId = (String) request.getVariables().getOrDefault("journalpostId", "87654321");

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

    private DokumentoversiktFagsakCoordinator opprettDokumentsiktFagsakCoordinator(JournalRepository journalRepository) {

        return (fagsakId, fagsaksystem) -> {
            // Fagsaksystem sees foreløpig bort fra i oppslaget. Kan vurderes å filtrere på kun "K9"
            List<JournalpostModell> modeller = journalRepository.finnJournalposterMedSakId(fagsakId);
            List<Journalpost> journalposter = modeller.stream()
                    .map(jpModell -> JournalpostBuilder.buildFrom(jpModell))
                    .collect(toList());

            return new Dokumentoversikt(journalposter, new SideInfo("sluttpeker", false));
        };
    }

    private JournalpostCoordinator opprettJournalpostCoordinator(JournalRepository journalRepository) {

        return journalpostId -> journalRepository.finnJournalpostMedJournalpostId(journalpostId)
                .map(jpModell -> JournalpostBuilder.buildFrom(jpModell))
                .orElseThrow(() ->  new SafFunctionalException("Fant ingen journalpost for journalpostId=" + journalpostId));
    }

}
