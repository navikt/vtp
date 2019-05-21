package no.nav.foreldrepenger.autotest.klienter.fpsak.dokument.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DokumentListeEnhet {
    protected String dokumentId;
    protected String gjelderFor;
    protected String journalpostId;
    protected String kommunikasjonsretning;
    protected String tittel;
    protected LocalDate tidspunkt;
    
    
}
