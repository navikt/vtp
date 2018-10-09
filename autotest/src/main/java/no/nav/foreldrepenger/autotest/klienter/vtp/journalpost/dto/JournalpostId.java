package no.nav.foreldrepenger.autotest.klienter.vtp.journalpost.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalpostId {
    protected String journalpostId;
    
    public JournalpostId() {
        
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }
    
    
}
