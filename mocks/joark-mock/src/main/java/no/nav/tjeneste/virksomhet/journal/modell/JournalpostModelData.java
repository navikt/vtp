package no.nav.tjeneste.virksomhet.journal.modell;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Arkivfiltyper;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentInnhold;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentinfoRelasjon;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Dokumenttyper;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.JournalfoertDokumentInfo;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Kommunikasjonsretninger;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.TilknyttetJournalpostSom;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Variantformater;

public class JournalpostModelData {

    private final Map<String, List<Journalpost>> journalposterPerFagsak = new HashMap<>();
    private final Map<String, Journalpost> journalpostPerJournalpostId = new HashMap<>();

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

    public JournalpostModelData(TestscenarioBuilderRepository scenarioRepository) {

        for (Personopplysninger pers : scenarioRepository.getPersonIndeks().getAlleSøkere()) {
            genererJournalposter(pers);
        }

        journalposterPerFagsak.forEach((saksnr, poster) -> poster.forEach(post -> journalpostPerJournalpostId.put(post.getJournalpostId(), post)));

    }

    // TODO (FC) : Logikken her er ikke helt bra, lag noe nytt
    private void genererJournalposter(Personopplysninger pers) {
        
        // Fagsaksnr settes opp som fnr*100 av simulert Swagger-mottak
        String saksnummer = Long.parseLong(pers.getSøker().getIdent()) * 100L + "";// saksnr = fnr * 100
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
        journalposterPerFagsak.put(saksnummer, journalposter);
    }

    public List<Journalpost> getJournalposterForFagsak(String saksnr) {
        return journalposterPerFagsak.get(saksnr);
    }

    public Journalpost getJournalpostForId(String journalpostId) {
        return journalpostPerJournalpostId.get(journalpostId);
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

        TilknyttetJournalpostSom tilknyttetJournalpostSom = new TilknyttetJournalpostSom();
        tilknyttetJournalpostSom.setValue(TILKNYTTET_SOM_HOVEDDOKUMENT);
        dokumentinfoRelasjon.setDokumentTilknyttetJournalpost(tilknyttetJournalpostSom);

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
