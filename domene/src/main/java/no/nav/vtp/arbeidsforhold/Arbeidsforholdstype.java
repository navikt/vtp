package no.nav.vtp.arbeidsforhold;

public enum Arbeidsforholdstype {
    ORDINÆRT_ARBEIDSFORHOLD("ordinaertArbeidsforhold"),
    MARITIMT_ARBEIDSFORHOLD("maritimtArbeidsforhold"),
    FRILANSER_OPPDRAGSTAKER_MED_MER("frilanserOppdragstakerHonorarPersonerMm"),
    FORENKLET_OPPGJØRSORDNING("forenkletOppgjoersordning");

    private final String kode;

    Arbeidsforholdstype(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }
}
