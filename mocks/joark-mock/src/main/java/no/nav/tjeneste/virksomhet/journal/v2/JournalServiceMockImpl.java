package no.nav.tjeneste.virksomhet.journal.v2;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.journal.JournalDokument;
import no.nav.tjeneste.virksomhet.journal.modell.JournalScenarioTjenesteImpl;
import no.nav.tjeneste.virksomhet.journal.modell.JournalpostBygger;
import no.nav.tjeneste.virksomhet.journal.modell.JournalpostModelData;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentURLDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentURLSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentJournalpostListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.JournalV2;
import no.nav.tjeneste.virksomhet.journal.v2.feil.DokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentInnhold;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentinfoRelasjon;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.JournalfoertDokumentInfo;
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
        Optional<String> funnetsaksnr = request.getSakListe().stream().map(sak -> sak.getSakId()).findFirst();

        if (!funnetsaksnr.isPresent()) {
            LOG.info("Fant ingen saksnr i request.");
            return response;
        }

        List<Journalpost> journalposter;

        List<JournalDokument> journalpost = joarkScenarioTjeneste.finnJournalposterMedSakId(funnetsaksnr.get());
        Map<String, List<JournalDokument>> journalDokumenterPerJpId = new HashMap<>();
        Map<String, List<JournalDokument>> journalDokumenterPerDokType = new HashMap<>();

        if (journalpost != null) {

            lagJournalDokumentMapMedJpIdSomKey(journalpost, journalDokumenterPerJpId);

            int j = 0;
            for (String key : journalDokumenterPerJpId.keySet()) {
                JournalpostBygger journalpostBygger = null;

                for (int i = 0; i < journalDokumenterPerJpId.get(key).size(); i++) {
                    String keyForType = journalDokumenterPerJpId.get(key).get(i).getDokumentType();
                    if (journalDokumenterPerDokType.containsKey(keyForType)) {
                        List<JournalDokument> jdList = journalDokumenterPerDokType.get(keyForType);
                        jdList.add(journalDokumenterPerJpId.get(key).get(i));

                    } else {
                        List<JournalDokument> jdokList = new ArrayList<>();
                        jdokList.add(journalDokumenterPerJpId.get(key).get(i));
                        journalDokumenterPerDokType.put(keyForType, jdokList);
                    }
                }

                try {

                    Object keyJd = journalDokumenterPerDokType.keySet().toArray()[0];
                    journalpostBygger = new JournalpostBygger(journalDokumenterPerDokType.get(keyJd).get(0));
                    response.getJournalpostListe().add(journalpostBygger.byggJournalpost());

                    for (String st : journalDokumenterPerDokType.keySet()) {
                        Object[] keyValues = journalDokumenterPerDokType.keySet().toArray();
                        int index = Arrays.asList(keyValues).indexOf(st);
                        List<DokumentInnhold> dokumentInnholdList = journalDokumenterPerDokType.get(st).stream().map(e-> new JournalpostBygger(e).byggDokumentInnhold()).collect(Collectors.toList());
                        List<DokumentinfoRelasjon> dokumentinfoRelasjonList = journalDokumenterPerDokType.get(st).stream().map(e-> new JournalpostBygger(e).byggDokumentinfoRelasjon()).collect(Collectors.toList());
                        List<JournalfoertDokumentInfo> journalfoertDokumentInfo = journalDokumenterPerDokType.get(st).stream().map(e-> new JournalpostBygger(e).byggJournalfoertDokumentInfo()).collect(Collectors.toList());
                        response.getJournalpostListe().get(j).getDokumentinfoRelasjonListe().add(dokumentinfoRelasjonList.get(0));
                        response.getJournalpostListe().get(j).getDokumentinfoRelasjonListe().get(index).setJournalfoertDokument(journalfoertDokumentInfo.get(0));
                        response.getJournalpostListe().get(j).getDokumentinfoRelasjonListe().get(index).getJournalfoertDokument().getBeskriverInnholdListe().addAll(dokumentInnholdList);

                    }
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
                j++;
            }
            return response;
        }

        String saksnr = funnetsaksnr.get();
        journalposter = journalpostModelData.getJournalposterForFagsak(saksnr);
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
         * Henter journal fra db dersom det finnes, hvis ikke hent statisk data(journalpost) fra prosjektfolder
         */

        JournalDokument journalDokument = null;

        if (request.getDokumentId() != null || request.getDokumentId().isEmpty()) {
            journalDokument = joarkScenarioTjeneste.finnDokumentMedDokumentId(request.getDokumentId()).
                    stream().filter(d -> d.getVariantformat().equals(request.getVariantformat().getValue())).findFirst().get();
        } else if (request.getJournalpostId() == null || request.getJournalpostId().isEmpty()) {
            journalDokument = joarkScenarioTjeneste.finnDokumentMedJournalId(request.getJournalpostId());
        }
        if (journalDokument != null) {
            hentDokumentResponse.setDokument(journalDokument.getDokument());
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

    private void lagJournalDokumentMapMedJpIdSomKey (List<JournalDokument> journalpost, Map<String, List<JournalDokument>> journalDokumenterPerJpId) {
        for (JournalDokument jd : journalpost) {
            String key = jd.getJournalpostId();

            if (journalDokumenterPerJpId.containsKey(key)) {
                List<JournalDokument> list = journalDokumenterPerJpId.get(key);
                list.add(jd);
            } else {
                List<JournalDokument> list = new ArrayList<>();
                list.add(jd);
                journalDokumenterPerJpId.put(key, list);
            }
        }
    }

}