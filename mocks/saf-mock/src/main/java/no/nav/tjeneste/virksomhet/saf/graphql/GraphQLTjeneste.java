package no.nav.tjeneste.virksomhet.saf.graphql;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.InputStreamReader;
import java.util.Objects;

public class GraphQLTjeneste {

    private static final String SCHEME_PATH = "schemas/saf.graphqls";

    private GraphQL executor;
    private DokumentWiring dokumentWiring;

    public void init() {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();

        InputStreamReader schema = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(SCHEME_PATH)));
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = DokumentWiring.lagRuntimeWiring();

        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        executor = GraphQL.newGraphQL(graphQLSchema).build();

    }

    public ExecutionResult executeStatement(String jsonRequest) {
        return executor.execute(jsonRequest);
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
