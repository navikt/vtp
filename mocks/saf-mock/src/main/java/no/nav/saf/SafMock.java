package no.nav.saf;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import graphql.ExecutionResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.saf.graphql.GraphQLRequest;
import no.nav.saf.graphql.GraphQLTjeneste;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Map;
import java.util.Optional;

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
    @Path("/hentdokument/{journalpostId}/{dokumentInfoId}/{variantFormat}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter dokument", response = HentDokumentResponse.class)
    public Response hentDokument( @PathParam(JOURNALPOST_ID) String journalpostId,
                                  @PathParam(DOKUMENT_INFO_ID) String dokumentInfoId,
                                  @PathParam(VARIANT_FORMAT) String variantFormat ) throws HentDokumentDokumentIkkeFunnet {
        // TODO(EW): Bruk den gamle responsklassen, HentDokumentResponse.
        HentDokumentResponse dokumentResponse = new HentDokumentResponse();
        Optional<DokumentModell> dokumentModell = journalRepository.finnDokumentMedDokumentId(dokumentInfoId);
        if (dokumentModell.isPresent()) {
            LOG.info("Henter dokument på følgende dokumentId: " + dokumentModell.get().getDokumentId());
            String innhold = dokumentModell.get().getInnhold();
            dokumentResponse.setDokument(innhold.getBytes());
            return Response
                    .status(Response.Status.OK)
                    .entity(dokumentResponse)
                    .build();
        } else {
            throw new HentDokumentDokumentIkkeFunnet("Kunne ikke finne dokument", new DokumentIkkeFunnet());
        }
    }


//    private JournalpostResponseDto konverterTilJournalpostResponseDto(Optional<JournalpostModell> journalpostModell) {
//        //To be implemented.
//        return null;
//    }


}
