package no.nav.foreldrepenger.vtp.server.api.journalforing;

import java.time.LocalDateTime;

import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.server.api.journalforing.hendelse.JournalforingHendelseSender;
import no.nav.foreldrepenger.vtp.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;

@Api(tags = "Journalføringsmock")
@Path("/api/journalforing")
public class JournalforingRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(JournalforingRestTjeneste.class);

    private static final String DOKUMENTTYYPEID_KEY = "dokumenttypeid";
    private static final String AKTORID_KEY = "fnr";
    private static final String JOURNALPOST_ID = "journalpostid";
    private static final String SAKSNUMMER = "saksnummer";
    private static final String JOURNALSTATUS = "journalstatus";

    @Context
    private LocalKafkaProducer localKafkaProducer;

    @Context
    private JournalRepository journalRepository;

    /** @deprecated Gammel innsending tjeneste - men tar ikke bare søknader men også inntektsmeldinger etc.  */
    @Deprecated(forRemoval=true)
    @POST
    @Path("/foreldrepengesoknadxml/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Lager en journalpost av type DokumenttypeId (se kilde for gyldige verdier, e.g. I000003). Innhold i journalpost legges ved som body."), response = JournalforingResultatDto.class)
    public JournalforingResultatDto foreldrepengesoknadErketype(String xml, @PathParam(AKTORID_KEY) String fnr, @PathParam(DOKUMENTTYYPEID_KEY) DokumenttypeId dokumenttypeId){

        var journalpostModell = JournalpostModellGenerator.lagJournalpostStrukturertDokument(xml, fnr, dokumenttypeId);
        journalpostModell.setMottattDato(LocalDateTime.now());
        String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        LOG.info("Oppretter journalpost for bruke: {}. JournalpostId: {}", fnr, journalpostId);

        return new JournalforingResultatDto(journalpostId);
    }

    @POST
    @Path("/journalfor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Journalfører journalpost og send en journalføringhendelse på topic"), response = JournalforingResultatDto.class)
    public JournalforingResultatDto journalførJournalpost(JournalpostModell journalpostModell){
        var journalpostId = journalRepository.leggTilJournalpost(journalpostModell);
        LOG.info("Oppretter journalpost for bruker: {}, JournalpostId: {}", journalpostModell.getAvsenderFnr(), journalpostId);
        var instansiertJournalpostModell = journalRepository
                .finnJournalpostMedJournalpostId(journalpostId)
                .orElseThrow();

        var journalforingHendelseSender = new JournalforingHendelseSender(localKafkaProducer);
        journalforingHendelseSender.leggTilJournalføringHendelsePåKafka(instansiertJournalpostModell);

        return new JournalforingResultatDto(journalpostId);
    }

    @POST
    @Path("/journalfor/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Lager en journalpost av type DokumenttypeId (se kilde for gyldige verdier, e.g. I000003). Innhold i journalpost legges ved som body."), response = JournalforingResultatDto.class)
    public JournalforingResultatDto journalførDokument(String content, @PathParam(AKTORID_KEY) String fnr, @PathParam(DOKUMENTTYYPEID_KEY) DokumenttypeId dokumenttypeId){
        var journalpostModell = JournalpostModellGenerator.lagJournalpostStrukturertDokument(content, fnr, dokumenttypeId);
        journalpostModell.setMottattDato(LocalDateTime.now());
        var journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        LOG.info("Oppretter journalpost for bruke: {}. JournalpostId: {}", fnr, journalpostId);

        return new JournalforingResultatDto(journalpostId);
    }

    @POST
    @Path("/ustrukturertjournalpost/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}")
    public JournalforingResultatDto lagUstrukturertJournalpost(@PathParam(AKTORID_KEY) String fnr, @PathParam(DOKUMENTTYYPEID_KEY) DokumenttypeId dokumenttypeid, @QueryParam(JOURNALSTATUS) String journalstatus){
        var journalpostModell = JournalpostModellGenerator.lagJournalpostUstrukturertDokument(fnr,dokumenttypeid);
        if(journalstatus != null && journalstatus.length() > 0){
            var status = new Journalstatus(journalstatus);
            journalpostModell.setJournalStatus(status);
        }

        var journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        LOG.info("Oppretter journalpost for bruker: {}. JournalpostId: {}", fnr, journalpostId);

        return new JournalforingResultatDto(journalpostId);
    }


    @POST
    @Path("/knyttsaktiljournalpost/journalpostid/{journalpostid}/saksnummer/{saksnummer}")
    public JournalforingResultatDto knyttSakTilJournalpost(@PathParam(JOURNALPOST_ID) String journalpostId, @PathParam(SAKSNUMMER) String saksnummer ){

        LOG.info("Knytter sak: {} til journalpost: {}", saksnummer, journalpostId);

        var journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId).orElseThrow(()-> new NotFoundException("Kunne ikke finne journalpost"));
        journalpostModell.setSakId(saksnummer);

        return new JournalforingResultatDto(journalpostId);
    }

}
