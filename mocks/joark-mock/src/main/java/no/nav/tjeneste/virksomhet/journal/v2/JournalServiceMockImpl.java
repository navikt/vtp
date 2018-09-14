package no.nav.tjeneste.virksomhet.journal.v2;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.testmodell.journal.JournalScenarioTjenesteImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.journal.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.journal.dokument.DokumentVariantInnhold;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.journal.modell.JournalpostBygger;
import no.nav.tjeneste.virksomhet.journal.modell.JournalpostModelData;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentURLDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentURLSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentJournalpostListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.JournalV2;
import no.nav.tjeneste.virksomhet.journal.v2.feil.DokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentResponse;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentURLRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentURLResponse;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentJournalpostListeRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentJournalpostListeResponse;

@Addressing
@WebService(name = "Journal_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2")
@HandlerChain(file="Handler-chain.xml")
public class JournalServiceMockImpl implements JournalV2 {
    private static final Logger LOG = LoggerFactory.getLogger(JournalServiceMockImpl.class);

    private JournalpostModelData journalpostModelData;

    private JournalScenarioTjenesteImpl joarkScenarioTjeneste;

    public JournalServiceMockImpl(){}

    public JournalServiceMockImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.journalpostModelData = new JournalpostModelData(scenarioRepository);
        this.joarkScenarioTjeneste = new JournalScenarioTjenesteImpl(scenarioRepository);
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentJournalpostListeRequest")
    @WebResult(name = "Response", targetNamespace = "")
    @RequestWrapper(localName = "hentJournalpostListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentJournalpostListe")
    @ResponseWrapper(localName = "hentJournalpostListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentJournalpostListeResponse")
    @Override
    public HentJournalpostListeResponse hentJournalpostListe(@WebParam(name = "Request", targetNamespace = "") HentJournalpostListeRequest request)
            throws HentJournalpostListeSikkerhetsbegrensning {

        HentJournalpostListeResponse response = new HentJournalpostListeResponse();
        List<String> sakList = request.getSakListe().stream().map(t-> t.getSakId()).collect(Collectors.toList());

        if (sakList.size() == 0) {
            LOG.info("Fant ingen saksnr i request");
            return response;
        }

        for(String sak : sakList){
            List<JournalpostModell> journalpostModelleForSak = joarkScenarioTjeneste.finnJournalposterMedSakId(sak);
            for(JournalpostModell journalpostModell : journalpostModelleForSak){
                Journalpost journalpost = JournalpostBygger.buildFrom(journalpostModell);
                response.getJournalpostListe().add(journalpost);
            }
        }
        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentDokumentRequest")
    @WebResult(name = "Response", targetNamespace = "")
    @RequestWrapper(localName = "hentDokument", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokument")
    @ResponseWrapper(localName = "hentDokumentResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentResponse")
    @Override
    public HentDokumentResponse hentDokument(@WebParam(name = "Request", targetNamespace = "") HentDokumentRequest request)
            throws HentDokumentDokumentIkkeFunnet, HentDokumentSikkerhetsbegrensning {


        /**
         * Henter journal fra db dersom det finnes, hvis ikke hent statisk data(journalpost) fra prosjektfolder
         */
        
        HentDokumentResponse hentDokumentResponse = new HentDokumentResponse();

        DokumentVariantInnhold dokumentVariantInnhold = null;

        if (request.getDokumentId() != null || request.getDokumentId().isEmpty()) {
            dokumentVariantInnhold = joarkScenarioTjeneste.finnDokumentMedDokumentId(request.getDokumentId()).getDokumentVariantInnholdListe().
                    stream().filter(d -> d.getVariantFormat().equals(request.getVariantformat().getValue())).findFirst().get();
        } else if (request.getJournalpostId() == null || request.getJournalpostId().isEmpty()) {
            joarkScenarioTjeneste.finnDokumentMedJournalId(request.getJournalpostId())
        }
        if (dokumentVariantInnhold != null) {
            hentDokumentResponse.setDokument(dokumentVariantInnhold.getDokumentInnhold());
            return hentDokumentResponse;
        }

        Journalpost journalpost = journalpostModelData.getJournalpostForId(request.getJournalpostId());

        if (journalpost == null) {
            throw new HentDokumentDokumentIkkeFunnet("Dokument ikke funnet for journalpostId :389425811", new DokumentIkkeFunnet());
        } else {
            Path pdfPath;

            if (JournalpostModelData.DOKUMENT_ID_393893509.equals(journalpost.getJournalpostId())) {
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