package no.nav.tjeneste.virksomhet.journal.v2;

import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentURLDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentURLSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentJournalpostListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.journal.v2.binding.JournalV2;
import no.nav.tjeneste.virksomhet.journal.v2.feil.DokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Arkivfiltyper;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentInnhold;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentinfoRelasjon;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Dokumenttyper;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.JournalfoertDokumentInfo;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Kommunikasjonsretninger;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Variantformater;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentResponse;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentURLRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentDokumentURLResponse;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentJournalpostListeRequest;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.HentJournalpostListeResponse;
import no.nav.tjeneste.virksomhet.person.v2.data.PersonDbLeser;
import no.nav.tjeneste.virksomhet.person.v2.modell.TpsPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toList;

@WebService(name = "Journal_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2")
public class JournalServiceMockImpl implements JournalV2 {
    private static final Logger LOG = LoggerFactory.getLogger(JournalServiceMockImpl.class);
    private static final String FILTYPE_XML = "XML";
    private static final String VARIANTFORMAT_ORIGINAL = "ORIGINAL";
    private static final String DOKUMENT_TYPE = "00001";
    private static final String VARIANTFORMAT_ARKIV = "ARKIV";
    private static final String FILTYPE_PDF = "PDF";
    private static final String DOKUMENT_ID_393893509 = "393893509";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime YESTERDAY = LocalDateTime.now().minusDays(1);

    private static final Map<String, List<Journalpost>> JOURNALPOSTER_PER_FAGSAK = new HashMap<>();
    private static final Map<String, Journalpost> JOURNALPOST_PER_JOURNAL_ID = new HashMap<>();

    static {
        // Fagsaksnr settes opp som fnr*100 av simulert Swagger-mottak
        EntityManager entityManager = Persistence.createEntityManagerFactory("tps").createEntityManager();

        List<TpsPerson> tpsPersoner = new PersonDbLeser(entityManager).lesTpsData();
        List<String> saksnummere = tpsPersoner.stream()
                .map(TpsPerson::getFnr)
                .map(fnr -> (parseLong(fnr) * 100) + "") //saksnr = fnr * 100
                .collect(toList());

        saksnummere.forEach(saksnummer -> {
                    Journalpost journalpostInn = createJournalpost("journalpost-inn-" + saksnummer, "I");
                    List<DokumentinfoRelasjon> dokumentListeInn = journalpostInn.getDokumentinfoRelasjonListe();
                    dokumentListeInn.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ARKIV, "Dokument 1", "393893532"));
                    dokumentListeInn.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ARKIV, "Dokument 1", "393893532"));
                    dokumentListeInn.add(createDokumentinfoRelasjon(FILTYPE_XML, VARIANTFORMAT_ARKIV, "Dokument 2", "393893544"));
                    dokumentListeInn.add(createDokumentinfoRelasjon(FILTYPE_XML, VARIANTFORMAT_ORIGINAL, "Dokument 3", "393893534"));

                    Journalpost journalpostUt = createJournalpost("journalpost-ut-" + saksnummer, "U");
                    List<DokumentinfoRelasjon> dokumentListeUt = journalpostInn.getDokumentinfoRelasjonListe();
                    dokumentListeUt.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ORIGINAL, "Dokument 4", DOKUMENT_ID_393893509));
                    dokumentListeUt.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ARKIV, "Dokument 5", DOKUMENT_ID_393893509));
                    dokumentListeUt.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ARKIV, "Dokument 6", "393893534"));

                    List<Journalpost> journalposter = new ArrayList<>();
                    journalposter.add(journalpostInn);
                    journalposter.add(journalpostUt);
                    JOURNALPOSTER_PER_FAGSAK.put(saksnummer, journalposter);
                }
        );
        JOURNALPOSTER_PER_FAGSAK.forEach((saksnr, poster) ->
                poster.forEach(post -> JOURNALPOST_PER_JOURNAL_ID.put(post.getJournalpostId(), post)));
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

        String saksnr = funnetsaksnr.get();
        List<Journalpost> journalposter = JOURNALPOSTER_PER_FAGSAK.get(saksnr);
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
        Journalpost journalpost = JOURNALPOST_PER_JOURNAL_ID.get(request.getJournalpostId());

        if (journalpost == null) {
            throw new HentDokumentDokumentIkkeFunnet("Dokument ikke funnet for journalpostId :389425811", new DokumentIkkeFunnet());
        } else {
            Path pdfPath;
            if (DOKUMENT_ID_393893509.equals(journalpost.getJournalpostId())) {
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

    private static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime localDateTime) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        XMLGregorianCalendar xmlGregorianCalendar = null;
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            LOG.error("", e);
        }
        return xmlGregorianCalendar;
    }

    private static Journalpost createJournalpost(String journalpostId, String kommunikasjonsretning) {
        Journalpost journalpost = new Journalpost();
        journalpost.setJournalpostId(journalpostId);
        journalpost.setSendt(convertToXMLGregorianCalendar(NOW));
        journalpost.setMottatt(convertToXMLGregorianCalendar(YESTERDAY));
        Kommunikasjonsretninger kommunikasjonsretninger = new Kommunikasjonsretninger();
        kommunikasjonsretninger.setValue(kommunikasjonsretning);
        journalpost.setKommunikasjonsretning(kommunikasjonsretninger);
        return journalpost;
    }

    private static DokumentinfoRelasjon createDokumentinfoRelasjon(String filtype, String variantformat, String tittel, String dokumentId) {
        DokumentinfoRelasjon dokumentinfoRelasjon = new DokumentinfoRelasjon();
        JournalfoertDokumentInfo journalfoertDokumentInfo = new JournalfoertDokumentInfo();
        journalfoertDokumentInfo.setDokumentId(dokumentId);
        Dokumenttyper dokumenttyper = new Dokumenttyper();
        dokumenttyper.setValue(DOKUMENT_TYPE);
        journalfoertDokumentInfo.setDokumentType(dokumenttyper);
        journalfoertDokumentInfo.setTittel(tittel);
        DokumentInnhold dokumentInnhold = new DokumentInnhold();
        Arkivfiltyper arkivfiltyper = new Arkivfiltyper();
        arkivfiltyper.setValue(filtype);
        dokumentInnhold.setFiltype(arkivfiltyper);
        Variantformater variantformater = new Variantformater();
        variantformater.setValue(variantformat);
        dokumentInnhold.setVariantformat(variantformater);
        journalfoertDokumentInfo.getBeskriverInnholdListe().add(dokumentInnhold);
        dokumentinfoRelasjon.setJournalfoertDokument(journalfoertDokumentInfo);
        return dokumentinfoRelasjon;
    }
}