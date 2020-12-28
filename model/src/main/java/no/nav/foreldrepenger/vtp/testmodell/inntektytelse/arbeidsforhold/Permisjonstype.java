package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Permisjonstype {
    PERMISJON("permisjon"),
    PERMISJON_MED_FORELDREPENGER("permisjonMedForeldrepenger"),
    PERMISJON_VED_MILITÆRTJENESTE("permisjonVedMilitaertjeneste"),
    PERMITTERING("permittering"),
    UTDANNINGSPERMISJON("utdanningspermisjon"),
    VELFERDSPERMISJON("velferdspermisjon");

    private final String kode;

    Permisjonstype(String kode) {
        this.kode = kode;
    }

    @JsonValue
    public String getKode() {
        return kode;
    }
}
