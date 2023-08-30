package no.nav.foreldrepenger.vtp.server.api.journalforing;

import java.time.LocalDateTime;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.server.api.journalforing.hendelse.JournalforingHendelseSender;
import no.nav.foreldrepenger.vtp.testmodell.dokument.JournalpostModellGenerator;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalforingResultatDto;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;

@Tag(name = "Journalføringsmock")
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

    @POST
    @Path("/journalfor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Journalfører journalpost og send en journalføringhendelse på topic", responses = {
        @ApiResponse(responseCode = "OK", content = @Content(schema = @Schema(implementation  = JournalforingResultatDto.class)))
    })
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
    @Operation(description = "Lager en journalpost av type DokumenttypeId (se kilde for gyldige verdier, e.g. I000003). Innhold i journalpost legges ved som body.", responses = {
            @ApiResponse(responseCode = "OK", content = @Content(schema = @Schema(implementation  = JournalforingResultatDto.class)))
    })
    public JournalforingResultatDto journalførDokument(String content, @PathParam(AKTORID_KEY) String fnr, @PathParam(DOKUMENTTYYPEID_KEY) String dokumenttypeId){
        var journalpostModell = JournalpostModellGenerator.lagJournalpostStrukturertDokument(content, fnr, DokumenttypeId.valueOfKode(dokumenttypeId));
        journalpostModell.setMottattDato(LocalDateTime.now());
        var journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        LOG.info("Oppretter journalpost for bruke: {}. JournalpostId: {}", fnr, journalpostId);

        return new JournalforingResultatDto(journalpostId);
    }

    @POST
    @Path("/ustrukturertjournalpost/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}")
    public JournalforingResultatDto lagUstrukturertJournalpost(@PathParam(AKTORID_KEY) String fnr, @PathParam(DOKUMENTTYYPEID_KEY) String dokumenttypeid, @QueryParam(JOURNALSTATUS) String journalstatus){
        var journalpostModell = JournalpostModellGenerator.lagJournalpostUstrukturertDokument(fnr,DokumenttypeId.valueOfKode(dokumenttypeid));
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
