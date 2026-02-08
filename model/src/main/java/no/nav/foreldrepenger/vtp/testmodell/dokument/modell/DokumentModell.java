package no.nav.foreldrepenger.vtp.testmodell.dokument.modell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DokumentModell {

    private String dokumentId;
    private DokumenttypeId dokumenttypeId;
    private Boolean erSensitiv;
    private String tittel;
    private String brevkode;
    private String innhold;
    private DokumentTilknyttetJournalpost dokumentTilknyttetJournalpost;
    private List<DokumentVariantInnhold> dokumentVariantInnholdListe = new ArrayList<>();

    public DokumentModell() {
    }

    @JsonCreator
    public DokumentModell(@JsonProperty("dokumentId") String dokumentId,
                          @JsonProperty("dokumenttypeId") DokumenttypeId dokumenttypeId,
                          @JsonProperty("erSensitiv") Boolean erSensitiv,
                          @JsonProperty("tittel") String tittel,
                          @JsonProperty("brevkode") String brevkode,
                          @JsonProperty("innhold") String innhold,
                          @JsonProperty("dokumentTilknyttetJournalpost") DokumentTilknyttetJournalpost dokumentTilknyttetJournalpost,
                          @JsonProperty("dokumentVariantInnholdListe") List<DokumentVariantInnhold> dokumentVariantInnholdListe) {
        this.dokumentId = dokumentId;
        this.dokumenttypeId = dokumenttypeId;
        this.erSensitiv = erSensitiv;
        this.tittel = tittel;
        this.brevkode = brevkode;
        this.innhold = innhold;
        this.dokumentTilknyttetJournalpost = dokumentTilknyttetJournalpost;
        this.dokumentVariantInnholdListe = dokumentVariantInnholdListe;
    }

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

    public String getBrevkode() {
        return brevkode;
    }

    public void setBrevkode(String brevkode) {
        this.brevkode = brevkode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DokumentModell that = (DokumentModell) o;
        return Objects.equals(dokumentId, that.dokumentId) && Objects.equals(dokumenttypeId, that.dokumenttypeId) && Objects.equals(erSensitiv, that.erSensitiv) && Objects.equals(tittel, that.tittel) && Objects.equals(brevkode, that.brevkode) && Objects.equals(innhold, that.innhold) && Objects.equals(dokumentTilknyttetJournalpost, that.dokumentTilknyttetJournalpost) && Objects.equals(dokumentVariantInnholdListe, that.dokumentVariantInnholdListe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dokumentId, dokumenttypeId, erSensitiv, tittel, brevkode, innhold, dokumentTilknyttetJournalpost, dokumentVariantInnholdListe);
    }

    @Override
    public String toString() {
        return "DokumentModell{" +
                "dokumentId='" + dokumentId + '\'' +
                ", dokumenttypeId=" + dokumenttypeId +
                ", erSensitiv=" + erSensitiv +
                ", tittel='" + tittel + '\'' +
                ", brevkode='" + brevkode + '\'' +
                ", innhold='" + innhold + '\'' +
                ", dokumentTilknyttetJournalpost=" + dokumentTilknyttetJournalpost +
                ", dokumentVariantInnholdListe=" + dokumentVariantInnholdListe +
                '}';
    }
}
