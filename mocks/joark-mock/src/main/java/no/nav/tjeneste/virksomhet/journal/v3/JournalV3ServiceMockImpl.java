package no.nav.tjeneste.virksomhet.journal.v3;

import java.util.List;
import java.util.Optional;
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

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.tjeneste.virksomhet.journal.modell.JournalpostV3Bulider;
import no.nav.tjeneste.virksomhet.journal.v3.feil.DokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentDokumentRequest;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentDokumentResponse;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentDokumentURLRequest;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentDokumentURLResponse;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentKjerneJournalpostListeRequest;
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.HentKjerneJournalpostListeResponse;


@Addressing
@WebService(name = "Journal_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3")
@HandlerChain(file="Handler-chain.xml")
public class JournalV3ServiceMockImpl implements JournalV3 {

    private static final Logger LOG = LoggerFactory.getLogger(JournalV3ServiceMockImpl.class);

    private JournalRepository journalRepository;

    public JournalV3ServiceMockImpl() {}

    public JournalV3ServiceMockImpl(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public void ping() {
        LOG.info("Ping mottat og besvart.");
    }

    @Override
    public HentDokumentURLResponse hentDokumentURL(HentDokumentURLRequest hentDokumentURLRequest) throws HentDokumentURLDokumentIkkeFunnet, HentDokumentURLSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert.");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v3/Journal_v3/hentKjerneJournalpostListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentKjerneJournalpostListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3", className = "no.nav.tjeneste.virksomhet.journal.v3.HentKjerneJournalpostListe")
    @ResponseWrapper(localName = "hentKjerneJournalpostListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3", className = "no.nav.tjeneste.virksomhet.journal.v3.HentKjerneJournalpostListeResponse")
    @Override
    public HentKjerneJournalpostListeResponse hentKjerneJournalpostListe(@WebParam(name = "request", targetNamespace = "") HentKjerneJournalpostListeRequest request) throws HentKjerneJournalpostListeSikkerhetsbegrensning, HentKjerneJournalpostListeUgyldigInput {
        LOG.info("JournalV3. hentKjerneJournalpostListe.");

        HentKjerneJournalpostListeResponse response = new HentKjerneJournalpostListeResponse();
        List<String> saker = request.getArkivSakListe().stream().map(t -> t.getArkivSakId()).collect(Collectors.toList());
        LOG.info("Henter journalpost for følgende saker: "+ saker.stream().collect(Collectors.joining(",")));

        for(String sak : saker){
            for(JournalpostModell modell : journalRepository.finnJournalposterMedSakId(sak)){
                response.getJournalpostListe().add(JournalpostV3Bulider.buildFrom(modell));
            }
        }
        // TODO: sett sisteIntervall?
        return response;
    }
    
    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v3/Journal_v3/hentDokumentRequest")
    @RequestWrapper(localName = "hentDokument", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3", className = "no.nav.tjeneste.virksomhet.journal.v3.HentDokument")
    @ResponseWrapper(localName = "hentDokumentResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3", className = "no.nav.tjeneste.virksomhet.journal.v3.HentDokumentResponse")
    @WebResult(name = "response", targetNamespace = "")
    public HentDokumentResponse hentDokument(@WebParam(name="request", targetNamespace="") HentDokumentRequest request) throws HentDokumentSikkerhetsbegrensning, HentDokumentDokumentIkkeFunnet, HentDokumentJournalpostIkkeFunnet {
        LOG.info("JournalV3. hentDokument");
        HentDokumentResponse response = new HentDokumentResponse();
        Optional<DokumentModell> dokumentModell = journalRepository.finnDokumentMedDokumentId(request.getDokumentId());
        LOG.info("Henter dokument på følgende dokumentId: " + request.getDokumentId());

        if (dokumentModell.isPresent()) {
            String innhold = dokumentModell.get().getInnhold();
            response.setDokument(innhold.getBytes());
            return response;
        } else {
            throw new HentDokumentDokumentIkkeFunnet("Kunne ikke finne dokument", new DokumentIkkeFunnet());
        }
    }
}
