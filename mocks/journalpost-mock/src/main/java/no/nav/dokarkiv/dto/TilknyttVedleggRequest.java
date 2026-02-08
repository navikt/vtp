package no.nav.dokarkiv.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TilknyttVedleggRequest(List<DokumentTilknytt> dokument, String tilknyttetAvNavn) {

    public record DokumentTilknytt(String kildeJournalpostId, String dokumentInfoId, Integer rekkefoelge) {
    }
}
