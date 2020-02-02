package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum TemaKode {
    @JsonEnumDefaultValue
    UKJENT,
    FA,
    SP,
    BS
}
