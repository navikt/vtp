package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalpostMottak {

    public String saksnummer;
    public String journalpostId;
    public String forsendelseId;
    public String behandlingstemaOffisiellKode;
    public String dokumentTypeIdOffisiellKode;
    public String forsendelseMottatt;
    public String payloadXml;
    public Integer payloadLength;
    
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
}
