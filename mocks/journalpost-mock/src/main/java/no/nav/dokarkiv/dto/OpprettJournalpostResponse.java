package no.nav.dokarkiv.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpprettJournalpostResponse(String journalpostId, boolean journalpostferdigstilt, List<DokumentInfoResponse> dokumenter) {

    public record DokumentInfoResponse(String dokumentInfoId) {
    }
}
