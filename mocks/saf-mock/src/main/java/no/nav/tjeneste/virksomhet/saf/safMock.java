package no.nav.tjeneste.virksomhet.saf;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.feil.DokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
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


    @POST
    @Path("/graphql")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter journalpost")
    public Response hentJournalpostListe() {
        // TODO - Må implementeres.
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    @GET
    @Path("/hentdokument/{journalpostId}/{dokumentInfoId}/{variantFormat}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter dokument", response = HentDokumentResponse.class)
    public Response hentDokument( @PathParam(JOURNALPOST_ID) String journalpostId,
                                  @PathParam(DOKUMENT_INFO_ID) String dokumentInfoId,
                                  @PathParam(VARIANT_FORMAT) String variantFormat ) throws HentDokumentDokumentIkkeFunnet {

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
