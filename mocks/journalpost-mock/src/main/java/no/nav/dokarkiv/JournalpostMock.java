package no.nav.dokarkiv;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.dokarkiv.generated.model.*;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = {"Dokarkiv"})
@Path("/dokarkiv/rest/journalpostapi/v1")
public class JournalpostMock {
    private static final Logger LOG = LoggerFactory.getLogger(JournalpostMock.class);

    @Context
    JournalRepository journalRepository;

    @POST
    @Path("/journalpost")
    @ApiOperation(value = "lag journalpost", notes = (""))
    public Response lagJournalpost(OpprettJournalpostRequest opprettJournalpostRequest, @QueryParam("forsoekFerdigstill") Boolean forsoekFerdigstill) {
        LOG.info("Dokarkiv. Lag journalpost. foersoekFerdigstill: {}", forsoekFerdigstill);

        JournalpostModell modell = new JournalpostMapper().tilModell(opprettJournalpostRequest);
        String journalpostId = journalRepository.leggTilJournalpost(modell);

        Optional<JournalpostModell> journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId);

        List<DokumentInfo> dokumentInfos = journalpostModell.get().getDokumentModellList().stream()
                .map(it -> {
                    DokumentInfo dokinfo = new DokumentInfo();
                    dokinfo.setDokumentInfoId(it.getDokumentId());
                    return dokinfo;
                }).collect(Collectors.toList());

        OpprettJournalpostResponse response = new OpprettJournalpostResponse();
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
        Optional<JournalpostModell> journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId);
        if(journalpostModell.isPresent()) {
            journalpostModell.get().setSakId(oppdaterJournalpostRequest.getSak().getFagsakId());
            journalpostModell.get().setBruker(new JournalpostMapper().mapAvsenderFraBruker(oppdaterJournalpostRequest.getBruker()));
        }
        OppdaterJournalpostResponse oppdaterJournalpostResponse = new OppdaterJournalpostResponse();
        oppdaterJournalpostResponse.setJournalpostId(journalpostId);

        return Response.accepted().entity(oppdaterJournalpostResponse).build();
    }

    @PATCH
    @Path("/journalpost/{journalpostid}/ferdigstill")
    @ApiOperation(value = "Ferdigstill journalpost")
    public Response ferdigstillJournalpost(FerdigstillJournalpostRequest ferdigstillJournalpostRequest){

        String journalfoerendeEnhet = ferdigstillJournalpostRequest.getJournalfoerendeEnhet();
        LOG.info("Kall til ferdigstill journalpost på enhet: {}", journalfoerendeEnhet);

        return Response.ok().entity("OK").build();
    }







}
