package no.nav.fager;

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
import no.nav.fager.graphql.GraphQLRequest;
import no.nav.fager.oppgave.OppgaveFagerCoordinatorImpl;
import no.nav.fager.oppgave.OppgaveFagerWiring;
import no.nav.fager.sak.SakFagerCoordinatorImpl;
import no.nav.fager.sak.SakFagerWiring;
import no.nav.foreldrepenger.vtp.testmodell.repo.ArbeidsgiverPortalRepository;

public class FagerGraphqlTjeneste {
    private static final String SCHEME_PATH = "schemas/produsent.graphql";

    private static FagerGraphqlTjeneste instance;
    private final ArbeidsgiverPortalRepository arbeidsgiverPortalRepository;

    private GraphQLSchema sakGraphqlSchema;
    private GraphQLSchema oppgaveGraphqlSchema;

    public static FagerGraphqlTjeneste getInstance(ArbeidsgiverPortalRepository arbeidsgiverPortalRepository) {
        if (instance == null) {
            instance = new FagerGraphqlTjeneste(arbeidsgiverPortalRepository);
        } return instance;
    }

    FagerGraphqlTjeneste(ArbeidsgiverPortalRepository arbeidsgiverPortalRepository) {
        this.arbeidsgiverPortalRepository = arbeidsgiverPortalRepository;
        init();
    }

    void init() {
        // GrapQL-skjema
        var schemaParser = new SchemaParser();
        var schemaGenerator = new SchemaGenerator();

        var streamReader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(SCHEME_PATH)));
        var typeDefinition = schemaParser.parse(streamReader);
        var originalWiring = FagerBasicWiring.lagBasicWiring();

        var sakWiring = SakFagerWiring.lagRuntimeWiring(new SakFagerCoordinatorImpl(arbeidsgiverPortalRepository), originalWiring);
        sakGraphqlSchema = schemaGenerator.makeExecutableSchema(typeDefinition, sakWiring);

        var oppgaveWiring = OppgaveFagerWiring.lagRuntimeWiring(new OppgaveFagerCoordinatorImpl(arbeidsgiverPortalRepository), originalWiring);
        oppgaveGraphqlSchema = schemaGenerator.makeExecutableSchema(typeDefinition, oppgaveWiring);
    }

    ExecutionResult sak(GraphQLRequest request) {
        return byggExecutionResult(request, sakGraphqlSchema);
    }

    ExecutionResult oppgave(GraphQLRequest request) {
        return byggExecutionResult(request, oppgaveGraphqlSchema);
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


