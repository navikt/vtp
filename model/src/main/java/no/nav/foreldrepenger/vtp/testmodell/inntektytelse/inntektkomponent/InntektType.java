package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InntektType {
    LØNNSINNTEKT("Lønnsinntekt"),
    NÆRINGSINNTEKT("Næringsinntekt"),
    PENSJON_ELLER_TRYGD("PensjonEllerTrygd"),
    YTELSE_FRA_OFFENTLIGE("YtelseFraOffentlige");

    @JsonValue
    private final String kode;

    InntektType(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }
}
