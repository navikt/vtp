package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

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
}
