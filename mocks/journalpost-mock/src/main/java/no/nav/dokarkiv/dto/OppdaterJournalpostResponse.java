package no.nav.dokarkiv.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OppdaterJournalpostResponse(String journalpostId) {
}
