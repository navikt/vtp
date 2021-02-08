package no.nav.tjeneste.virksomhet.infotrygd.rest.saker;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum SakResultat {
    @JsonEnumDefaultValue
    UKJENT,
    A,
    AK,
    AV,
    DI,
    DT,
    FB,
    FI,
    H,
    HB,
    I,
    IN,
    IS,
    IT,
    MO,
    MT,
    NB,
    O,
    PA,
    R,
    SB,
    TB,
    TH,
    TO,
    UB,
    Ø;

    private SakResultat() {
    }
}
