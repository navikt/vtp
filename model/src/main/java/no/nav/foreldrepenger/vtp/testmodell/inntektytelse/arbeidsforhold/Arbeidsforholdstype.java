package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Arbeidsforholdstype {
    ORDINÆRT_ARBEIDSFORHOLD("ordinaertArbeidsforhold"),
    MARITIMT_ARBEIDSFORHOLD("maritimtArbeidsforhold"),
    FRILANSER_OPPDRAGSTAKER_MED_MER("frilanserOppdragstakerHonorarPersonerMm"),
    FORENKLET_OPPGJØRSORDNING("forenkletOppgjoersordning");

    private final String kode;

    Arbeidsforholdstype(String kode) {
        this.kode = kode;
    }

    @JsonValue
    public String getKode() {
        return kode;
    }
}
