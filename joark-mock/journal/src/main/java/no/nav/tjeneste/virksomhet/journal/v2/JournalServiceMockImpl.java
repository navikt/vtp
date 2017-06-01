package no.nav.tjeneste.virksomhet.journal.v2;

import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentURLDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentURLSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentJournalpostListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.JournalV2;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDbLeser;
import no.nav.tjeneste.virksomhet.journal.v2.modell.JournalpostBygger;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDokument;
import no.nav.tjeneste.virksomhet.journal.v2.feil.DokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentResponse;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentURLRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentURLResponse;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentJournalpostListeRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentJournalpostListeResponse;
import no.nav.tjeneste.virksomhet.journal.v2.modell.StaticModelData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;

@WebService(name = "Journal_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2")
public class JournalServiceMockImpl implements JournalV2 {
    private static final Logger LOG = LoggerFactory.getLogger(JournalServiceMockImpl.class);

    private static final EntityManager joarkEntityManager = Persistence.createEntityManagerFactory("joark").createEntityManager();

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentJournalpostListeRequest")
    @WebResult(name = "Response", targetNamespace = "")
    @RequestWrapper(localName = "hentJournalpostListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentJournalpostListe")
    @ResponseWrapper(localName = "hentJournalpostListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentJournalpostListeResponse")
    @Override
    public HentJournalpostListeResponse hentJournalpostListe(@WebParam(name = "Request", targetNamespace = "") HentJournalpostListeRequest request)
            throws HentJournalpostListeSikkerhetsbegrensning {
        HentJournalpostListeResponse response = new HentJournalpostListeResponse();
        Optional<String> funnetsaksnr = request.getSakListe().stream().map(sak -> sak.getSakId()).findFirst();

        if (!funnetsaksnr.isPresent()) {
            LOG.info("Fant ingen saksnr i request.");
            return response;
        }

        List<Journalpost> journalposter = null;

        List<JournalDokument> journalpost = new JournalDbLeser(joarkEntityManager).finnJournalposterMedSakId(funnetsaksnr.get());
        if (journalpost != null) {
            for (JournalDokument jd : journalpost) {
                JournalpostBygger jb = new JournalpostBygger(jd);
                response.getJournalpostListe().add(jb.ByggJournalpost());
            }
            return response;
        }

        String saksnr = funnetsaksnr.get();
        journalposter = StaticModelData.getJournalposterForFagsak(saksnr);
        if (journalposter == null) {
            LOG.info("Fant ingen matchende journalpost for saksnr = " + saksnr);
            return response;
        }
        response.getJournalpostListe().addAll(journalposter);
        LOG.info("Sender HentJournalpostListeResponse med 1 Journalpost for saksnr = " + saksnr);
        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentDokumentRequest")
    @WebResult(name = "Response", targetNamespace = "")
    @RequestWrapper(localName = "hentDokument", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokument")
    @ResponseWrapper(localName = "hentDokumentResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentResponse")
    @Override
    public HentDokumentResponse hentDokument(@WebParam(name = "Request", targetNamespace = "") HentDokumentRequest request)
            throws HentDokumentDokumentIkkeFunnet, HentDokumentSikkerhetsbegrensning {
        HentDokumentResponse hentDokumentResponse = new HentDokumentResponse();
        if (request.getJournalpostId() == null || request.getDokumentId() == null || request.getJournalpostId().isEmpty() || request.getDokumentId().isEmpty()) {
            throw new HentDokumentDokumentIkkeFunnet("DoumentId eller JournalpostId er null.", new DokumentIkkeFunnet());
        }

        /**
         * Henter dokument fra db dersom det finnes, hvis ikke hent statisk data(journalpost) fra prosjektfolder
         */

        JournalDokument journalDokument = null;

        if (request.getDokumentId() != null || request.getDokumentId().isEmpty()){
            journalDokument = new JournalDbLeser(joarkEntityManager).finnDokumentMedDokumentId(request.getDokumentId());            
        } else if(request.getJournalpostId() == null || request.getJournalpostId().isEmpty() ){
            journalDokument = new JournalDbLeser(joarkEntityManager).finnDokumentMedJournalId(request.getJournalpostId());
        }
        if(journalDokument != null) {
            hentDokumentResponse.setDokument(journalDokument.getDokument());
            return hentDokumentResponse;
        }

        Journalpost journalpost = StaticModelData.getJournalpostForId(request.getJournalpostId());

        if (journalpost == null) {
            throw new HentDokumentDokumentIkkeFunnet("Dokument ikke funnet for journalpostId :389425811", new DokumentIkkeFunnet());
        } else {
            Path pdfPath;

            if (StaticModelData.DOKUMENT_ID_393893509.equals(journalpost.getJournalpostId())) {
                pdfPath = FileSystems.getDefault().getPath("/git/vl-mock/joark-mock/journal/src/main/resources/foreldrepenger_soknad.pdf");
            } else {
                pdfPath = FileSystems.getDefault().getPath("/git/vl-mock/joark-mock/journal/src/main/resources/termin_bekreftelse.pdf");
            }
            byte[] pdf = new byte[0];
            try {
                pdf = Files.readAllBytes(pdfPath);
            } catch (IOException e) {

                LOG.error("Error reading pdf file.", e);
            }
            LOG.info("HentDokument: Binary pdf file = %s", pdf);
            hentDokumentResponse.setDokument(pdf);
        }
        return hentDokumentResponse;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentDokumentURLRequest")
    @WebResult(name = "Response", targetNamespace = "")
    @RequestWrapper(localName = "hentDokumentURL", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentURL")
    @ResponseWrapper(localName = "hentDokumentURLResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentURLResponse")
    @Override
    public HentDokumentURLResponse hentDokumentURL(@WebParam(name = "Request", targetNamespace = "") HentDokumentURLRequest request)
            throws HentDokumentURLDokumentIkkeFunnet, HentDokumentURLSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

}