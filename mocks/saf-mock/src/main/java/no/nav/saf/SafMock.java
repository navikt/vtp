package no.nav.saf;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.ExecutionResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.saf.graphql.GraphQLRequest;
import no.nav.saf.graphql.GraphQLTjeneste;

@Api(tags = {"saf"})
@Path("/api/saf")
public class SafMock {

    private static final Logger LOG = LoggerFactory.getLogger(SafMock.class);

    private static final String X_CORRELATION_ID = "X-Correlation-ID";
    private static final String NAV_CALLID = "Nav-Callid";
    private static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";

    private static final String JOURNALPOST_ID = "journalpostId";
    private static final String DOKUMENT_INFO_ID = "dokumentInfoId";
    private static final String VARIANT_FORMAT = "variantFormat";

    @Context
    private JournalRepository journalRepository;

    private GraphQLTjeneste graphQLTjeneste;


    public SafMock() {
        this.graphQLTjeneste = GraphQLTjeneste.getInstance();
        this.journalRepository = JournalRepositoryImpl.getInstance();
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

        ExecutionResult executionResult = graphQLTjeneste.executeStatement(request, journalRepository);
        return executionResult.toSpecification();
    }


    @GET
    @Path("/rest/hentdokument/{journalpostId}/{dokumentInfoId}/{variantFormat}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter dokument", response = Response.class)
    public Response hentDokument( @PathParam(JOURNALPOST_ID) String journalpostId,
                                  @PathParam(DOKUMENT_INFO_ID) String dokumentInfoId,
                                  @PathParam(VARIANT_FORMAT) String variantFormat ) {

        Optional<DokumentModell> dokumentModell = journalRepository.finnDokumentMedDokumentId(dokumentInfoId);
        if (dokumentModell.isPresent()) {
            LOG.info("Henter dokument på følgende dokumentId: " + dokumentModell.get().getDokumentId());
            DokumentVariantInnhold dokumentVariantInnhold = dokumentModell.get().getDokumentVariantInnholdListe().stream()
                    .filter(innhold -> innhold.getVariantFormat().getKode().equalsIgnoreCase(variantFormat))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Fant ikke dokument som matchet variantformat: " + variantFormat));
            return Response
                    .status(Response.Status.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + dokumentInfoId + "_" + variantFormat)
                    .entity(dokumentVariantInnhold.getDokumentInnhold())
                    .build();
        } else if (journalpostId != null) { // TODO: Fjern når SafMock ikke er WIP
            try (InputStream is = getClass().getResourceAsStream("/dokumenter/foreldrepenger_soknad.pdf")) {
                return Response
                        .status(Response.Status.OK)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + dokumentInfoId + "_" + variantFormat)
                        .entity(is.readAllBytes())
                        .build();
            } catch (IOException e) {
                throw new RuntimeException(String.format("Kunne ikke lese dummyrespons for " +
                        "journalpostId=%s, dokumentInfoId=%s, variantFormat=%s", journalpostId, dokumentInfoId, variantFormat));
            }

        } else {
            throw new RuntimeException(String.format("Kunne ikke finne dokument for " +
                    "journalpostId=%s, dokumentInfoId=%s, variantFormat=%s", journalpostId, dokumentInfoId, variantFormat));
        }
    }
}
