package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Avlønningstype {
    FASTLØNN("fastlønn");

    private final String kode;

    Avlønningstype(String kode) {
        this.kode = kode;
    }

    @JsonValue
    public String getKode() {
        return kode;
    }
}
