package no.nav.tjeneste.virksomhet.journalmodell;

//import org.hibernate.annotationsNamedQueries;
//import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    Long id;

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

    @Column(name = "DOKUMENTTYPEID")
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

    @Column(name = "DATO_JP_FERDISTILT")
    LocalDateTime datoFerdigstillt;

    @Column(name = "JOURNALSTATUS")
    String journalStatus;

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

    @Column(name = "KOMMUNIKASJONSRETNING")
    String kommunikasjonsretning;


    //TODO (rune) skal fjernes
    @Column(name = "FEILKODE")
    String feilkode;

    @Column(name = "FEILKODE_FERDIGSTILLJF")
    String feilkodeFerdigstillJournalfoering;

    @Column(name = "FEILKODE_OPPDATERJOURNALPOST")
    String feilkodeOppdaterJournalpost;

    @Column(name = "FEILKODE_HENTJOURNALPOST")
    String feilkodeHentJournalpost;

    @Column(name = "FEILKODE_UTLEDJFBEHOV")
    String feilkodeUtledJournalfoeringsbehov;

    JournalDokument(){
    }

    public String getBrukerFnr() {
        return brukerFnr;
    }

    public Long getId() {
        return id;
    }

    public String getDokumentId() {
        return dokumentId;
    }

    public String getJournalpostId() {
        return journalpostId;
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

    public String getDokumentType() {
        return dokumentType;
    }

    public String getFilType() {
        return filType;
    }

    public String getVariantformat() {
        return variantformat;
    }

    public String getTilknJpSom() {
        return tilknJpSom;
    }

    public String getKommunikasjonskanal() {
        return kommunikasjonskanal;
    }

    public LocalDateTime getDatoMottatt() {
        return datoMottatt;
    }

    public LocalDateTime getDatoFerdigstillt() {
        return datoFerdigstillt;
    }

    public void setDatoFerdigstillt(LocalDateTime datoFerdigstillt) {
        this.datoFerdigstillt = datoFerdigstillt;
    }

    public String getArkivtema() {
        return arkivtema;
    }

    public String getKategori() {
        return kategori;
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

    public String getKommunikasjonsretning() {
        return kommunikasjonsretning;
    }

    public String getJournalStatus() {
        return journalStatus;
    }

    public void setJournalStatus(String journalStatus) {
        this.journalStatus = journalStatus;
    }

    public String getFeilkodeFerdigstillJournalfoering() {
        return feilkodeFerdigstillJournalfoering;
    }

    public String getFeilkodeOppdaterJournalpost() {
        return feilkodeOppdaterJournalpost;
    }

    public String getFeilkodeHentJournalpost() {
        return feilkodeHentJournalpost;
    }

    public String getFeilkodeUtledJournalfoeringsbehov() {
        return feilkodeUtledJournalfoeringsbehov;
    }
}
