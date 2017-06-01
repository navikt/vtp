package no.nav.tjeneste.virksomhet.journalmodell;

//import org.hibernate.annotationsNamedQueries;
//import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
//import javax.persistence.Transient;
//import java.io.FileNotFoundException;


/*
@NamedQueries({
        */
/*@NamedQuery(name="JournalDokument.findByDocumentId",
                query="SELECT t FROM MOCK_JOARK_JOURNALDOKUMENTER t WHERE t.dokument_id = ':dokument_id'"),
        @NamedQuery(name="JournalDokument.findByJournalpostId",
                query="SELECT t FROM MOCK_JOARK_JOURNALDOKUMENTER t WHERE t.journalpost_id = :'journalpost_id'"),*//*


})
*/

@Entity(name = "JournalDokument")
@Table(name = "JOURNALDOKUMENT")
public class JournalDokument {

    @Id
    @Column(name = "ID", nullable = false)
    long id;

    @Column(name = "BRUKER_FNR")
    String brukerFnr;

    @Column(name = "DOKUMENT_ID")
    String dokumentId;

    @Column(name = "JOURNALPOST_ID")
    String journalpostId;

    @Column(name = "SAK_ID")
    String sakId;

    @Column(name = "DOKUMENT", nullable = false)
    byte[] dokument;

    @Column(name = "DOKUMENTTYPE")
    String dokumentType;

    @Column(name = "FILTYPE")
    String filType;

    @Column(name = "VARIANTFORMAT")
    String variantformat;

    @Column(name = "KOMMUNIKASJONSKANAL")
    String kommunikasjonskanal;

    @Column(name = "TILKNYTTET_JP_SOM")
    String tilknJpSom;

    @Column(name = "DATO_JP_MOTTATT")
    LocalDateTime datoMottatt;

    @Column(name = "ARKIVTEMA")
    String arkivtema;

    @Column(name ="KATEGORI")
    String kategori;

    @Column(name = "JOURNALTILSTAND")
    String journaltilstand;

    @Column(name = "DOKUMENTTILSTAND")
    String dokumenttilstand;

    @Column(name = "FAGSYSTEM")
    String fagsystem;

    JournalDokument(){
    }

    //TODO (rune) rydd opp det som evt ikke brukes:

    public String getBrukerFnr() {
        return brukerFnr;
    }

    public void setBrukerFnr(String brukerFnr) {
        this.brukerFnr = brukerFnr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDokumentId() {
        return dokumentId;
    }

    public void setDokumentId(String dokumentId) {
        this.dokumentId = dokumentId;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public String getSakId() {
        return sakId;
    }

    public void setSakId(String sakId) {
        this.sakId = sakId;
    }

    public byte[] getDokument() {
        return dokument;
    }

    public void setDokument(byte[] dokument) {
        this.dokument = dokument;
    }

    public String getDokumentType() {
        return dokumentType;
    }

    public void setDokumentType(String dokumentType) {
        this.dokumentType = dokumentType;
    }

    public String getFilType() {
        return filType;
    }

    public void setFilType(String filType) {
        this.filType = filType;
    }

    public String getVariantformat() {
        return variantformat;
    }

    public void setVariantformat(String variantformat) {
        this.variantformat = variantformat;
    }

    public String getTilknJpSom() {
        return tilknJpSom;
    }

    public void setTilknJpSom(String tilknJpSom) {
        this.tilknJpSom = tilknJpSom;
    }

    public String getKommunikasjonskanal() {
        return kommunikasjonskanal;
    }

    public void setKommunikasjonskanal(String kommunikasjonskanal) {
        this.kommunikasjonskanal = kommunikasjonskanal;
    }

    public LocalDateTime getDatoMottatt() {
        return datoMottatt;
    }

    public void setDatoMottatt(LocalDateTime datoMottatt) {
        this.datoMottatt = datoMottatt;
    }

    public String getArkivtema() {
        return arkivtema;
    }

    public void setArkivtema(String arkivtema) {
        this.arkivtema = arkivtema;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJournaltilstand() {
        return journaltilstand;
    }

    public void setJournaltilstand(String journaltilstand) {
        this.journaltilstand = journaltilstand;
    }

    public String getDokumenttilstand() {
        return dokumenttilstand;
    }

    public void setDokumenttilstand(String dokumenttilstand) {
        this.dokumenttilstand = dokumenttilstand;
    }

    public String getFagsystem() {
        return fagsystem;
    }

    public void setFagsystem(String fagsystem) {
        this.fagsystem = fagsystem;
    }
}
