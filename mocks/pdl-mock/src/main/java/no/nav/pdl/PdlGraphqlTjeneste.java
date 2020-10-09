package no.nav.pdl;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Objects;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.SimpleDataFetcherExceptionHandler;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.graphql.GraphQLRequest;
import no.nav.pdl.hentIdenter.HentIdenterCoordinatorFunction;
import no.nav.pdl.hentIdenter.HentIdenterWiring;
import no.nav.pdl.hentperson.HentPersonCoordinatorFunction;
import no.nav.pdl.hentperson.HentPersonWiring;

public class PdlGraphqlTjeneste {

    private static final String SCHEME_PATH = "schemas/pdl-api-sdl.graphqls";

    private static PdlGraphqlTjeneste instance;
    private final TestscenarioBuilderRepository scenarioRepository;

    private GraphQLSchema hentPersonGraphqlSchema;
    private GraphQLSchema hentIdenterGraphqlSchema;

    public static synchronized PdlGraphqlTjeneste getInstance(TestscenarioBuilderRepository scenarioRepository){
        if(instance == null){
            instance = new PdlGraphqlTjeneste(scenarioRepository);
            instance.init();
        }
        return instance;
    }

    public PdlGraphqlTjeneste(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    public void init() {
        // GrapQL-skjema
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();

        InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(SCHEME_PATH)));
        TypeDefinitionRegistry typeDefinition = schemaParser.parse(streamReader);

        // Opprette koordinatere og binde til GraphQL-skjema
        var hentPersonCoordinator = HentPersonCoordinatorFunction.opprettCoordinator(scenarioRepository);
        var hentPersonWiring = HentPersonWiring.lagRuntimeWiring(hentPersonCoordinator);
        hentPersonGraphqlSchema = schemaGenerator.makeExecutableSchema(typeDefinition, hentPersonWiring);

        var hentIdenterCoordinator = HentIdenterCoordinatorFunction.opprettCoordinator(scenarioRepository);
        var hentIdenterWiring = HentIdenterWiring.lagRuntimeWiring(hentIdenterCoordinator);
        hentIdenterGraphqlSchema = schemaGenerator.makeExecutableSchema(typeDefinition, hentIdenterWiring);
    }

    ExecutionResult hentPerson(GraphQLRequest request) {
        return byggExecutionResult(request, hentPersonGraphqlSchema);
    }

    ExecutionResult hentIdenter(GraphQLRequest request) {
        return byggExecutionResult(request, hentIdenterGraphqlSchema);
    }

    static ExecutionResult byggExecutionResult(GraphQLRequest request, GraphQLSchema graphQLSchema) {
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

}


