package no.nav.dokarkiv.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GjenaapneSakRequest(
        String tema,
        String fagsakId,
        String fagsaksystem,
        Bruker bruker
) {

    public GjenaapneSakRequest {
        Objects.requireNonNull(tema, "tema cannot be null");
        Objects.requireNonNull(fagsakId, "fagsakId cannot be null");
        Objects.requireNonNull(fagsaksystem, "fagsaksystem cannot be null");
        Objects.requireNonNull(bruker, "bruker cannot be null");
    }
}
