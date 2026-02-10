package no.nav.dokarkiv.dto;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Bruker(String id, BrukerIdType idType) {

    public enum BrukerIdType {
        @JsonEnumDefaultValue UKJENT,
        AKTOERID,
        FNR,
        ORGNR
    }
}
