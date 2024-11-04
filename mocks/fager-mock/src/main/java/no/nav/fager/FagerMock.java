package no.nav.fager;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.util.Map;

import no.nav.foreldrepenger.vtp.testmodell.repo.ArbeidsgiverPortalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.ArbeidsgiverPortalRepositoryImpl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import no.nav.fager.graphql.GraphQLRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "fager")
@Path("/api/fager")
public class FagerMock {
    private static final Logger LOG = LoggerFactory.getLogger(FagerMock.class);

    private static final String X_CORRELATION_ID = "X-Correlation-ID";
    private static final String NAV_CALLID = "Nav-Callid";
    private static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";
    private final FagerGraphqlTjeneste graphqlTjeneste;

    public FagerMock() {
        this.graphqlTjeneste = FagerGraphqlTjeneste.getInstance(buildArbeidsgiverPortalRepository());
    }

    // Kun for test
    FagerMock(ArbeidsgiverPortalRepository arbeidsgiverPortalRepository) {
        this.graphqlTjeneste = FagerGraphqlTjeneste.getInstance(arbeidsgiverPortalRepository);
    }

    private ArbeidsgiverPortalRepository buildArbeidsgiverPortalRepository() {
        return ArbeidsgiverPortalRepositoryImpl.getInstance();
    }

    @POST
    @Path("/graphql")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Manipulerer saker og oppgaver til Arbeidsgivere")
    public Map<String, Object> graphQLRequest(@HeaderParam(AUTHORIZATION) String authorizationHeader,
                                              @HeaderParam(X_CORRELATION_ID) String xCorrelationId,
                                              @HeaderParam(NAV_CALLID) String navCallid,
                                              @HeaderParam(NAV_CONSUMER_ID) String navConsumerId,
                                              GraphQLRequest request) {
        var operationName = hentOperationName(request);
        LOG.info("FAGER: Operation name: {}", operationName);
        LOG.info("FAGER: Query: {}", request.getQuery());
        if ("nySak".equals(operationName)) {
            LOG.info("FAGER: nySak operation");
            var executionResult = graphqlTjeneste.sak(request);
            return executionResult.toSpecification();
        } else if ("nyOppgave".equals(operationName)) {
            LOG.info("FAGER: nyOppgave operation");
            var executionResult = graphqlTjeneste.oppgave(request);
            return executionResult.toSpecification();
        } else if ("oppgaveUtfoert".equals(operationName)) {
            LOG.info("FAGER: oppgaveUtfoert operation");
            var executionResult = graphqlTjeneste.oppgave(request);
            return executionResult.toSpecification();
        } else if ("oppgaveUtgaat".equals(operationName)) {
            LOG.info("FAGER: oppgaveUtgaat operation");
            var executionResult = graphqlTjeneste.oppgave(request);
            return executionResult.toSpecification();
        } else if ("nyStatusSak".equals(operationName)) {
            LOG.info("FAGER: nyStatusSak operation");
            var executionResult = graphqlTjeneste.sak(request);
            return executionResult.toSpecification();
        } else if ("tilleggsinformasjonSak".equals(operationName)) {
            LOG.info("FAGER: tilleggsinformasjonSak operation");
            var executionResult = graphqlTjeneste.sak(request);
            return executionResult.toSpecification();
        } else if ("hardDeleteSak".equals(operationName)) {
            LOG.info("FAGER: hardDeleteSak operation");
            var executionResult = graphqlTjeneste.sak(request);
            return executionResult.toSpecification();
        }
        throw new NotImplementedException("Operasjon er ikke implementert:" + operationName);
    }

    private String hentOperationName(GraphQLRequest request) {
        var operasjon = StringUtils.substringBetween(request.getQuery(), "{", "(").trim();
        if (operasjon.contains(":")) { // Alias
            return StringUtils.substringAfter(operasjon, ":").trim();
        }
        return operasjon;
    }

}
