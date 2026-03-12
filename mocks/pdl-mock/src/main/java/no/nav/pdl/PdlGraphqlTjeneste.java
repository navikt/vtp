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
import no.nav.pdl.graphql.GraphQLRequest;
import no.nav.pdl.hentGeografiskTilknytning.HentGeografiskTilknytningCoordinatorFunction;
import no.nav.pdl.hentGeografiskTilknytning.HentGeografiskTilknytningWiring;
import no.nav.pdl.hentIdenter.HentIdenterCoordinatorFunction;
import no.nav.pdl.hentIdenter.HentIdenterWiring;
import no.nav.pdl.hentIdenterBolk.HentIdenterBolkWiring;
import no.nav.pdl.hentperson.HentPersonCoordinatorFunction;
import no.nav.pdl.hentperson.HentPersonWiring;
import no.nav.pdl.hentpersonBolk.HentPersonBolkWiring;
import no.nav.vtp.PersonRepository;

public class PdlGraphqlTjeneste {

    private static final String SCHEME_PATH = "schemas/pdl.graphqls";

    private static PdlGraphqlTjeneste instance;
    private final PersonRepository personRepository;

    private GraphQLSchema hentPersonGraphqlSchema;
    private GraphQLSchema hentPersonBolkGraphqlSchema;
    private GraphQLSchema hentGeografiskTilknytningGraphqlSchema;
    private GraphQLSchema hentIdenterGraphqlSchema;
    private GraphQLSchema hentIdenterBolkGraphqlSchema;

    public static synchronized PdlGraphqlTjeneste getInstance(PersonRepository personRepository){
        if(instance == null){
            instance = new PdlGraphqlTjeneste(personRepository);
        }
        return instance;
    }

    private PdlGraphqlTjeneste(PersonRepository personRepository) {
        this.personRepository = personRepository;
        init();
    }

    public void init() {
        // GrapQL-skjema
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();

        InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(SCHEME_PATH)));
        TypeDefinitionRegistry typeDefinition = schemaParser.parse(streamReader);

        // Opprette koordinatere og binde til GraphQL-skjema
        var hentPersonCoordinator = HentPersonCoordinatorFunction.opprettCoordinator(personRepository);
        var hentPersonWiring = HentPersonWiring.lagRuntimeWiring(hentPersonCoordinator);
        hentPersonGraphqlSchema = schemaGenerator.makeExecutableSchema(typeDefinition, hentPersonWiring);

        var hentPersonBolkWiring = HentPersonBolkWiring.lagRuntimeWiring(hentPersonCoordinator);
        hentPersonBolkGraphqlSchema = schemaGenerator.makeExecutableSchema(typeDefinition, hentPersonBolkWiring);

        var hentGeografiskTilknytningCoordinator = HentGeografiskTilknytningCoordinatorFunction.opprettCoordinator(personRepository);
        var hentGeografiskTilknyningWiring = HentGeografiskTilknytningWiring.lagRuntimeWiring(hentGeografiskTilknytningCoordinator);
        hentGeografiskTilknytningGraphqlSchema = schemaGenerator.makeExecutableSchema(typeDefinition, hentGeografiskTilknyningWiring);

        var hentIdenterCoordinator = HentIdenterCoordinatorFunction.opprettCoordinator(personRepository);
        var hentIdenterWiring = HentIdenterWiring.lagRuntimeWiring(hentIdenterCoordinator);
        hentIdenterGraphqlSchema = schemaGenerator.makeExecutableSchema(typeDefinition, hentIdenterWiring);

        var hentIdenterBolkWiring = HentIdenterBolkWiring.lagRuntimeWiring(hentIdenterCoordinator);
        hentIdenterBolkGraphqlSchema = schemaGenerator.makeExecutableSchema(typeDefinition, hentIdenterBolkWiring);
    }

    ExecutionResult hentPerson(GraphQLRequest request) {
        return byggExecutionResult(request, hentPersonGraphqlSchema);
    }

    ExecutionResult hentPersonBolk(GraphQLRequest request) {
        return byggExecutionResult(request, hentPersonBolkGraphqlSchema);
    }

    ExecutionResult hentGeografiskTilknytning(GraphQLRequest request) {
        return byggExecutionResult(request, hentGeografiskTilknytningGraphqlSchema);
    }

    ExecutionResult hentIdenter(GraphQLRequest request) {
        return byggExecutionResult(request, hentIdenterGraphqlSchema);
    }

    ExecutionResult hentIdenterBolk(GraphQLRequest request) {
        return byggExecutionResult(request, hentIdenterBolkGraphqlSchema);
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


