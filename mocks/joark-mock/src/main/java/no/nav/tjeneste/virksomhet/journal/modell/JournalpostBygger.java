package no.nav.tjeneste.virksomhet.journal.modell;

import java.time.LocalDateTime;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Arkivfiltyper;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Arkivtemaer;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentInnhold;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentinfoRelasjon;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Dokumenttyper;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.JournalfoertDokumentInfo;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Katagorier;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Kommunikasjonskanaler;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Kommunikasjonsretninger;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.TilknyttetJournalpostSom;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Variantformater;

public class JournalpostBygger {
    protected long id;
    protected String dokumentId;
    protected String journalpostId;
//    protected String innhold;
    protected String sakId;
    protected String dokumentTyper;
    protected String kategori;
    protected String tilknyttetJpSom;
    protected String kommskanaler;
    protected String kommsretninger;
    protected String arkivFiltype;
    protected String variantFormat;
    protected String arkivTema;
    protected LocalDateTime datoMottatt;


    public JournalpostBygger(JournalDokument journalDokument){
        this.id = journalDokument.getId();
        this.dokumentId = journalDokument.getDokumentId();
        this.journalpostId = journalDokument.getJournalpostId();
//        this.innhold = journalDokument.getDokument().toString();
        this.sakId = journalDokument.getSakId();
        this.dokumentTyper = journalDokument.getDokumentType();
        this.kategori = journalDokument.getKategori();
        this.tilknyttetJpSom = journalDokument.getTilknJpSom();
        this.kommskanaler = journalDokument.getKommunikasjonskanal();
        this.kommsretninger = journalDokument.getKommunikasjonsretning();
        this.arkivFiltype = journalDokument.getFilType();
        this.variantFormat = journalDokument.getVariantformat();
        this.arkivTema = journalDokument.getArkivtema();
        this.datoMottatt = journalDokument.getDatoMottatt();
    }

    public JournalpostBygger(long id, String journalpostId, String innhold, String sakId) {
        this.id = id;
        this.journalpostId = journalpostId;
//        this.innhold = innhold;
        this.sakId = sakId;
    }

    public Journalpost byggJournalpost(){
        Journalpost journalpost = new Journalpost();
        journalpost.setJournalpostId(this.journalpostId);
        //Kommunikasjonskanaler
        Kommunikasjonskanaler kk = new Kommunikasjonskanaler();
        kk.setValue(kommskanaler);
        //Kommunikasjonsretninger
        Kommunikasjonsretninger kr = new Kommunikasjonsretninger();
        kr.setValue(kommsretninger);
        //Arkivtemaer
        Arkivtemaer at = new Arkivtemaer();
        at.setValue(arkivTema);
        //DatoMottatt
        if(datoMottatt != null) {
            journalpost.setMottatt(ConversionUtils.convertToXMLGregorianCalendar(datoMottatt));
        }

        journalpost.setArkivtema(at);
        journalpost.setKommunikasjonskanal(kk);
        journalpost.setKommunikasjonsretning(kr);

        return journalpost;
    }

    public DokumentinfoRelasjon byggDokumentinfoRelasjon() {
        DokumentinfoRelasjon dokumentinfoRelasjon = new DokumentinfoRelasjon();
        //TilknyttetJournalPostSom
        TilknyttetJournalpostSom tj = new TilknyttetJournalpostSom();
        tj.setValue(tilknyttetJpSom);

        dokumentinfoRelasjon.setDokumentTilknyttetJournalpost(tj);

        return dokumentinfoRelasjon;

    }

    public DokumentInnhold byggDokumentInnhold() {
        //Arkivfiltyper
        Arkivfiltyper a = new Arkivfiltyper();
        a.setValue(arkivFiltype);
        //Variantformater
        Variantformater v = new Variantformater();
        v.setValue(variantFormat);
        //DokumentInnhold
        DokumentInnhold dokumentInnhold = new DokumentInnhold();
        dokumentInnhold.setFiltype(a);
        dokumentInnhold.setVariantformat(v);

        return dokumentInnhold;
    }

    public JournalfoertDokumentInfo byggJournalfoertDokumentInfo() {
        //Dokumenttyper
        Dokumenttyper dt = new Dokumenttyper();
        dt.setValue(dokumentTyper);
        //Katagorier
        Katagorier k = new Katagorier();
        k.setValue(kategori);

        //JournalfoertDokument
        JournalfoertDokumentInfo jd = new JournalfoertDokumentInfo();
        jd.setDokumentType(dt);
        jd.setKategori(k);
        jd.setDokumentId(dokumentId);

        return jd;

    }

}
