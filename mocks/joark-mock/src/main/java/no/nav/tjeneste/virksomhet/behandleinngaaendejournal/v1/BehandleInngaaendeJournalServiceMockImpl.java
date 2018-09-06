package no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1;

import static no.nav.tjeneste.virksomhet.journal.modell.JournalDokumentKonstanter.FEILKODE_JOURNALPOST_IKKE_INNGÅENDE;
import static no.nav.tjeneste.virksomhet.journal.modell.JournalDokumentKonstanter.FEILKODE_OBJEKT_IKKE_FUNNET;
import static no.nav.tjeneste.virksomhet.journal.modell.JournalDokumentKonstanter.FEILKODE_OPPDATERING_IKKE_MULIG;
import static no.nav.tjeneste.virksomhet.journal.modell.JournalDokumentKonstanter.FEILKODE_SIKKERHETSBEGRENSNING;
import static no.nav.tjeneste.virksomhet.journal.modell.JournalDokumentKonstanter.FEILKODE_UGYLDIG_INPUT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioRepository;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.BehandleInngaaendeJournalV1;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.FerdigstillJournalfoeringFerdigstillingIkkeMulig;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.FerdigstillJournalfoeringJournalpostIkkeInngaaende;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.FerdigstillJournalfoeringObjektIkkeFunnet;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.FerdigstillJournalfoeringSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.FerdigstillJournalfoeringUgyldigInput;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.OppdaterJournalpostJournalpostIkkeInngaaende;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.OppdaterJournalpostObjektIkkeFunnet;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.OppdaterJournalpostOppdateringIkkeMulig;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.OppdaterJournalpostSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.OppdaterJournalpostUgyldigInput;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.FerdigstillingIkkeMulig;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.ForretningsmessigUnntak;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.JournalpostIkkeInngaeende;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.ObjektIkkeFunnet;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.OppdateringIkkeMulig;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.Sikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.meldinger.FerdigstillJournalfoeringRequest;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.meldinger.OppdaterJournalpostRequest;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumenttilstand;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Journaltilstand;
import no.nav.tjeneste.virksomhet.journal.modell.JournalDokument;
import no.nav.tjeneste.virksomhet.journal.modell.JournalScenarioTjenesteImpl;
import no.nav.tjeneste.virksomhet.journal.modell.JournalV2Constants;

@Addressing
@WebService(
        name = "BehandleInngaaendeJournal_v1",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1"
)
@HandlerChain(file="Handler-chain.xml")
public class BehandleInngaaendeJournalServiceMockImpl implements BehandleInngaaendeJournalV1 {

    private static final String KOMMUNIKASJONSRETNING_INNGAAENDE = "I";

    private static final String FAULTINFO_FEILAARSAK = "ja si det?";
    private static final String FAULTINFO_FEILKILDE = "mock behandleinngaaendejournal";

    private TestscenarioRepository scenarioRepository;

    public BehandleInngaaendeJournalServiceMockImpl(TestscenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

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
        new JournalScenarioTjenesteImpl(scenarioRepository).oppdaterJournalpost(journalDok);
    }

    private void haandterDatadrevneExceptionsForFerdigstille(List<JournalDokument> journalDokListe)
            throws FerdigstillJournalfoeringFerdigstillingIkkeMulig, FerdigstillJournalfoeringJournalpostIkkeInngaaende,
                FerdigstillJournalfoeringObjektIkkeFunnet, FerdigstillJournalfoeringSikkerhetsbegrensning, FerdigstillJournalfoeringUgyldigInput {

        for (JournalDokument journalDok : journalDokListe) {
            String feilkode = journalDok.getFeilkodeFerdigstillJournalfoering();
            if (feilkode != null) {
                String journalpostId = journalDok.getJournalpostId();
                switch (feilkode) {
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
        new JournalScenarioTjenesteImpl(scenarioRepository).oppdaterJournalpost(journalDok);
    }

    private void haandterDatadrevneExceptionsForOppdater(List<JournalDokument> journalDokListe)
            throws OppdaterJournalpostJournalpostIkkeInngaaende, OppdaterJournalpostObjektIkkeFunnet,
            OppdaterJournalpostOppdateringIkkeMulig, OppdaterJournalpostSikkerhetsbegrensning, OppdaterJournalpostUgyldigInput {

        for (JournalDokument journalDok : journalDokListe) {
            String feilkode = journalDok.getFeilkodeOppdaterJournalpost();
            if (feilkode != null) {
                String journalpostId = journalDok.getJournalpostId();
                switch (feilkode) {
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
        List<JournalDokument> journalDokListe = new JournalScenarioTjenesteImpl(scenarioRepository).finnDokumenterMedJournalId(journalpostId);
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

