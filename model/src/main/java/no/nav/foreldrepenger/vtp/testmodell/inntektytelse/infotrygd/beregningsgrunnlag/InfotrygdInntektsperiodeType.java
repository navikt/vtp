package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum InfotrygdInntektsperiodeType {
    D("Daglig"),
    U("Ukentlig"),
    F("Fjorten dager"),
    M("Måned"),
    Å("Årlig"),
    X("Fastsatt etter 25 prosent avvik"),
    Y("Premiegrunnlag oppdrastaker (gjelder de to første ukene)"),
    @JsonEnumDefaultValue
    UDEFINERT("-", "Ikke definert");

    private final String kode;
    private final String termnavn;

    InfotrygdInntektsperiodeType(String termnavn) {
        this(null, termnavn);
    }

    InfotrygdInntektsperiodeType(String kode, String termnavn) {
        this.kode = Optional.ofNullable(kode).orElse(name());
        this.termnavn = termnavn;
    }

    public String getKode() {
        return kode;
    }

    public String getTermnavn() {
        return termnavn;
    }
}
