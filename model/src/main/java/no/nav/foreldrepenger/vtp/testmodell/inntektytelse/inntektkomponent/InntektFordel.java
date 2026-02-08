package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InntektFordel {
    KONTANTYTELSE("kontantytelse"),
    UTGIFTSGODTGJØRELSE("utgiftsgodtgjørelse"),
    NATURALYTELSE("naturalytelse");

    @JsonValue
    private final String kode;

    InntektFordel(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }
}
