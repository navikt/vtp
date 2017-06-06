package no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.*;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.ObjektIkkeFunnet;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.meldinger.FerdigstillJournalfoeringRequest;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.meldinger.OppdaterJournalpostRequest;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumenttilstand;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Journaltilstand;
import no.nav.tjeneste.virksomhet.journal.v2.modell.JournalV2Constants;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDbLeser;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDokument;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@WebService(
        name = "BehandleInngaaendeJournal_v1",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1"
)
public class BehandleInngaaendeJournalServiceMockImpl implements BehandleInngaaendeJournalV1 {

    private static final EntityManager joarkEntityManager = Persistence.createEntityManagerFactory("joark").createEntityManager();

    private static final String KOMMUNIKASJONSRETNING_INNGAAENDE = "I";
    private static final String FAULTINFO_FEILAARSAK = "brukerfeil";
    private static final String FAULTINFO_FEILKILDE = "mock behandleinngaaendejournal";

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1/BehandleInngaaendeJournal_v1/pingRequest"
    )
    @RequestWrapper(
            localName = "ping",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.Ping"
    )
    @ResponseWrapper(
            localName = "pingResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.PingResponse"
    )
    @Override
    public void ping() {
        // ikke noe
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1/BehandleInngaaendeJournal_v1/ferdigstillJournalfoeringRequest"
    )
    @RequestWrapper(
            localName = "ferdigstillJournalfoering",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.FerdigstillJournalfoering"
    )
    @ResponseWrapper(
            localName = "ferdigstillJournalfoeringResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.FerdigstillJournalfoeringResponse"
    )
    @Override
    public void ferdigstillJournalfoering(@WebParam(name = "request",targetNamespace = "") FerdigstillJournalfoeringRequest request)
            throws FerdigstillJournalfoeringFerdigstillingIkkeMulig, FerdigstillJournalfoeringJournalpostIkkeInngaaende,
                FerdigstillJournalfoeringObjektIkkeFunnet, FerdigstillJournalfoeringSikkerhetsbegrensning, FerdigstillJournalfoeringUgyldigInput {

        String journalpostId = request.getJournalpostId();
        if (journalpostId == null || journalpostId.isEmpty()) {
            UgyldigInput faultInfo = new UgyldigInput();
            faultInfo.setFeilmelding("journalpostId == null eller tom streng");
            faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
            faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
            faultInfo.setTidspunkt(now());
            throw new FerdigstillJournalfoeringUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }

        //String enhetId = request.getEnhetId();
            //TODO (rune) skal den brukes?

        List<JournalDokument> inngaaendeJournalDokListe = getJournalDokuments(journalpostId);
        if (inngaaendeJournalDokListe.isEmpty()) {
            ObjektIkkeFunnet faultInfo = new ObjektIkkeFunnet();
            faultInfo.setFeilmelding("fant ikke journalpost med id \"" + journalpostId +  "\"");
            faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
            faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
            faultInfo.setTidspunkt(now());
            throw new FerdigstillJournalfoeringObjektIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
        }

        haandterDatadrevneExceptions(inngaaendeJournalDokListe);

        inngaaendeJournalDokListe
                .forEach(journalDok -> ferdigstillJournalDokument(journalDok));
    }

    private XMLGregorianCalendar now() {
        return ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now());
    }

    private void ferdigstillJournalDokument(JournalDokument journalDok) {

        journalDok.setDatoFerdigstillt(LocalDateTime.now());
        journalDok.setJournalStatus(JournalV2Constants.JOURNALSTATUS_ENDELIG);
        journalDok.setJournaltilstand(Journaltilstand.ENDELIG.value());
        journalDok.setDokumenttilstand(Dokumenttilstand.FERDIGSTILT.value());
        new JournalDbLeser(joarkEntityManager).oppdaterJournalpost(journalDok);
    }

    //--------------------

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1/BehandleInngaaendeJournal_v1/oppdaterJournalpostRequest"
    )
    @RequestWrapper(
            localName = "oppdaterJournalpost",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.OppdaterJournalpost"
    )
    @ResponseWrapper(
            localName = "oppdaterJournalpostResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.OppdaterJournalpostResponse"
    )
    @Override
    public void oppdaterJournalpost(@WebParam(name = "request",targetNamespace = "") OppdaterJournalpostRequest request)
            throws OppdaterJournalpostJournalpostIkkeInngaaende, OppdaterJournalpostObjektIkkeFunnet,
                OppdaterJournalpostOppdateringIkkeMulig, OppdaterJournalpostSikkerhetsbegrensning, OppdaterJournalpostUgyldigInput {

        String journalpostId = request.getInngaaendeJournalpost().getJournalpostId();
        if (journalpostId == null || journalpostId.isEmpty()) {
            UgyldigInput faultInfo = new UgyldigInput();
            faultInfo.setFeilmelding("journalpostId == null eller tom streng");
            faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
            faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
            faultInfo.setTidspunkt(now());
            throw new OppdaterJournalpostUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }

        //String enhetId = request.getEnhetId();
        //TODO (rune) skal den brukes?

        List<JournalDokument> inngaaendeJournalDokListe = getJournalDokuments(journalpostId);
        if (inngaaendeJournalDokListe.isEmpty()) {
            ObjektIkkeFunnet faultInfo = new ObjektIkkeFunnet();
            faultInfo.setFeilmelding("fant ikke journalpost med id \"" + journalpostId +  "\"");
            faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
            faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
            faultInfo.setTidspunkt(now());
            throw new OppdaterJournalpostObjektIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
        }

        haandterDatadrevneExceptions(inngaaendeJournalDokListe);

        final String sakId;
        if (request.getInngaaendeJournalpost().getArkivSak() != null) {
            sakId = request.getInngaaendeJournalpost().getArkivSak().getArkivSakId();
        } else {
            sakId = null;
        }
        if (sakId != null) {
            inngaaendeJournalDokListe
                    .forEach(journalDok -> oppdaterSaksnr(journalDok, sakId));
        }
    }

    private void oppdaterSaksnr(JournalDokument journalDok, String sakId) {

        journalDok.setSakId(sakId);
        new JournalDbLeser(joarkEntityManager).oppdaterJournalpost(journalDok);
    }

    //--------------------

    private List<JournalDokument> getJournalDokuments(String journalpostId) {
        List<JournalDokument> journalDokListe = new JournalDbLeser(joarkEntityManager).finnDokumenterMedJournalId(journalpostId);
        List<JournalDokument> inngaaendeJournalDokListe = journalDokListe.stream()
                .filter(journalDokument -> KOMMUNIKASJONSRETNING_INNGAAENDE.equals(journalDokument.getKommunikasjonsretning()))
                .collect(Collectors.toList());

        return inngaaendeJournalDokListe;
    }

    private void haandterDatadrevneExceptions(List<JournalDokument> inngaaendeJournalDokListe) {
        //TODO (rune)
    }
}

