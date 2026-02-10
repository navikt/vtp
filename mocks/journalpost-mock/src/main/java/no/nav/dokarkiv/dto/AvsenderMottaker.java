package no.nav.dokarkiv.dto;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AvsenderMottaker(String id, AvsenderMottakerIdType idType, String navn) {

    public enum AvsenderMottakerIdType {
        @JsonEnumDefaultValue UKJENT,
        FNR,
        ORGNR,
        HPRNR,
        UTL_ORG
    }
}
