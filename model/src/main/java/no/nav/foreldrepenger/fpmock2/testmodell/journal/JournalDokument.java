package no.nav.foreldrepenger.fpmock2.testmodell.journal;

import java.time.LocalDateTime;

public class JournalDokument {

    private String brukerFnr;
    private String journaltilstand;
    private String dokumenttilstand;
    private String dokumentId;
    private String journalpostId;
    private byte[] dokument;
    private String sakId;
    private String dokumentType;
    private String kategori;
    private String tilknyttetJPSom;
    private String kommunikasjonskanal;
    private String kommunkasjonsretning;
    private String filType;
    private String variantformat;
    private String arkivtema;
    private LocalDateTime mottattDato;
    private String fagsystem;

    public String getBrukerFnr() {
        return brukerFnr;
    }

    public void setBrukerFnr(String brukerFnr) {
        this.brukerFnr = brukerFnr;
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

    public byte[] getDokument() {
        return dokument;
    }

    public void setDokument(byte[] dokument) {
        this.dokument = dokument;
    }

    public String getSakId() {
        return sakId;
    }

    public void setSakId(String sakId) {
        this.sakId = sakId;
    }

    public String getDokumentType() {
        return dokumentType;
    }

    public void setDokumentType(String dokumentType) {
        this.dokumentType = dokumentType;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTilknyttetJPSom() {
        return tilknyttetJPSom;
    }

    public void setTilknyttetJPSom(String tilknyttetJPSom) {
        this.tilknyttetJPSom = tilknyttetJPSom;
    }

    public String getKommunikasjonskanal() {
        return kommunikasjonskanal;
    }

    public void setKommunikasjonskanal(String kommunikasjonskanal) {
        this.kommunikasjonskanal = kommunikasjonskanal;
    }

    public String getKommunkasjonsretning() {
        return kommunkasjonsretning;
    }

    public void setKommunkasjonsretning(String kommunkasjonsretning) {
        this.kommunkasjonsretning = kommunkasjonsretning;
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

    public String getArkivtema() {
        return arkivtema;
    }

    public void setArkivtema(String arkivtema) {
        this.arkivtema = arkivtema;
    }

    public LocalDateTime getMottattDato() {
        return mottattDato;
    }

    public void setMottattDato(LocalDateTime mottattDato) {
        this.mottattDato = mottattDato;
    }

    public String getFagsystem() {
        return fagsystem;
    }

    public void setFagsystem(String fagsystem) {
        this.fagsystem = fagsystem;
    }
}
