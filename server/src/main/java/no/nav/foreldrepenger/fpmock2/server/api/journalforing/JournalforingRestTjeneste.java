package no.nav.foreldrepenger.fpmock2.server.api.journalforing;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;

@Api(tags = "Journalføringsmock")
@Path("/api/journalforing")
public class JournalforingRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(JournalforingRestTjeneste.class);


    private static final String DOKUMENTTYYPEID_KEY = "dokumenttypeid";
    private static final String AKTORID_KEY = "fnr";
    private static final String JOURNALPOST_ID = "journalpostid";
    private static final String SAKSNUMMER = "saksnummer";

    @POST
    @Path("/foreldrepengesoknadxml/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Lager en journalpost av type DokumenttypeId (se kilde for gyldige verdier, e.g. I000003). Innhold i journalpost legges ved som body."), response = JournalforingResultatDto.class)
    public JournalforingResultatDto foreldrepengesoknadErketype(String xml, @PathParam(AKTORID_KEY) String fnr, @PathParam(DOKUMENTTYYPEID_KEY) DokumenttypeId dokumenttypeId){



        JournalpostModell journalpostModell = JournalpostModellGenerator.lagJournalpost(xml, fnr, dokumenttypeId);
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();
        String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        LOG.info("Oppretter journalpost for bruke: {}. JournalpostId: {}", fnr, journalpostId);

        JournalforingResultatDto res = new JournalforingResultatDto();
        res.setJournalpostId(journalpostId);
        return res;
    }

    @POST
    @Path("/knyttsaktiljournalpost/journalpostid/{journalpostid}/saksnummer/{saksnummer}")
    public JournalforingResultatDto knyttSakTilJournalpost(@PathParam(JOURNALPOST_ID) String journalpostId, @PathParam(SAKSNUMMER) String saksnummer ){

        LOG.info("Knytter sak: {} til journalpost: {}", saksnummer, journalpostId);

        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();
        JournalpostModell journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId).orElseThrow(()-> new NotFoundException("Kunne ikke finne journalpost"));
        journalpostModell.setSakId(saksnummer);

        JournalforingResultatDto res = new JournalforingResultatDto();
        res.setJournalpostId(journalpostId);
        return res;
    }

}
