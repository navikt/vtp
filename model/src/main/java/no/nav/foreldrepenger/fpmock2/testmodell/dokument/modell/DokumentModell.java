package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Dokumentkategori;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

public class DokumentModell {

    private String dokumentId;
    private DokumenttypeId dokumenttypeId;
    private Boolean erSensitiv;
    private String tittel;
    private String innhold;
    private DokumentTilknyttetJournalpost dokumentTilknyttetJournalpost;
    private List<DokumentVariantInnhold> dokumentVariantInnholdListe = new ArrayList<>();
    private Dokumentkategori dokumentkategori;



    public String getDokumentId() {
        return dokumentId;
    }

    public void setDokumentId(String dokumentId) {
        this.dokumentId = dokumentId;
    }

    public DokumenttypeId getDokumentType() {
        return dokumenttypeId;
    }

    public void setDokumentType(DokumenttypeId dokumentType) {
        this.dokumenttypeId = dokumentType;
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

    public DokumentTilknyttetJournalpost getDokumentTilknyttetJournalpost() {
        return dokumentTilknyttetJournalpost;
    }

    public void setDokumentTilknyttetJournalpost(DokumentTilknyttetJournalpost dokumentTilknyttetJournalpost) {
        this.dokumentTilknyttetJournalpost = dokumentTilknyttetJournalpost;
    }

    public List<DokumentVariantInnhold> getDokumentVariantInnholdListe() {
        return dokumentVariantInnholdListe;
    }

    public void setDokumentVariantInnholdListe(List<DokumentVariantInnhold> dokumentVariantInnholdListe) {
        this.dokumentVariantInnholdListe = dokumentVariantInnholdListe;
    }

    public Dokumentkategori getDokumentkategori() { return dokumentkategori; }

    public void setDokumentkategori(Dokumentkategori dokumentkategori) { this.dokumentkategori = dokumentkategori;}
}
