package no.nav.dokarkiv;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import no.nav.dokarkiv.dto.FerdigstillJournalpostRequest;
import no.nav.dokarkiv.dto.OppdaterJournalpostRequest;
import no.nav.dokarkiv.dto.OppdaterJournalpostResponse;
import no.nav.dokarkiv.dto.OpprettJournalpostRequest;
import no.nav.dokarkiv.dto.OpprettJournalpostResponse;
import no.nav.dokarkiv.dto.TilknyttVedleggRequest;
import no.nav.dokarkiv.dto.TilknyttVedleggResponse;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Mottakskanal;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;

// Ref: https://confluence.adeo.no/display/BOA/Oversikt+over+Joark-+og+dokarkiv-tjenester
@Tag(name = "Dokarkiv")
@Path("/dokarkiv/rest/journalpostapi/v1")
public class JournalpostMock {
    private static final Logger LOG = LoggerFactory.getLogger(JournalpostMock.class);
    private static final JournalpostMapper journalpostMapper = JournalpostMapper.getInstance();

    @Context
    JournalRepository journalRepository;

    @Context
    private LocalKafkaProducer localKafkaProducer;


    @POST
    @Path("/journalpost")
    @Operation(description = "lag journalpost")
    public Response lagJournalpost(OpprettJournalpostRequest opprettJournalpostRequest,
                                   @QueryParam("forsoekFerdigstill") Boolean forsoekFerdigstill) {
        LOG.info("Dokarkiv. Lag journalpost. foersoekFerdigstill: {}", forsoekFerdigstill);

        var modell = journalpostMapper.tilModell(opprettJournalpostRequest);
        var journalpostId = journalRepository.leggTilJournalpost(modell);

        var journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId).orElseThrow();

        if (skalSendeJournalføringHendelse(journalpostModell)) {
            opprettJournalføringsHendelse(journalpostModell, Journalstatus.MOTTATT);
        }

        var response = tilOpprettJouralpostResponse(journalpostModell, forsoekFerdigstill);
        return Response.accepted().entity(response).build();
    }

    private boolean skalSendeJournalføringHendelse(JournalpostModell journalpostModell) {
        if (!Mottakskanal.NAV_NO.equals(journalpostModell.getMottakskanal())) {
            return false;
        }

        String tittel = journalpostModell.getTittel();
        return "Inntektsmelding".equals(tittel) || tittel.toLowerCase().contains("ungdomsprogramytelse");
    }

    private void opprettJournalføringsHendelse(JournalpostModell journalpostModell, Journalstatus journalStatus) {
        journalpostModell.setJournalStatus(journalStatus);

        var journalforingHendelseSender = new JournalforingHendelseSender(localKafkaProducer);
        journalforingHendelseSender.leggTilJournalføringHendelsePåKafka(journalpostModell);
    }


    @PUT
    @Path("/journalpost/{journalpostid}")
    @Operation(description = "Oppdater journalpost")
    public Response oppdaterJournalpost(OppdaterJournalpostRequest oppdaterJournalpostRequest,
                                        @PathParam("journalpostid") String journalpostId) {

        LOG.info("Kall til oppdater journalpost: {}", journalpostId);
        var journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId);
        if (journalpostModell.isPresent()) {
            journalpostMapper.oppdaterJournalpost(oppdaterJournalpostRequest, journalpostModell.get());
        } else {
            LOG.info("Journalpost med journalpostId {} eksistere ikke!", journalpostId);
        }
        var oppdaterJournalpostResponse = new OppdaterJournalpostResponse(journalpostId);
        return Response.ok().entity(oppdaterJournalpostResponse).build();
    }

    @PATCH
    @Path("/journalpost/{journalpostid}/ferdigstill")
    @Operation(description = "Ferdigstill journalpost")
    public Response ferdigstillJournalpost(FerdigstillJournalpostRequest ferdigstillJournalpostRequest,
                                           @PathParam("journalpostid") String journalpostId) {

        var journalfoerendeEnhet = ferdigstillJournalpostRequest.journalfoerendeEnhet();
        LOG.info("Kall til ferdigstill journalpost på enhet: {}", journalfoerendeEnhet);

        journalRepository.finnJournalpostMedJournalpostId(journalpostId)
                .ifPresent(j -> j.setJournalStatus(Journalstatus.JOURNALFØRT));

        return Response.ok().entity("OK").build();
    }

    @PUT
    @Path("/journalpost/{journalpostid}/tilknyttVedlegg")
    @Operation(description = "Tilknytt vedlegg")
    public TilknyttVedleggResponse tilknyttVedlegg(TilknyttVedleggRequest tilknyttVedleggRequest) {

        LOG.info("Kall til tilknyttet vedlegg for dokumenter {}", tilknyttVedleggRequest.dokument());

        return new TilknyttVedleggResponse(List.of());
    }


    private OpprettJournalpostResponse tilOpprettJouralpostResponse(JournalpostModell journalpostModell, Boolean forsoekFerdigstill) {
        var docs = journalpostModell.getDokumentModellList().stream()
                .map(it -> new OpprettJournalpostResponse.DokumentInfoResponse(it.getDokumentId()))
                .toList();
        return new OpprettJournalpostResponse(journalpostModell.getJournalpostId(),
                forsoekFerdigstill != null && forsoekFerdigstill, docs);
    }
}
