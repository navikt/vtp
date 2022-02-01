package no.nav.mock.pesys.dto;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum UføreTypeCode {
    @JsonEnumDefaultValue
    UKJENT,
    /**
     * Uføre
     */
    UFORE,
    /**
     * Uføre m/yrkesskade
     */
    UF_M_YRKE,
    /**
     * Første virkningsdato, ikke ufør
     */
    VIRK_IKKE_UFOR,
    /**
     * Yrkesskade
     */
    YRKE;
}
