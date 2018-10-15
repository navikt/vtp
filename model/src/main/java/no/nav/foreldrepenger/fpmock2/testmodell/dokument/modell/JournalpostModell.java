package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Arkivtema;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Journalposttyper;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Journalstatus;

public class JournalpostModell {

    private String journalpostId;
    private List<DokumentModell> dokumentModellList = new ArrayList<>();
    private String avsenderFnr;
    private String sakId;
    private String fagsystemId;
    private Journalstatus journalStatus;
    private String kommunikasjonsretning;
    private LocalDateTime mottattDato;
    private String mottakskanal;
    private Arkivtema arkivtema;
    private String journaltilstand;
    private Journalposttyper journalposttype;

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public List<DokumentModell> getDokumentModellList() {
        return dokumentModellList;
    }

    public void setDokumentModellList(List<DokumentModell> dokumentModellList) {
        this.dokumentModellList = dokumentModellList;
    }

    public String getAvsenderFnr() {
        return avsenderFnr;
    }

    public void setAvsenderFnr(String avsenderFnr) {
        this.avsenderFnr = avsenderFnr;
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

    public String getMottakskanal() {
        return mottakskanal;
    }

    public void setMottakskanal(String mottakskanal) {
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

    public Journalposttyper getJournalposttype() { return journalposttype; }

    public void setJournalposttype(Journalposttyper journalposttype) { this.journalposttype = journalposttype;}
}
