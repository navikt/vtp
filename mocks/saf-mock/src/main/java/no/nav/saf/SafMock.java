package no.nav.saf;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.ExecutionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.saf.graphql.GraphQLRequest;
import no.nav.saf.graphql.GraphQLTjeneste;

@Tag(name = "saf")
@Path("/api/saf")
public class SafMock {

    private static final Logger LOG = LoggerFactory.getLogger(SafMock.class);

    private static final String X_CORRELATION_ID = "X-Correlation-ID";
    private static final String NAV_CALLID = "Nav-Callid";
    private static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";

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
    @Operation(description = "Henter journalpost")
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
    @Operation(description = "Henter dokument")
    public Response hentDokument(@PathParam("journalpostId") String journalpostId,
                                 @PathParam("dokumentInfoId") String dokumentInfoId,
                                 @PathParam("variantFormat") String variantFormat ) {
        Optional<DokumentModell> dokumentModell = journalRepository.finnDokumentMedDokumentId(dokumentInfoId);
        if (dokumentModell.isPresent()) {
            LOG.info("Henter dokument på følgende dokumentId: {}", dokumentModell.get().getDokumentId());
            var dokumentinnhold = dokumentModell.get().getDokumentVariantInnholdListe().stream()
                    .filter(innhold -> innhold.getVariantFormat().getKode().equalsIgnoreCase(variantFormat))
                    .map(DokumentVariantInnhold::getDokumentInnhold)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Fant ikke dokument som matchet variantformat: " + variantFormat));
            return Response
                    .status(Response.Status.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + dokumentInfoId + "_" + variantFormat + ".pdf")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .entity(dokumentinnhold)
                    .build();
        } else {
            throw new RuntimeException(String.format("Kunne ikke finne dokument for " +
                    "journalpostId=%s, dokumentInfoId=%s, variantFormat=%s", journalpostId, dokumentInfoId, variantFormat));
        }
    }
}
