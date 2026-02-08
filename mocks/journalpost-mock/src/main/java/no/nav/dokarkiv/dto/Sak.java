package no.nav.dokarkiv.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Sak(String fagsakId, String fagsaksystem, Sakstype sakstype) {

    public enum Sakstype {
        FAGSAK,
        GENERELL_SAK,
        @Deprecated ARKIVSAK;

    }
}
