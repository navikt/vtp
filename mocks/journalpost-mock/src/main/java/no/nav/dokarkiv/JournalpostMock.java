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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.dokarkiv.generated.model.DokumentInfo;
import no.nav.dokarkiv.generated.model.FerdigstillJournalpostRequest;
import no.nav.dokarkiv.generated.model.OppdaterJournalpostRequest;
import no.nav.dokarkiv.generated.model.OppdaterJournalpostResponse;
import no.nav.dokarkiv.generated.model.OpprettJournalpostRequest;
import no.nav.dokarkiv.generated.model.OpprettJournalpostResponse;
import no.nav.dokarkiv.generated.model.TilknyttVedleggRequest;
import no.nav.dokarkiv.generated.model.TilknyttVedleggResponse;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;

// Ref: https://confluence.adeo.no/display/BOA/Oversikt+over+Joark-+og+dokarkiv-tjenester
@Tag(name = "Dokarkiv")
@Path("/dokarkiv/rest/journalpostapi/v1")
public class JournalpostMock {
    private static final Logger LOG = LoggerFactory.getLogger(JournalpostMock.class);
    private static final JournalpostMapper journalpostMapper = JournalpostMapper.getInstance();

    @Context
    JournalRepository journalRepository;

    @POST
    @Path("/journalpost")
    @Operation(description = "lag journalpost")
    public Response lagJournalpost(OpprettJournalpostRequest opprettJournalpostRequest, @QueryParam("forsoekFerdigstill") Boolean forsoekFerdigstill) {
        LOG.info("Dokarkiv. Lag journalpost. foersoekFerdigstill: {}", forsoekFerdigstill);

        var modell = journalpostMapper.tilModell(opprettJournalpostRequest);
        var journalpostId = journalRepository.leggTilJournalpost(modell);

        var journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId)
                .orElseThrow();

        OpprettJournalpostResponse response = tilOpprettJouralpostResponse(journalpostModell);
        return Response.accepted().entity(response).build();
    }


    @PUT
    @Path("/journalpost/{journalpostid}")
    @Operation(description = "Oppdater journalpost")
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
    @Operation(description = "Ferdigstill journalpost")
    public Response ferdigstillJournalpost(FerdigstillJournalpostRequest ferdigstillJournalpostRequest){

        var journalfoerendeEnhet = ferdigstillJournalpostRequest.getJournalfoerendeEnhet();
        LOG.info("Kall til ferdigstill journalpost på enhet: {}", journalfoerendeEnhet);

        return Response.ok().entity("OK").build();
    }

    @PUT
    @Path("/journalpost/{journalpostid}/tilknyttVedlegg")
    @Operation(description = "Tilknytt vedlegg")
    public TilknyttVedleggResponse tilknyttVedlegg(TilknyttVedleggRequest tilknyttVedleggRequest){

        var response = new TilknyttVedleggResponse();

        response.setFeiledeDokumenter(Collections.emptyList());

        LOG.info("Kall til tilknyttet vedlegg for dokumenter {}", tilknyttVedleggRequest.getDokument());

        return response;
    }


    private OpprettJournalpostResponse tilOpprettJouralpostResponse(JournalpostModell journalpostModell) {
        var dokumentInfos = journalpostModell.getDokumentModellList().stream()
                .map(it -> {
                    DokumentInfo dokinfo = new DokumentInfo();
                    dokinfo.setBrevkode(it.getBrevkode());
                    dokinfo.setDokumentInfoId(it.getDokumentId());
                    dokinfo.setTittel(it.getTittel());
                    return dokinfo;
                }).collect(Collectors.toList());

        var response = new OpprettJournalpostResponse();
        response.setDokumenter(dokumentInfos);
        response.setJournalpostId(journalpostModell.getJournalpostId());
        response.setJournalpostferdigstilt(Boolean.TRUE);
        return response;
    }
}
