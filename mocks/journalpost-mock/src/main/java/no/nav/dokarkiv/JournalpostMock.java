package no.nav.dokarkiv;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import no.nav.dokarkiv.generated.model.OpprettJournalpostRequest;
import no.nav.dokarkiv.generated.model.OpprettJournalpostResponse;
import no.nav.foreldrepenger.vtp.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalposttyper;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Dokarkiv"})
@Path("/dokarkiv/rest/journalpostapi/v1")
public class JournalpostMock {
    private static final Logger LOG = LoggerFactory.getLogger(JournalpostMock.class);

    @Context
    JournalRepository journalRepository;

    @POST
    @Path("/journalpost")
    @ApiOperation(value = "lag journalpost", notes = (""))
    public Response lagJournalpost(OpprettJournalpostRequest opprettJournalpostRequest, @QueryParam("foersoekFerdigstill") Boolean foersoekFerdigstill) {
        LOG.info("Dokarkiv. Lag journalpost. foersoekFerdigstill: {}", foersoekFerdigstill);

        JournalpostModell modell = new JournalpostMapper().tilModell(opprettJournalpostRequest);
        String journalpostId = journalRepository.leggTilJournalpost(modell);

        OpprettJournalpostResponse response = new OpprettJournalpostResponse();
        response.setJournalpostId(journalpostId);
        response.setJournalpostferdigstilt(Boolean.TRUE);
        return Response.accepted().entity(response).build();
    }
}
