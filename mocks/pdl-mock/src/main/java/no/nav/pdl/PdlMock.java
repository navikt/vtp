package no.nav.pdl;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pdl.graphql.GraphQLRequest;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = {"pdl"})
@Path("/api/pdl")
public class PdlMock {

    private static final Logger LOG = LoggerFactory.getLogger(PdlMock.class);

    private static final String X_CORRELATION_ID = "X-Correlation-ID";
    private static final String NAV_CALLID = "Nav-Callid";
    private static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";

    private PdlGraphqlTjeneste graphqlTjeneste;

    public PdlMock() {
        this.graphqlTjeneste = PdlGraphqlTjeneste.getInstance(buildTestscenarioRepository());
    }

    // Kun for test
    public PdlMock(TestscenarioBuilderRepository scenarioBuilderRepository) {
        this.graphqlTjeneste = PdlGraphqlTjeneste.getInstance(scenarioBuilderRepository);
    }

    private TestscenarioBuilderRepository buildTestscenarioRepository() {
        try {
            return TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/graphql")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter journalpost")
    public Map<String, Object> graphQLRequest(@HeaderParam(AUTHORIZATION) String authorizationHeader,
                                              @HeaderParam(X_CORRELATION_ID) String xCorrelationId,
                                              @HeaderParam(NAV_CALLID) String navCallid,
                                              @HeaderParam(NAV_CONSUMER_ID) String navConsumerId,
                                              GraphQLRequest request) {

        var operationName = hentOperationName(request);
        if ("hentPerson".equals(operationName)) {
            var executionResult = graphqlTjeneste.hentPerson(request);
            return executionResult.toSpecification();
        }
        if ("hentIdenter".equals(operationName)) {
            var executionResult = graphqlTjeneste.hentIdenter(request);
            return executionResult.toSpecification();
        }
        throw new NotImplementedException("Operasjon er ikke implementert:" + operationName);
    }

    private String hentOperationName(GraphQLRequest request) {
        // Forventet format: 'query { operationName(key1: value1, ...) {...} }'
        var wordArray = request.getQuery().split("\\W");
        var words = List.of(wordArray).stream()
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());
        if (!"query".equals(words.get(0)) || words.size() <= 1) {
            throw new IllegalArgumentException("GraphQLRequest-query kan ikke gjenkjennes. Er ikke på format 'query { operationName(key1: value1, ...) {...}'");
        }
        return words.get(1);
    }

}
