package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalpostMottak {

    protected String saksnummer;
    protected String journalpostId;
    protected String forsendelseId;
    protected String behandlingstemaOffisiellKode;
    private String dokumentTypeIdOffisiellKode;
    protected String forsendelseMottatt;
    private String payloadXml;
    private Integer payloadLength;
    
    public JournalpostMottak(String saksnummer, String journalpostId, LocalDate forsendelseMottatt,
            String behandlingstemaOffisiellKode) {
        this(saksnummer, journalpostId, forsendelseMottatt.toString(), behandlingstemaOffisiellKode);
    }
    
    public JournalpostMottak(String saksnummer, String journalpostId, String forsendelseMottatt,
            String behandlingstemaOffisiellKode) {
        super();
        this.saksnummer = saksnummer;
        this.journalpostId = journalpostId;
        this.forsendelseMottatt = forsendelseMottatt;
        this.behandlingstemaOffisiellKode = behandlingstemaOffisiellKode;
    }

    public String getDokumentTypeIdOffisiellKode() {
        return dokumentTypeIdOffisiellKode;
    }

    public void setDokumentTypeIdOffisiellKode(String dokumentTypeIdOffisiellKode) {
        this.dokumentTypeIdOffisiellKode = dokumentTypeIdOffisiellKode;
    }

    public String getPayloadXml() {
        return payloadXml;
    }

    public void setPayloadXml(String payloadXml) {
        this.payloadXml = payloadXml;
    }

    public Integer getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(Integer payloadLength) {
        this.payloadLength = payloadLength;
    }
}
