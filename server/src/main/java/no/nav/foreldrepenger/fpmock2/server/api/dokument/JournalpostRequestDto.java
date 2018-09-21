package no.nav.foreldrepenger.fpmock2.server.api.dokument;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.JournalpostErketyper;

public class JournalpostRequestDto {

    private JournalpostErketyper journalpostType;
    private String XmlPayload;
    private String sokerId;

    public JournalpostErketyper getJournalpostType() {
        return journalpostType;
    }

    public void setJournalpostType(JournalpostErketyper journalpostType) {
        this.journalpostType = journalpostType;
    }

    public String getXmlPayload() {
        return XmlPayload;
    }

    public void setXmlPayload(String xmlPayload) {
        XmlPayload = xmlPayload;
    }

    public String getSokerId() {
        return sokerId;
    }

    public void setSokerId(String sokerId) {
        this.sokerId = sokerId;
    }
}
