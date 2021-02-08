package no.nav.tjeneste.virksomhet.infotrygd.rest.saker;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum SakType {
    @JsonEnumDefaultValue
    UKJENT,
    S,
    R,
    K,
    A;

    private SakType() {
    }
}
