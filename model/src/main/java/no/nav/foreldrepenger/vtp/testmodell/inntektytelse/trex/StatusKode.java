package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum StatusKode {
    @JsonEnumDefaultValue
    UKJENT,
    L,
    A,
    I
}
