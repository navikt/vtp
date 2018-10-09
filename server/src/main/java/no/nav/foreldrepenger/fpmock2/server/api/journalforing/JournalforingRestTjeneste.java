package no.nav.foreldrepenger.fpmock2.server.api.journalforing;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(tags = "Journalføringsmock")
@Path("/api/journalforing")
public class JournalforingRestTjeneste {

    private static final String DOKUMENTTYYPE_KEY = "dokumenttype";
    private static final String DOKUMENTTYYPEID_KEY = "dokumenttypeid";
    private static final String AKTORID_KEY = "fnr";

    @POST
    @Path("/foreldrepengesoknadxml/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}/dokumenttype/{dokumenttype}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Journalfører en foreldrepengesøknad basert på erketype. XML postes som body."), response = JournalforingResultatDto.class)
    public JournalforingResultatDto foreldrepengesoknadErketype(String xml, @PathParam(AKTORID_KEY) String fnr, @PathParam(DOKUMENTTYYPE_KEY) DokumentTilknyttetJournalpost dokumenttype, @PathParam(DOKUMENTTYYPEID_KEY) DokumenttypeId dokumenttypeId){

        JournalpostModell journalpostModell = JournalpostModellGenerator.lagJournalpost(xml, fnr, dokumenttype, dokumenttypeId);
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();
        String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        JournalforingResultatDto res = new JournalforingResultatDto();
        res.setJournalpostId(journalpostId);
        return res;
    }

}
