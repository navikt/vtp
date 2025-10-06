package no.nav.foreldrepenger.vtp.testmodell.dokument.modell;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivtema;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.BrukerType;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalposttyper;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Mottakskanal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JournalpostModell {

    private String journalpostId;
    private String eksternReferanseId;
    private String tittel;
    private List<DokumentModell> dokumentModellList = new ArrayList<>();
    private JournalpostBruker avsenderMottaker;
    private String sakId;
    private String fagsystemId;
    private Journalstatus journalStatus;
    private String kommunikasjonsretning;
    private LocalDateTime mottattDato;
    private Mottakskanal mottakskanal;
    private Arkivtema arkivtema;
    private String journaltilstand;
    private Journalposttyper journalposttype;
    private JournalpostBruker bruker;
    private String behandlingTema;
    private List<Tilleggsopplysning> tilleggsopplysninger = new ArrayList<>();

    public JournalpostModell() {
    }

    @JsonCreator
    public JournalpostModell(@JsonProperty("journalpostId") String journalpostId,
                             @JsonProperty("eksternReferanseId") String eksternReferanseId,
                             @JsonProperty("tittel") String tittel,
                             @JsonProperty("dokumentModellList") List<DokumentModell> dokumentModellList,
                             @JsonProperty("avsenderFnr") String avsenderFnr,
                             @JsonProperty("sakId") String sakId,
                             @JsonProperty("fagsystemId") String fagsystemId,
                             @JsonProperty("journalStatus") Journalstatus journalStatus,
                             @JsonProperty("kommunikasjonsretning") String kommunikasjonsretning,
                             @JsonProperty("mottattDato") LocalDateTime mottattDato,
                             @JsonProperty("mottakskanal") Mottakskanal mottakskanal,
                             @JsonProperty("arkivtema") Arkivtema arkivtema,
                             @JsonProperty("journaltilstand") String journaltilstand,
                             @JsonProperty("journalposttype") Journalposttyper journalposttype,
                             @JsonProperty("bruker") JournalpostBruker bruker,
                             @JsonProperty("behandlingTema") String behandlingTema) {
        this.journalpostId = journalpostId;
        this.eksternReferanseId = eksternReferanseId;
        this.tittel = tittel;
        this.dokumentModellList = dokumentModellList;
        this.avsenderMottaker = avsenderFnr != null ? new JournalpostBruker(avsenderFnr, BrukerType.FNR) : null;
        this.sakId = sakId;
        this.fagsystemId = fagsystemId;
        this.journalStatus = journalStatus;
        this.kommunikasjonsretning = kommunikasjonsretning;
        this.mottattDato = mottattDato;
        this.mottakskanal = mottakskanal;
        this.arkivtema = arkivtema;
        this.journaltilstand = journaltilstand;
        this.journalposttype = journalposttype;
        this.bruker = bruker;
        this.behandlingTema = behandlingTema;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public String getEksternReferanseId() {
        return eksternReferanseId;
    }

    public void setEksternReferanseId(String eksternReferanseId) {
        this.eksternReferanseId = eksternReferanseId;
    }

    public void setTittel(String tittel) {
        this.tittel = tittel;
    }

    public String getTittel() {
        return tittel;
    }

    public List<DokumentModell> getDokumentModellList() {
        return dokumentModellList;
    }

    public void setDokumentModellList(List<DokumentModell> dokumentModellList) {
        this.dokumentModellList = dokumentModellList;
    }

    public String getAvsenderFnr() {
        if (avsenderMottaker != null && avsenderMottaker.getBrukerType() == BrukerType.FNR) {
            return avsenderMottaker.getIdent();
        } else return null;
    }

    public void setAvsenderFnr(String avsenderFnr) {
        this.avsenderMottaker = new JournalpostBruker(avsenderFnr, BrukerType.FNR);
    }

    public String getSakId() {
        return sakId;
    }

    public void setSakId(String sakId) {
        this.sakId = sakId;
    }

    public String getFagsystemId() {
        return fagsystemId;
    }

    public void setFagsystemId(String fagsystemId) {
        this.fagsystemId = fagsystemId;
    }

    public Journalstatus getJournalStatus() {
        return journalStatus;
    }

    public void setJournalStatus(Journalstatus journalStatus) {
        this.journalStatus = journalStatus;
    }

    public String getKommunikasjonsretning() {
        return kommunikasjonsretning;
    }

    public void setKommunikasjonsretning(String kommunikasjonsretning) {
        this.kommunikasjonsretning = kommunikasjonsretning;
    }

    public LocalDateTime getMottattDato() {
        return mottattDato;
    }

    public void setMottattDato(LocalDateTime mottattDato) {
        this.mottattDato = mottattDato;
    }

    public Mottakskanal getMottakskanal() {
        return mottakskanal;
    }

    public void setMottakskanal(Mottakskanal mottakskanal) {
        this.mottakskanal = mottakskanal;
    }

    public Arkivtema getArkivtema() {
        return arkivtema;
    }

    public void setArkivtema(Arkivtema arkivtema) {
        this.arkivtema = arkivtema;
    }

    public String getJournaltilstand() {
        return journaltilstand;
    }

    public void setJournaltilstand(String journaltilstand) {
        this.journaltilstand = journaltilstand;
    }

    public Journalposttyper getJournalposttype() {
        return journalposttype;
    }

    public void setJournalposttype(Journalposttyper journalposttype) {
        this.journalposttype = journalposttype;
    }

    public JournalpostBruker getBruker() {
        return bruker;
    }

    public void setBruker(JournalpostBruker bruker) {
        this.bruker = bruker;
    }

    public String getBehandlingTema() {
        return behandlingTema;
    }

    public void setBehandlingTema(String behandlingTema) {
        this.behandlingTema = behandlingTema;
    }

    public List<Tilleggsopplysning> getTilleggsopplysninger() {
        return tilleggsopplysninger;
    }

    public void setTilleggsopplysninger(List<Tilleggsopplysning> tilleggsopplysninger) {
        this.tilleggsopplysninger = tilleggsopplysninger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalpostModell that = (JournalpostModell) o;
        return Objects.equals(journalpostId, that.journalpostId) && Objects.equals(eksternReferanseId, that.eksternReferanseId) && Objects.equals(tittel, that.tittel) && Objects.equals(dokumentModellList, that.dokumentModellList) && Objects.equals(avsenderMottaker, that.avsenderMottaker) && Objects.equals(sakId, that.sakId) && Objects.equals(fagsystemId, that.fagsystemId) && Objects.equals(journalStatus, that.journalStatus) && Objects.equals(kommunikasjonsretning, that.kommunikasjonsretning) && Objects.equals(mottattDato, that.mottattDato) && Objects.equals(mottakskanal, that.mottakskanal) && Objects.equals(arkivtema, that.arkivtema) && Objects.equals(journaltilstand, that.journaltilstand) && Objects.equals(journalposttype, that.journalposttype) && Objects.equals(bruker, that.bruker) && Objects.equals(tilleggsopplysninger, that.tilleggsopplysninger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(journalpostId, eksternReferanseId, tittel, dokumentModellList, avsenderMottaker, sakId, fagsystemId, journalStatus, kommunikasjonsretning, mottattDato, mottakskanal, arkivtema, journaltilstand, journalposttype, bruker, tilleggsopplysninger);
    }

    @Override
    public String toString() {
        return "JournalpostModell{" +
                "journalpostId='" + journalpostId + '\'' +
                ", dokumentModellList=" + dokumentModellList +
                ", avsenderMottaker='" + avsenderMottaker.getIdent() + '\'' +
                ", sakId='" + sakId + '\'' +
                ", fagsystemId='" + fagsystemId + '\'' +
                ", journalStatus=" + journalStatus +
                ", kommunikasjonsretning='" + kommunikasjonsretning + '\'' +
                ", mottattDato=" + mottattDato +
                ", mottakskanal='" + mottakskanal + '\'' +
                ", arkivtema=" + arkivtema +
                ", journaltilstand='" + journaltilstand + '\'' +
                ", journalposttype=" + journalposttype +
                ", tilleggsopplysninger=" + tilleggsopplysninger +
                '}';
    }

    public JournalpostBruker getAvsenderMottaker() {
        return avsenderMottaker;
    }

    public void setAvsenderMottaker(JournalpostBruker avsenderMottaker) {
        this.avsenderMottaker = avsenderMottaker;
    }
}
