package no.nav.tjeneste.virksomhet.journal.v2.modell;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.*;
import no.nav.tjeneste.virksomhet.person.v2.data.PersonDbLeser;
import no.nav.tjeneste.virksomhet.person.v2.modell.TpsPerson;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toList;

public class StaticModelData {

    private static final Map<String, List<Journalpost>> JOURNALPOSTER_PER_FAGSAK = new HashMap<>();
    private static final Map<String, Journalpost> JOURNALPOST_PER_JOURNAL_ID = new HashMap<>();

    private static final String FILTYPE_XML = "XML";
    private static final String VARIANTFORMAT_ORIGINAL = "ORIGINAL";
    private static final String DOKUMENT_TYPE = "00001";
    private static final String VARIANTFORMAT_ARKIV = "ARKIV";
    private static final String FILTYPE_PDF = "PDF";
    public static final String DOKUMENT_ID_393893509 = "393893509";
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime YESTERDAY = LocalDateTime.now().minusDays(1);
    public static final String TILKNYTTET_SOM_HOVEDDOKUMENT = "HOVEDDOKUMENT";
    public static final String TILKNYTTET_SOM_VEDLEGG = "VEDLEGG";

    private static final EntityManager tpsEntityManager = Persistence.createEntityManagerFactory("tps").createEntityManager();

    static {
        // Fagsaksnr settes opp som fnr*100 av simulert Swagger-mottak

        List<TpsPerson> tpsPersoner = new PersonDbLeser(tpsEntityManager).lesTpsData();
        List<String> saksnummere = tpsPersoner.stream()
                .map(TpsPerson::getFnr)
                .map(fnr -> (parseLong(fnr) * 100) + "") //saksnr = fnr * 100
                .collect(toList());

        saksnummere.forEach(saksnummer -> {
                    Journalpost journalpostInn = createJournalpost("journalpost-inn-" + saksnummer, "I");
                    List<DokumentinfoRelasjon> dokumentListeInn = journalpostInn.getDokumentinfoRelasjonListe();
                    dokumentListeInn.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ARKIV, "Dokument_inn_1", "393893532"));
                    dokumentListeInn.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ARKIV, "Dokument_inn_2", "393893532"));
                    dokumentListeInn.add(createDokumentinfoRelasjon(FILTYPE_XML, VARIANTFORMAT_ARKIV, "Dokument_inn_3", "393893544"));
                    dokumentListeInn.add(createDokumentinfoRelasjon(FILTYPE_XML, VARIANTFORMAT_ORIGINAL, "Dokument_inn_4", "393893534"));

                    Journalpost journalpostUt = createJournalpost("journalpost-ut-" + saksnummer, "U");
                    List<DokumentinfoRelasjon> dokumentListeUt = journalpostUt.getDokumentinfoRelasjonListe();
                    dokumentListeUt.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ORIGINAL, "Dokument_ut_1", DOKUMENT_ID_393893509));
                    dokumentListeUt.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ARKIV, "Dokument_ut_2", DOKUMENT_ID_393893509));
                    dokumentListeUt.add(createDokumentinfoRelasjon(FILTYPE_PDF, VARIANTFORMAT_ARKIV, "Dokument_ut_3", "393893534"));

                    List<Journalpost> journalposter = new ArrayList<>();
                    journalposter.add(journalpostInn);
                    journalposter.add(journalpostUt);
                    JOURNALPOSTER_PER_FAGSAK.put(saksnummer, journalposter);
                }
        );
        JOURNALPOSTER_PER_FAGSAK.forEach((saksnr, poster) ->
                poster.forEach(post -> JOURNALPOST_PER_JOURNAL_ID.put(post.getJournalpostId(), post)));
    }

    private StaticModelData() {
        // skjult
    }

    public static List<Journalpost> getJournalposterForFagsak(String saksnr) {
        return JOURNALPOSTER_PER_FAGSAK.get(saksnr);
    }

    public static Journalpost getJournalpostForId(String journalpostId) {
        return JOURNALPOST_PER_JOURNAL_ID.get(journalpostId);
    }

    private static Journalpost createJournalpost(String journalpostId, String kommunikasjonsretning) {
        Journalpost journalpost = new Journalpost();
        journalpost.setJournalpostId(journalpostId);
        journalpost.setSendt(ConversionUtils.convertToXMLGregorianCalendar(NOW));
        journalpost.setMottatt(ConversionUtils.convertToXMLGregorianCalendar(YESTERDAY));
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
