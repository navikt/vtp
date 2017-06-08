package no.nav.tjeneste.virksomhet.inngaaendejournal.v1;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
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
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.feil.JournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.feil.JournalpostIkkeInngaeende;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.feil.JournalpostKanIkkeBehandles;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.InngaaendeJournalpost;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Journalfoeringsbehov;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.JournalpostMangler;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Journaltilstand;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.HentJournalpostRequest;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.HentJournalpostResponse;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.UtledJournalfoeringsbehovRequest;
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
import java.util.stream.Collectors;

@WebService(
        name = "InngaaendeJournal_v1",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1"
)
public class InngaaendeJournalServiceMockImpl implements InngaaendeJournalV1 {

    private static final EntityManager joarkEntityManager = Persistence.createEntityManagerFactory("joark").createEntityManager();

    private static final String FAULTINFO_FEILAARSAK = "Brukerfeil";
    private static final String FAULTINFO_FEILKILDE = "mock inngaaendejournal";

    private static final String KOMMUNIKASJONSRETNING_INNGAAENDE = "I";


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
    public HentJournalpostResponse hentJournalpost(@WebParam(name = "request", targetNamespace = "") HentJournalpostRequest request)
            throws HentJournalpostJournalpostIkkeFunnet, HentJournalpostJournalpostIkkeInngaaende,
            HentJournalpostSikkerhetsbegrensning, HentJournalpostUgyldigInput {

        // Sjekk input params:

        String journalpostId = request.getJournalpostId();
        if (journalpostId == null || journalpostId.isEmpty()) {
            UgyldigInput faultInfo = getUgyldigInput(journalpostId);
            throw new HentJournalpostUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }

        HentJournalpostResponse response = new HentJournalpostResponse();
        InngaaendeJournalpostBuilder ijpBuilder = new InngaaendeJournalpostBuilder();

        // Let i db og returner det som evt. funnet der:

        List<JournalDokument> journalDokListe = new JournalDbLeser(joarkEntityManager).finnDokumenterMedJournalId(journalpostId);
        List<JournalDokument> inngaaendeJournalDokListe = journalDokListe.stream()
                .filter(journalDokument -> KOMMUNIKASJONSRETNING_INNGAAENDE.equals(journalDokument.getKommunikasjonsretning()))
                .collect(Collectors.toList());
        if (!inngaaendeJournalDokListe.isEmpty()) {
            response.setInngaaendeJournalpost(ijpBuilder.buildFrom(inngaaendeJournalDokListe));
            return response;
        }

        // Let i statiske data og returner det som evt. funnet der:

        Journalpost journalpost = StaticModelData.getJournalpostForId(journalpostId);
        if (journalpost != null && KOMMUNIKASJONSRETNING_INNGAAENDE.equals(journalpost.getKommunikasjonsretning().getValue())) {
            response.setInngaaendeJournalpost(ijpBuilder.buildFrom(journalpost));
            return response;
        }

        // Fant ikke noe:
        JournalpostIkkeFunnet faultInfo = getJournalpostIkkeFunnet(journalpostId);
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
    public UtledJournalfoeringsbehovResponse utledJournalfoeringsbehov(@WebParam(name = "request", targetNamespace = "") UtledJournalfoeringsbehovRequest request)
            throws UtledJournalfoeringsbehovJournalpostIkkeFunnet, UtledJournalfoeringsbehovJournalpostIkkeInngaaende,
            UtledJournalfoeringsbehovJournalpostKanIkkeBehandles, UtledJournalfoeringsbehovSikkerhetsbegrensning,
            UtledJournalfoeringsbehovUgyldigInput {

        String journalpostId = request.getJournalpostId();
        if (journalpostId == null || journalpostId.isEmpty()) {
            UgyldigInput faultInfo = getUgyldigInput(journalpostId);
            throw new UtledJournalfoeringsbehovUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }


        // Let i db og returner det som evt. funnet der:
        List<JournalDokument> journalDokListe = new JournalDbLeser(joarkEntityManager).finnDokumenterMedJournalId(request.getJournalpostId());
        // Let i statiske data og returner det som evt. funnet der:
        Journalpost journalpost = StaticModelData.getJournalpostForId(journalpostId);

        if (journalpost == null) {
            //Finnes ikke objekt i statisk data, utled fra db data
            return utledFrajournalDokumentListe(journalpostId, journalDokListe);
        } else {
            return utledFraJournalpost(journalpostId, journalpost);
        }
    }

    private UtledJournalfoeringsbehovResponse utledFrajournalDokumentListe(String journalpostId, List<JournalDokument> journalDokListe) throws UtledJournalfoeringsbehovJournalpostIkkeFunnet, UtledJournalfoeringsbehovJournalpostIkkeInngaaende,
            UtledJournalfoeringsbehovJournalpostKanIkkeBehandles, UtledJournalfoeringsbehovSikkerhetsbegrensning,
            UtledJournalfoeringsbehovUgyldigInput {

        //Først finn ut om det er noe feil.
        if (journalDokListe == null || journalDokListe.isEmpty()) {
            JournalpostIkkeFunnet journalpostIkkeFunnet = getJournalpostIkkeFunnet(journalpostId);
            throw new UtledJournalfoeringsbehovJournalpostIkkeFunnet(journalpostIkkeFunnet.getFeilmelding(), journalpostIkkeFunnet);
        }

        List<JournalDokument> inngaaendeJournalDokListe = journalDokListe.stream()
                .filter(journalDokument -> KOMMUNIKASJONSRETNING_INNGAAENDE.equals(journalDokument.getKommunikasjonsretning()))
                .collect(Collectors.toList());

        if (inngaaendeJournalDokListe.isEmpty()) {
            JournalpostIkkeInngaeende journalpostIkkeInngaeende = getJournalpostIkkeInngaeende(journalpostId);
            throw new UtledJournalfoeringsbehovJournalpostIkkeInngaaende(journalpostIkkeInngaeende.getFeilmelding(), journalpostIkkeInngaeende);
        }

        InngaaendeJournalpostBuilder ijpBuilder = new InngaaendeJournalpostBuilder();
        InngaaendeJournalpost inngaaendeJournalpost = ijpBuilder.buildFrom(inngaaendeJournalDokListe);
        return utledJournalfoeringsbehovResponse(journalpostId, inngaaendeJournalpost);
    }

    private UtledJournalfoeringsbehovResponse utledFraJournalpost(String journalpostId, Journalpost journalpost) throws UtledJournalfoeringsbehovJournalpostIkkeFunnet, UtledJournalfoeringsbehovJournalpostIkkeInngaaende,
            UtledJournalfoeringsbehovJournalpostKanIkkeBehandles, UtledJournalfoeringsbehovSikkerhetsbegrensning,
            UtledJournalfoeringsbehovUgyldigInput {

        if (journalpost == null) {
            JournalpostIkkeFunnet journalpostIkkeFunnet = getJournalpostIkkeFunnet(journalpostId);
            throw new UtledJournalfoeringsbehovJournalpostIkkeFunnet(journalpostIkkeFunnet.getFeilmelding(), journalpostIkkeFunnet);
        }

        if (!KOMMUNIKASJONSRETNING_INNGAAENDE.equals(journalpost.getKommunikasjonsretning().getValue())) {
            JournalpostIkkeInngaeende journalpostIkkeInngaeende = getJournalpostIkkeInngaeende(journalpostId);
            throw new UtledJournalfoeringsbehovJournalpostIkkeInngaaende(journalpostIkkeInngaeende.getFeilmelding(), journalpostIkkeInngaeende);
        }

        InngaaendeJournalpostBuilder ijpBuilder = new InngaaendeJournalpostBuilder();
        InngaaendeJournalpost inngaaendeJournalpost = ijpBuilder.buildFrom(journalpost);
        return utledJournalfoeringsbehovResponse(journalpostId, inngaaendeJournalpost);
    }

    private UtledJournalfoeringsbehovResponse utledJournalfoeringsbehovResponse(String journalpostId, InngaaendeJournalpost inngaaendeJournalpost) throws UtledJournalfoeringsbehovJournalpostKanIkkeBehandles {

        if ((inngaaendeJournalpost.getJournaltilstand() != null) && (!Journaltilstand.MIDLERTIDIG.equals(inngaaendeJournalpost.getJournaltilstand()))) {
            JournalpostKanIkkeBehandles journalpostKanIkkeBehandles = getJournalpostKanIkkeBehandles(journalpostId);
            throw new UtledJournalfoeringsbehovJournalpostKanIkkeBehandles(journalpostKanIkkeBehandles.getFeilmelding(), journalpostKanIkkeBehandles);
        }

        UtledJournalfoeringsbehovResponse response = new UtledJournalfoeringsbehovResponse();
        JournalpostMangler mangler = new JournalpostMangler();

        if (inngaaendeJournalpost.getArkivSak() == null) {
            mangler.setArkivSak(Journalfoeringsbehov.MANGLER);
        } else {
            mangler.setArkivSak(Journalfoeringsbehov.MANGLER_IKKE);
        }

        response.setJournalfoeringsbehov(mangler);
        return response;
    }

    private JournalpostKanIkkeBehandles getJournalpostKanIkkeBehandles(String journalpostId) {
        JournalpostKanIkkeBehandles faultInfo = new JournalpostKanIkkeBehandles();
        faultInfo.setFeilmelding(String.format("journalpostId %s kan ikke behandles", journalpostId));
        faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
        faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
        faultInfo.setTidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now()));
        return faultInfo;
    }

    private JournalpostIkkeInngaeende getJournalpostIkkeInngaeende(String journalpostId) {
        JournalpostIkkeInngaeende faultInfo = new JournalpostIkkeInngaeende();
        faultInfo.setFeilmelding(String.format("journalpostId %s ikke inngående", journalpostId));
        faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
        faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
        faultInfo.setTidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now()));
        return faultInfo;
    }

    private UgyldigInput getUgyldigInput(String journalpostId) {
        UgyldigInput faultInfo = new UgyldigInput();
        faultInfo.setFeilmelding(String.format("journalpostId %s er ugyldig", journalpostId));
        faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
        faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
        faultInfo.setTidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now()));
        return faultInfo;
    }

    private JournalpostIkkeFunnet getJournalpostIkkeFunnet(String journalpostId) {
        JournalpostIkkeFunnet faultInfo = new JournalpostIkkeFunnet();
        faultInfo.setFeilmelding("Fant ikke journalpost med id " + journalpostId);
        faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
        faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
        faultInfo.setTidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now()));
        return faultInfo;
    }
}
