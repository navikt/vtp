package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InntektFordel {
    KONTANTYTELSE("kontantytelse"),
    UTGIFTSGODTGJØRELSE("utgiftsgodtgjørelse"),
    NATURALYTELSE("naturalytelse");

    private final String kode;

    InntektFordel(String kode) {
        this.kode = kode;
    }

    @JsonValue
    public String getKode() {
        return kode;
    }
}
