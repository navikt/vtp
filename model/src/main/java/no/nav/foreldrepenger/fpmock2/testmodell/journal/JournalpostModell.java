package no.nav.foreldrepenger.fpmock2.testmodell.journal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.fpmock2.testmodell.journal.dokument.DokumentModell;

public class JournalpostModell {

    private List<DokumentModell> dokumentModellList = new ArrayList<>();
    private String avsenderFnr;
    private String sakId;
    private String fagsystemId;
    private String journalStatus;
    private String kommunikasjonsretning;
    private LocalDateTime mottattDato;
    private String mottakskanal;
    private String arkivtema;
    private String journaltilstand;

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

    public String getJournalStatus() {
        return journalStatus;
    }

    public void setJournalStatus(String journalStatus) {
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

    public String getArkivtema() {
        return arkivtema;
    }

    public void setArkivtema(String arkivtema) {
        this.arkivtema = arkivtema;
    }

    public String getJournaltilstand() {
        return journaltilstand;
    }

    public void setJournaltilstand(String journaltilstand) {
        this.journaltilstand = journaltilstand;
    }
}
