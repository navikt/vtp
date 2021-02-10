package no.nav.dokarkiv;

import java.util.Collections;
import java.util.stream.Collectors;

import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.dokarkiv.generated.model.DokumentInfo;
import no.nav.dokarkiv.generated.model.FerdigstillJournalpostRequest;
import no.nav.dokarkiv.generated.model.OppdaterJournalpostRequest;
import no.nav.dokarkiv.generated.model.OppdaterJournalpostResponse;
import no.nav.dokarkiv.generated.model.OpprettJournalpostRequest;
import no.nav.dokarkiv.generated.model.OpprettJournalpostResponse;
import no.nav.dokarkiv.generated.model.TilknyttVedleggRequest;
import no.nav.dokarkiv.generated.model.TilknyttVedleggResponse;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;

@Api(tags = {"Dokarkiv"})
@Path("/dokarkiv/rest/journalpostapi/v1")
public class JournalpostMock {
    private static final Logger LOG = LoggerFactory.getLogger(JournalpostMock.class);
    private static final JournalpostMapper journalpostMapper = JournalpostMapper.getInstance();

    @Context
    JournalRepository journalRepository;

    @POST
    @Path("/journalpost")
    @ApiOperation(value = "lag journalpost")
    public Response lagJournalpost(OpprettJournalpostRequest opprettJournalpostRequest, @QueryParam("forsoekFerdigstill") Boolean forsoekFerdigstill) {
        LOG.info("Dokarkiv. Lag journalpost. foersoekFerdigstill: {}", forsoekFerdigstill);

        var modell = journalpostMapper.tilModell(opprettJournalpostRequest);
        var journalpostId = journalRepository.leggTilJournalpost(modell);

        var journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId)
                .orElseThrow();

        var dokumentInfos = journalpostModell.getDokumentModellList().stream()
                .map(it -> {
                    DokumentInfo dokinfo = new DokumentInfo();
                    dokinfo.setDokumentInfoId(it.getDokumentId());
                    return dokinfo;
                }).collect(Collectors.toList());

        var response = new OpprettJournalpostResponse();
        response.setDokumenter(dokumentInfos);
        response.setJournalpostId(journalpostId);
        response.setJournalpostferdigstilt(Boolean.TRUE);
        return Response.accepted().entity(response).build();
    }


    @PUT
    @Path("/journalpost/{journalpostid}")
    @ApiOperation(value = "Oppdater journalpost")
    public Response oppdaterJournalpost(OppdaterJournalpostRequest oppdaterJournalpostRequest, @PathParam("journalpostid") String journalpostId){

        LOG.info("Kall til oppdater journalpost: {}", journalpostId);
        var journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId);
        if(journalpostModell.isPresent()) {
            journalpostMapper.oppdaterJournalpost(oppdaterJournalpostRequest, journalpostModell.get());
        } else {
            LOG.info("Journalpost med journalpostId {} eksistere ikke!", journalpostId);
        }
        var oppdaterJournalpostResponse = new OppdaterJournalpostResponse();
        oppdaterJournalpostResponse.setJournalpostId(journalpostId);
        return Response.accepted().entity(oppdaterJournalpostResponse).build();
    }

    @PATCH
    @Path("/journalpost/{journalpostid}/ferdigstill")
    @ApiOperation(value = "Ferdigstill journalpost")
    public Response ferdigstillJournalpost(FerdigstillJournalpostRequest ferdigstillJournalpostRequest){

        var journalfoerendeEnhet = ferdigstillJournalpostRequest.getJournalfoerendeEnhet();
        LOG.info("Kall til ferdigstill journalpost på enhet: {}", journalfoerendeEnhet);

        return Response.ok().entity("OK").build();
    }

    @PUT
    @Path("/journalpost/{journalpostid}/tilknyttVedlegg")
    @ApiOperation(value = "Tilknytt vedlegg")
    public TilknyttVedleggResponse tilknyttVedlegg(TilknyttVedleggRequest tilknyttVedleggRequest){

        var response = new TilknyttVedleggResponse();

        response.setFeiledeDokumenter(Collections.emptyList());

        LOG.info("Kall til tilknyttet vedlegg for dokumenter {}", tilknyttVedleggRequest.getDokument());

        return response;
    }
}
