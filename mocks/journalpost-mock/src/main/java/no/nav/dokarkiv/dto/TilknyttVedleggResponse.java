package no.nav.dokarkiv.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TilknyttVedleggResponse(List<FeiletDokument> feiledeDokumenter) {

    public record FeiletDokument(String kildeJournalpostId, String dokumentInfoId, String arsakKode) {
    }
}
