package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum BehandlingstemaKode {
    @JsonEnumDefaultValue
    UKJENT,
    AP,
    FP,
    FU,
    FØ,
    SV,
    SP,
    OM,
    PB,
    OP,
    PP,
    PI,
    PN
}
