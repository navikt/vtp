package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Avlønningstype {
    FASTLØNN("fastlønn");

    @JsonValue
    private final String kode;

    Avlønningstype(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }
}
