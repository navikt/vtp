package no.nav.saf.graphql;

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

import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;

import no.nav.saf.generatedsources.Arkivsaksystem;
import no.nav.saf.generatedsources.Journalpost;
import no.nav.saf.generatedsources.Sak;

import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Collections;
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
        String journalpostId = (String) request.getVariables().getOrDefault("journalpostId", "87654321");

        RuntimeWiring runtimeWiring = DokumentWiring.lagRuntimeWiring(opprettJournalpostFeed(journalpostId, testscenarioRepository));
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        return GraphQL.newGraphQL(graphQLSchema)
                .mutationExecutionStrategy(new AsyncSerialExecutionStrategy(new SimpleDataFetcherExceptionHandler()))
                .queryExecutionStrategy(new AsyncExecutionStrategy(new SimpleDataFetcherExceptionHandler()))
                .build().execute(ExecutionInput.newExecutionInput()
                .query(request.getQuery())
                .operationName(request.getOperationName())
                .variables(request.getVariables() == null ? Collections.emptyMap() : request.getVariables())
                .build());
    }

    private JournalpostCoordinator opprettJournalpostFeed(String journalpostId, JournalRepository journalRepository) {

        //TODO: legg inn logikk for å bygge hentJournalpost-response

        return new JournalpostCoordinator() {
            @Override
            public Journalpost hentJournalpost(String journalpostId) {
                return new Journalpost.Builder()
                        .withJournalpostId("12345678")
                        .withDatoOpprettet(LocalDateTime.now())
                        .withSak(new Sak.Builder()
                                .withArkivsaksystem(Arkivsaksystem.GSAK)
                                .withArkivsaksnummer("123")
                                .withDatoOpprettet(LocalDateTime.now()).build())
                        .build();
            }
        };
    }

}



//    DataFetcher productsDataFetcher = new DataFetcher<List<ProductDTO>>() {
//        @Override
//        public List<ProductDTO> get(DataFetchingEnvironment environment) {
//            List<ProductDTO> products = null;
//            ProductDTO product1 = new ProductDTO();
//            product1.setId(2);
//            product1.setName("Espen");
//            product1.setDescription("En gutt");
//            product1.setCost(20.0);
//
//            System.out.println(product1);
//            products.add(product1);
//
//            return products;
//        }
//    };
//
//    RuntimeWiring runtimeWiring = newRuntimeWiring()
//            .type("Query1", typeWire -> typeWire
//                    .dataFetcher("products", productsDataFetcher)
//            )
//            .build();
