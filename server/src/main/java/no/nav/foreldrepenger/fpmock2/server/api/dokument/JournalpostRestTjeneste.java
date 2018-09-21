package no.nav.foreldrepenger.fpmock2.server.api.dokument;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;

@Api(tags = {"Journalposter"})
@Path("/api/journalposter")
public class JournalpostRestTjeneste {

    @Context
    private JournalRepository journalRepository;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Mottar innhold og type for journalpost"), response = JournalpostResponseDto.class)
    public JournalpostResponseDto lagreJournalpost(JournalpostModell request){

        String journalpostId = journalRepository.leggTilJournalpost(request);


        JournalpostResponseDto journalpostResponseDto = new JournalpostResponseDto();
        journalpostResponseDto.setJournalpostId(journalpostId);
        return journalpostResponseDto;

    }


}
