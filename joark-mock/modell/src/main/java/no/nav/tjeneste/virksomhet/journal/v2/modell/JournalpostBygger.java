package no.nav.tjeneste.virksomhet.journal.v2.modell;

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
import no.nav.tjeneste.virksomhet.journalmodell.JournalDokument;

public class JournalpostBygger {
    protected long id;
    protected String dokumentId;
    protected String journalpostId;
    protected String innhold;
    protected String sakId;
    protected String dokumentTyper;
    protected String kategori;
    protected String tilknyttetJpSom;
    protected String kommskanaler;
    protected String kommsretninger;
    protected String arkivFiltype;
    protected String variantFormat;
    protected String arkivTema;

    public JournalpostBygger(JournalDokument journalDokument){
        this.id = journalDokument.getId();
        //this.dokumentId = journalDokument.dokumentId;
        this.journalpostId = journalDokument.getJournalpostId();
        this.innhold = journalDokument.getDokument().toString();
        this.sakId = journalDokument.getSakId();
        this.dokumentTyper = journalDokument.getDokumentType();
        this.kategori = journalDokument.getKategori();
        this.tilknyttetJpSom = journalDokument.getTilknJpSom();
        this.kommskanaler = journalDokument.getKommunikasjonskanal();
        this.kommsretninger = journalDokument.getKommunikasjonsretning();
        this.arkivFiltype = journalDokument.getFilType();
        this.variantFormat = journalDokument.getVariantformat();
        this.arkivTema = journalDokument.getArkivtema();
    }

    public JournalpostBygger(long id, String journalpostId, String innhold, String sakId) {
        this.id = id;
        this.journalpostId = journalpostId;
        this.innhold = innhold;
        this.sakId = sakId;
    }

    public Journalpost ByggJournalpost(){
        Journalpost j = new Journalpost();
        j.setJournalpostId(this.journalpostId);
        j.setInnhold(this.innhold);
        //Dokumenttyper
        Dokumenttyper dt = new Dokumenttyper();
        dt.setValue(dokumentTyper);
        //Katagorier
        Katagorier k = new Katagorier();
        k.setValue(kategori);
        //TilknyttetJournalPostSom
        TilknyttetJournalpostSom tj = new TilknyttetJournalpostSom();
        tj.setValue(tilknyttetJpSom);
        //Kommunikasjonskanaler
        Kommunikasjonskanaler kk = new Kommunikasjonskanaler();
        kk.setValue(kommskanaler);
        //Kommunikasjonsretninger
        Kommunikasjonsretninger kr = new Kommunikasjonsretninger();
        kr.setValue(kommsretninger);
        //Arkivfiltyper
        Arkivfiltyper a = new Arkivfiltyper();
        a.setValue(arkivFiltype);
        //Variantformater
        Variantformater v = new Variantformater();
        v.setValue(variantFormat);
        //Arkivtemaer
        Arkivtemaer at = new Arkivtemaer();
        at.setValue(arkivTema);
        //DokumentInnhold
        DokumentInnhold dokumentInnhold = new DokumentInnhold();
        dokumentInnhold.setFiltype(a);
        dokumentInnhold.setVariantformat(v);
        //JournalfoertDokument
        JournalfoertDokumentInfo jd = new JournalfoertDokumentInfo();
        jd.setDokumentType(dt);
        jd.setKategori(k);
        jd.getBeskriverInnholdListe().add(dokumentInnhold);
        //DokumentinfoRelasjon
        DokumentinfoRelasjon dr = new DokumentinfoRelasjon();
        dr.setJournalfoertDokument(jd);
        dr.setDokumentTilknyttetJournalpost(tj);
        j.getDokumentinfoRelasjonListe().add(dr);

        j.setArkivtema(at);
        j.setKommunikasjonskanal(kk);
        j.setKommunikasjonsretning(kr);

        return j;
    }
}
