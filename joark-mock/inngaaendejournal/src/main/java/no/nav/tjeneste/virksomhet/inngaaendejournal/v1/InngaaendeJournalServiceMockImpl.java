package no.nav.tjeneste.virksomhet.inngaaendejournal.v1;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.*;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.feil.JournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.*;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.HentJournalpostResponse;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.UtledJournalfoeringsbehovResponse;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.modell.StaticModelData;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDbLeser;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDokument;
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
import java.time.LocalDateTime;
import java.util.List;

@WebService(
        name = "InngaaendeJournal_v1",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1"
)
public class InngaaendeJournalServiceMockImpl implements InngaaendeJournalV1 {

    private static final Logger LOG = LoggerFactory.getLogger(InngaaendeJournalServiceMockImpl.class);

    private static final EntityManager joarkEntityManager = Persistence.createEntityManagerFactory("joark").createEntityManager();

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
        // ikke noe
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
    public HentJournalpostResponse hentJournalpost(@WebParam(name = "request",targetNamespace = "") HentJournalpostRequest request)
            throws HentJournalpostJournalpostIkkeFunnet, HentJournalpostJournalpostIkkeInngaaende,
            HentJournalpostSikkerhetsbegrensning, HentJournalpostUgyldigInput {

        // Sjekk input params:

        String journalpostId = request.getJournalpostId();
        if (journalpostId == null || journalpostId.isEmpty()) {
            UgyldigInput faultInfo = new UgyldigInput();
            faultInfo.setFeilmelding("journalpostId == null eller tom streng");
            faultInfo.setFeilaarsak("brukerfeil");
            faultInfo.setFeilkilde("mock inngaaendejournal");
            faultInfo.setTidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now()));
            throw new HentJournalpostUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }

        HentJournalpostResponse response = new HentJournalpostResponse();
        InngaaendeJournalpostBuilder ijpBuilder = new InngaaendeJournalpostBuilder();

        // Let i db og returner det som evt. funnet der:

        List<JournalDokument> journalDokListe = new JournalDbLeser(joarkEntityManager).finnDokumenterMedJournalId(journalpostId);
        if (! journalDokListe.isEmpty()) {
            response.setInngaaendeJournalpost(ijpBuilder.buildFrom(journalDokListe));
            return response;
        }

        // Let i statiske data og returner det som evt. funnet der:

        Journalpost journalpost = StaticModelData.getJournalpostForId(journalpostId);
        if (journalpost != null) {
            response.setInngaaendeJournalpost(ijpBuilder.buildFrom(journalpost));
            return response;
        }

        // Fant ikke noe:

        JournalpostIkkeFunnet faultInfo = new JournalpostIkkeFunnet();
        faultInfo.setFeilmelding("fant ikke journalpost med id " + journalpostId);
        faultInfo.setFeilaarsak("brukerfeil");
        faultInfo.setFeilkilde("mock inngaaendejournal");
        faultInfo.setTidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now()));
        throw new HentJournalpostJournalpostIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
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
    public UtledJournalfoeringsbehovResponse utledJournalfoeringsbehov(@WebParam(name = "request",targetNamespace = "") UtledJournalfoeringsbehovRequest request)
            throws UtledJournalfoeringsbehovJournalpostIkkeFunnet, UtledJournalfoeringsbehovJournalpostIkkeInngaaende,
            UtledJournalfoeringsbehovJournalpostKanIkkeBehandles, UtledJournalfoeringsbehovSikkerhetsbegrensning,
            UtledJournalfoeringsbehovUgyldigInput {

        throw new UnsupportedOperationException("sorry - utledJournalfoeringsbehov ikke implementert");
    }
}
