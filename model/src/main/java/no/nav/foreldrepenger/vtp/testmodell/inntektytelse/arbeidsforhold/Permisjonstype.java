package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Permisjonstype {
    PERMISJON("permisjon"),
    PERMISJON_MED_FORELDREPENGER("permisjonMedForeldrepenger"),
    PERMISJON_VED_MILITÆRTJENESTE("permisjonVedMilitaertjeneste"),
    PERMITTERING("permittering"),

    UTDANNINGSPERMISJON("utdanningspermisjon"),
    UTDANNINGSPERMISJON_IKKE_LOVFESTET("utdanningspermisjonIkkeLovfestet"),
    UTDANNINGSPERMISJON_LOVFESTET("utdanningspermisjonLovfestet"),
    VELFERDSPERMISJON("velferdspermisjon"),
    ANNEN_PERMISJON_IKKE_LOVFESTET("andreIkkeLovfestedePermisjoner"),
    ANNEN_PERMISJON_LOVFESTET("andreLovfestedePermisjoner");

    private final String kode;

    Permisjonstype(String kode) {
        this.kode = kode;
    }

    @JsonValue
    public String getKode() {
        return kode;
    }
}
