package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum InntektsperiodeKode {
    @JsonEnumDefaultValue
    UKJENT,
    M,
    U,
    D,
    Å,
    F,
    X,
    Y
}
