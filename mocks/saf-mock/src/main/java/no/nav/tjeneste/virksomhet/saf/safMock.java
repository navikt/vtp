package no.nav.tjeneste.virksomhet.saf;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.tjeneste.virksomhet.saf.modell.JournalpostResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Api(tags = {"saf"})
@Path("/api/saf")
public class safMock {

    private static final Logger LOG = LoggerFactory.getLogger(safMock.class);

    private static final String JOURNALPOST_ID = "journalpostId";
    private static final String DOKUMENT_INFO_ID = "dokumentInfoId";
    private static final String VARIANT_FORMAT = "variantFormat";

    @Context
    private JournalRepository journalRepository;



    @GET
    @Path("/hentdokument/{journalpostId}/{dokumentInfoId}/{variantFormat}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter journalpost", response = JournalpostResponseDto.class)
    public Response hentKjerneJournalpost( @PathParam(JOURNALPOST_ID) String journalpostId,
                                           @PathParam(DOKUMENT_INFO_ID) String dokumentInfoId,
                                           @PathParam(VARIANT_FORMAT) String variantFormat ) {

        Optional<JournalpostModell> journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId);
        if (journalpostModell.isPresent()) {
            LOG.info("Henter journalpost med journalpostId: " + journalpostModell.get().getJournalpostId());
            return Response
                    .status(Response.Status.OK)
                    .entity(konverterTilJournalpostResponseDto(journalpostModell))
                    .build();

        } else {
            LOG.info("Kunne ikke finne journalpost med journalpostId: " + journalpostId);
            return Response.status(Response.Status.NO_CONTENT).build();
        }


    }

    //
    private JournalpostResponseDto konverterTilJournalpostResponseDto(Optional<JournalpostModell> journalpostModell) {
        // TODO-EW To be implemented.
        return null;
    }


}
