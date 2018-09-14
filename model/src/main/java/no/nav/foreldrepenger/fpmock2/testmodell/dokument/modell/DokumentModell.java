package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell;

import java.util.List;

public class DokumentModell {

    private String dokumentId;
    private String dokumentType;
    private Boolean erSensitiv;
    private String tittel;
    private String innhold;
    private String dokumentTilknyttetJournalpost;
    private List<DokumentVariantInnhold> dokumentVariantInnholdListe;


    public String getDokumentId() {
        return dokumentId;
    }

    public void setDokumentId(String dokumentId) {
        this.dokumentId = dokumentId;
    }

    public String getDokumentType() {
        return dokumentType;
    }

    public void setDokumentType(String dokumentType) {
        this.dokumentType = dokumentType;
    }

    public Boolean getErSensitiv() {
        return erSensitiv;
    }

    public void setErSensitiv(Boolean erSensitiv) {
        this.erSensitiv = erSensitiv;
    }

    public String getTittel() {
        return tittel;
    }

    public void setTittel(String tittel) {
        this.tittel = tittel;
    }

    public String getInnhold() {
        return innhold;
    }

    public void setInnhold(String innhold) {
        this.innhold = innhold;
    }

    public String getDokumentTilknyttetJournalpost() {
        return dokumentTilknyttetJournalpost;
    }

    public void setDokumentTilknyttetJournalpost(String dokumentTilknyttetJournalpost) {
        this.dokumentTilknyttetJournalpost = dokumentTilknyttetJournalpost;
    }

    public List<DokumentVariantInnhold> getDokumentVariantInnholdListe() {
        return dokumentVariantInnholdListe;
    }

    public void setDokumentVariantInnholdListe(List<DokumentVariantInnhold> dokumentVariantInnholdListe) {
        this.dokumentVariantInnholdListe = dokumentVariantInnholdListe;
    }
}
