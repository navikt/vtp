package no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.*;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.*;
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

    private static final String FAULTINFO_FEILAARSAK = "ja si det?";
    private static final String FAULTINFO_FEILKILDE = "mock behandleinngaaendejournal";

    private static final String FEILKODE_JOURNALPOST_IKKE_INNGÅENDE = "JournalpostIkkeInngående";
    private static final String FEILKODE_OBJEKT_IKKE_FUNNET = "ObjektIkkeFunnet";
    private static final String FEILKODE_SIKKERHETSBEGRENSNING = "Sikkerhetsbegrensning";
    private static final String FEILKODE_UGYLDIG_INPUT = "UgyldigInput";
    private static final String FEILKODE_OPPDATERING_IKKE_MULIG = "OppdateringIkkeMulig";

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
            UgyldigInput faultInfo = lagUgyldigInput(journalpostId);
            throw new FerdigstillJournalfoeringUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }

        //String enhetId = request.getEnhetId();
            //TODO (rune) skal den brukes?

        List<JournalDokument> inngaaendeJournalDokListe = getInngaaendeJournalDokumenter(journalpostId);
        if (inngaaendeJournalDokListe.isEmpty()) {
            ObjektIkkeFunnet faultInfo = lagObjektIkkeFunnet(journalpostId);
            throw new FerdigstillJournalfoeringObjektIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
        }

        haandterDatadrevneExceptionsForFerdigstille(inngaaendeJournalDokListe);

        inngaaendeJournalDokListe
                .forEach(journalDok -> ferdigstillJournalDokument(journalDok));
    }

    private void ferdigstillJournalDokument(JournalDokument journalDok) {

        journalDok.setDatoFerdigstillt(LocalDateTime.now());
        journalDok.setJournalStatus(JournalV2Constants.JOURNALSTATUS_JOURNALFØRT);
        journalDok.setJournaltilstand(Journaltilstand.ENDELIG.value());
        journalDok.setDokumenttilstand(Dokumenttilstand.FERDIGSTILT.value());
        new JournalDbLeser(joarkEntityManager).oppdaterJournalpost(journalDok);
    }

    private void haandterDatadrevneExceptionsForFerdigstille(List<JournalDokument> journalDokListe)
            throws FerdigstillJournalfoeringFerdigstillingIkkeMulig, FerdigstillJournalfoeringJournalpostIkkeInngaaende,
                FerdigstillJournalfoeringObjektIkkeFunnet, FerdigstillJournalfoeringSikkerhetsbegrensning, FerdigstillJournalfoeringUgyldigInput {

        for (JournalDokument journalDok : journalDokListe) {
            if (journalDok.getFeilkode() != null) {
                String journalpostId = journalDok.getJournalpostId();
                switch (journalDok.getFeilkode()) {
                    case FEILKODE_JOURNALPOST_IKKE_INNGÅENDE: {
                        JournalpostIkkeInngaeende faultInfo = lagJournalpostIkkeInngaeende(journalpostId);
                        throw new FerdigstillJournalfoeringJournalpostIkkeInngaaende(faultInfo.getFeilmelding(), faultInfo);
                    }
                    case FEILKODE_OBJEKT_IKKE_FUNNET: {
                        ObjektIkkeFunnet faultInfo = lagObjektIkkeFunnet(journalpostId);
                        throw new FerdigstillJournalfoeringObjektIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
                    }
                    case FEILKODE_SIKKERHETSBEGRENSNING: {
                        Sikkerhetsbegrensning faultInfo = lagSikkerhetsbegrensning(journalpostId);
                        throw new FerdigstillJournalfoeringSikkerhetsbegrensning(faultInfo.getFeilmelding(), faultInfo);
                    }
                    case FEILKODE_UGYLDIG_INPUT: {
                        UgyldigInput faultInfo = lagUgyldigInput(journalpostId);
                        throw new FerdigstillJournalfoeringUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
                    }
                    case FEILKODE_OPPDATERING_IKKE_MULIG: {
                        FerdigstillingIkkeMulig faultInfo = lagFerdigstillingIkkeMulig(journalpostId);
                        throw new FerdigstillJournalfoeringFerdigstillingIkkeMulig(faultInfo.getFeilmelding(), faultInfo);
                    }
                    default:
                        // ikke noe
                        break;
                }
            }
        }
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
            UgyldigInput faultInfo = lagUgyldigInput(journalpostId);
            throw new OppdaterJournalpostUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }

        //String enhetId = request.getEnhetId();
        //TODO (rune) skal den brukes?

        List<JournalDokument> inngaaendeJournalDokListe = getInngaaendeJournalDokumenter(journalpostId);
        if (inngaaendeJournalDokListe.isEmpty()) {
            ObjektIkkeFunnet faultInfo = lagObjektIkkeFunnet(journalpostId);
            throw new OppdaterJournalpostObjektIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
        }

        haandterDatadrevneExceptionsForOppdater(inngaaendeJournalDokListe);

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

    private void haandterDatadrevneExceptionsForOppdater(List<JournalDokument> journalDokListe)
            throws OppdaterJournalpostJournalpostIkkeInngaaende, OppdaterJournalpostObjektIkkeFunnet,
            OppdaterJournalpostOppdateringIkkeMulig, OppdaterJournalpostSikkerhetsbegrensning, OppdaterJournalpostUgyldigInput {

        for (JournalDokument journalDok : journalDokListe) {
            if (journalDok.getFeilkode() != null) {
                String journalpostId = journalDok.getJournalpostId();
                switch (journalDok.getFeilkode()) {
                    case FEILKODE_JOURNALPOST_IKKE_INNGÅENDE: {
                        JournalpostIkkeInngaeende faultInfo = lagJournalpostIkkeInngaeende(journalpostId);
                        throw new OppdaterJournalpostJournalpostIkkeInngaaende(faultInfo.getFeilmelding(), faultInfo);
                    }
                    case FEILKODE_OBJEKT_IKKE_FUNNET: {
                        ObjektIkkeFunnet faultInfo = lagObjektIkkeFunnet(journalpostId);
                        throw new OppdaterJournalpostObjektIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
                    }
                    case FEILKODE_SIKKERHETSBEGRENSNING: {
                        Sikkerhetsbegrensning faultInfo = lagSikkerhetsbegrensning(journalpostId);
                        throw new OppdaterJournalpostSikkerhetsbegrensning(faultInfo.getFeilmelding(), faultInfo);
                    }
                    case FEILKODE_UGYLDIG_INPUT: {
                        UgyldigInput faultInfo = lagUgyldigInput(journalpostId);
                        throw new OppdaterJournalpostUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
                    }
                    case FEILKODE_OPPDATERING_IKKE_MULIG: {
                        OppdateringIkkeMulig faultInfo = lagOppdateringIkkeMulig(journalpostId);
                        throw new OppdaterJournalpostOppdateringIkkeMulig(faultInfo.getFeilmelding(), faultInfo);
                    }
                    default:
                        // ikke noe
                        break;
                }
            }
        }
    }

    //--------------------

    private List<JournalDokument> getInngaaendeJournalDokumenter(String journalpostId) {
        List<JournalDokument> journalDokListe = new JournalDbLeser(joarkEntityManager).finnDokumenterMedJournalId(journalpostId);
        List<JournalDokument> inngaaendeJournalDokListe = journalDokListe.stream()
                .filter(journalDokument -> KOMMUNIKASJONSRETNING_INNGAAENDE.equals(journalDokument.getKommunikasjonsretning()))
                .collect(Collectors.toList());

        return inngaaendeJournalDokListe;
    }

    private JournalpostIkkeInngaeende lagJournalpostIkkeInngaeende(String journalpostId) {

        JournalpostIkkeInngaeende faultInfo = new JournalpostIkkeInngaeende();
        faultInfo.setFeilmelding("journalpost med id \"" + journalpostId +  "\" er ikke inngående");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private ObjektIkkeFunnet lagObjektIkkeFunnet(String journalpostId) {

        ObjektIkkeFunnet faultInfo = new ObjektIkkeFunnet();
        faultInfo.setFeilmelding("journalpost med id \"" + journalpostId +  "\" ble ikke funnet");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private Sikkerhetsbegrensning lagSikkerhetsbegrensning(String journalpostId) {

        Sikkerhetsbegrensning faultInfo = new Sikkerhetsbegrensning();
        faultInfo.setFeilmelding("journalpost med id \"" + journalpostId +  "\" har sikkerhetsbegrensning");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private UgyldigInput lagUgyldigInput(String journalpostId) {

        UgyldigInput faultInfo = new UgyldigInput();
        faultInfo.setFeilmelding("id \"" + journalpostId +  "\" er ugyldig input");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private OppdateringIkkeMulig lagOppdateringIkkeMulig(String journalpostId) {
        OppdateringIkkeMulig faultInfo = new OppdateringIkkeMulig();
        faultInfo.setFeilmelding("journalpost med id \"" + journalpostId +  "\" er ikke mulig å oppdatere");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private FerdigstillingIkkeMulig lagFerdigstillingIkkeMulig(String journalpostId) {
        FerdigstillingIkkeMulig faultInfo = new FerdigstillingIkkeMulig();
        faultInfo.setFeilmelding("journalpost med id \"" + journalpostId +  "\" er ikke mulig å ferdigstille");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private void populerMedStandardVerdier(ForretningsmessigUnntak faultInfo) {
        faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
        faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
        faultInfo.setTidspunkt(now());
    }

    private XMLGregorianCalendar now() {
        return ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now());
    }
}

