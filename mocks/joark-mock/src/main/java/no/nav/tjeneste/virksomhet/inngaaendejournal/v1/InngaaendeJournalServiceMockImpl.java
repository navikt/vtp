package no.nav.tjeneste.virksomhet.inngaaendejournal.v1;

import java.util.Optional;

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

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.tjeneste.virksomhet.inngaaendejournal.modell.InngaaendeJournalpostBuilder;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.HentJournalpostJournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.HentJournalpostJournalpostIkkeInngaaende;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.HentJournalpostSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.HentJournalpostUgyldigInput;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.InngaaendeJournalV1;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.UtledJournalfoeringsbehovJournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.UtledJournalfoeringsbehovJournalpostIkkeInngaaende;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.UtledJournalfoeringsbehovJournalpostKanIkkeBehandles;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.UtledJournalfoeringsbehovSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.UtledJournalfoeringsbehovUgyldigInput;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.InngaaendeJournalpost;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.HentJournalpostRequest;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.HentJournalpostResponse;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.UtledJournalfoeringsbehovRequest;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.UtledJournalfoeringsbehovResponse;

@Addressing
@WebService(
        name = "InngaaendeJournal_v1",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1"
)
@HandlerChain(file="Handler-chain.xml")
public class InngaaendeJournalServiceMockImpl implements InngaaendeJournalV1 {
    private static final Logger LOG = LoggerFactory.getLogger(InngaaendeJournalServiceMockImpl.class);


    private static final String KOMMUNIKASJONSRETNING_INNGAAENDE = "I";

    public InngaaendeJournalServiceMockImpl(){}

    private JournalRepository journalRepository;

    public InngaaendeJournalServiceMockImpl(JournalRepository journalRepository){
        this.journalRepository = journalRepository;
    }
    
    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1/InngaaendeJournal_v1/pingRequest"
    )
    @RequestWrapper(
            localName = "ping",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.Ping"
    )
    @ResponseWrapper(
            localName = "pingResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.PingResponse"
    )
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1/InngaaendeJournal_v1/hentJournalpostRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "hentJournalpost",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.HentJournalpost"
    )
    @ResponseWrapper(
            localName = "hentJournalpostResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.HentJournalpostResponse"
    )
    @Override
    public HentJournalpostResponse hentJournalpost(@WebParam(name = "request", targetNamespace = "") HentJournalpostRequest request)
            throws HentJournalpostJournalpostIkkeFunnet, HentJournalpostJournalpostIkkeInngaaende,
            HentJournalpostSikkerhetsbegrensning, HentJournalpostUgyldigInput {

        String journalpostId = request.getJournalpostId();
        LOG.info("Henter journalpost med id: "+ journalpostId);

        Optional<JournalpostModell> journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId);
        if(!journalpostModell.isPresent()){
            throw new HentJournalpostJournalpostIkkeFunnet("Kunne ikke finne journalpost");
        }

        InngaaendeJournalpost inngaaendeJournalpost = InngaaendeJournalpostBuilder.buildFrom(journalpostModell.get());
        HentJournalpostResponse hentJournalpostResponse = new HentJournalpostResponse();
        hentJournalpostResponse.setInngaaendeJournalpost(inngaaendeJournalpost);
        return hentJournalpostResponse;
    }



    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1/InngaaendeJournal_v1/utledJournalfoeringsbehovRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "utledJournalfoeringsbehov",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.UtledJournalfoeringsbehov"
    )
    @ResponseWrapper(
            localName = "utledJournalfoeringsbehovResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.UtledJournalfoeringsbehovResponse"
    )
    @Override
    public UtledJournalfoeringsbehovResponse utledJournalfoeringsbehov(@WebParam(name = "request", targetNamespace = "") UtledJournalfoeringsbehovRequest request)
            throws UtledJournalfoeringsbehovJournalpostIkkeFunnet, UtledJournalfoeringsbehovJournalpostIkkeInngaaende,
            UtledJournalfoeringsbehovJournalpostKanIkkeBehandles, UtledJournalfoeringsbehovSikkerhetsbegrensning,
            UtledJournalfoeringsbehovUgyldigInput {

            throw new UnsupportedOperationException("Ikke implementert");

    }


}
