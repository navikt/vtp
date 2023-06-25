package no.nav.pdl;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pdl.graphql.GraphQLRequest;

@Tag(name = "pdl")
@Path("/api/pdl")
public class PdlMock {
    private static final String X_CORRELATION_ID = "X-Correlation-ID";
    private static final String NAV_CALLID = "Nav-Callid";
    private static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";
    private final PdlGraphqlTjeneste graphqlTjeneste;

    public PdlMock() {
        this.graphqlTjeneste = PdlGraphqlTjeneste.getInstance(buildTestscenarioRepository());
    }

    // Kun for test
    PdlMock(TestscenarioBuilderRepository scenarioBuilderRepository) {
        this.graphqlTjeneste = PdlGraphqlTjeneste.getInstance(scenarioBuilderRepository);
    }

    private TestscenarioBuilderRepository buildTestscenarioRepository() {
        return TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }

    @POST
    @Path("/graphql")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter journalpost")
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
        if ("hentGeografiskTilknytning".equals(operationName)) {
            var executionResult = graphqlTjeneste.hentGeografiskTilknytning(request);
            return executionResult.toSpecification();
        }
        if ("hentIdenter".equals(operationName)) {
            var executionResult = graphqlTjeneste.hentIdenter(request);
            return executionResult.toSpecification();
        }
        if ("hentIdenterBolk".equals(operationName)) {
            var executionResult = graphqlTjeneste.hentIdenterBolk(request);
            return executionResult.toSpecification();
        }
        if ("hentPersonBolk".equals(operationName)) {
            var executionResult = graphqlTjeneste.hentPersonBolk(request);
            return executionResult.toSpecification();
        }
        throw new NotImplementedException("Operasjon er ikke implementert:" + operationName);
    }

    // TODO: Det er ikke støtte for å gjøre flere operasjoner på en query (f.eks. hentPerson og hentGeografiskTilknytning samtidig)
    private String hentOperationName(GraphQLRequest request) {
        var operasjon = StringUtils.substringBetween(request.getQuery(), "{", "(").trim();
        if (operasjon.contains(":")) { // Alias
            return StringUtils.substringAfter(operasjon, ":").trim();
        }
        return operasjon;
    }

}
