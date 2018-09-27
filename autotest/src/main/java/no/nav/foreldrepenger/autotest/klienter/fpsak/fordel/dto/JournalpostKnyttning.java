package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalpostKnyttning {
    
    protected Saksnummer saksnummerDto;
    protected JournalpostId journalpostIdDto;
    
    public JournalpostKnyttning(Saksnummer saksnummerDto, JournalpostId journalpostId) {
        super();
        this.saksnummerDto = saksnummerDto;
        this.journalpostIdDto = journalpostId;
    }
}
