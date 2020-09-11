package no.nav.pdl;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.pdl.graphql.GraphQLRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

@Api(tags = {"pdl"})
@Path("/api/pdl")
public class PdlMock {

    private static final Logger LOG = LoggerFactory.getLogger(PdlMock.class);

    private static final String X_CORRELATION_ID = "X-Correlation-ID";
    private static final String NAV_CALLID = "Nav-Callid";
    private static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";

    private static final String JOURNALPOST_ID = "journalpostId";
    private static final String DOKUMENT_INFO_ID = "dokumentInfoId";
    private static final String VARIANT_FORMAT = "variantFormat";

    @Context
    private JournalRepository journalRepository;

    @Context
    private TestscenarioBuilderRepository scenarioRepository;

    private PersonServiceMockImpl personServiceMock;

    public PdlMock() {
        this.personServiceMock = new PersonServiceMockImpl(scenarioRepository);
    }

    public PdlMock(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
        this.personServiceMock = new PersonServiceMockImpl(scenarioRepository);
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
        if ("hentPerson".equals(request.getOperationName())) {
            var ident = (String) request.getVariables().get("ident");
            return personServiceMock.hentPerson(ident);
        }
        return Map.of();
    }

}
