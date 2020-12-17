package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Infotrygd ytelse sak resultat.
 *
 * Brukes ikke av SP, FA, BS, Pårørende sykdom, Pleiepenger (PN) har ikke opplysning om resultat.  Engangssstønad returnerer "I" (Innvilget) som standard.
 * Returneres kun for Enslig forsørger?
 *
 * @see https://confluence.adeo.no/pages/viewpage.action?pageId=220537850
 */
public enum InfotrygdSakResultat {
    A("Avslag"),
    AK("avvist klage"),
    AV("advarsel"),
    DI("delvis innvilget"),
    DT("delvis tilbakebetale"),
    FB("ferdigbehandlet"),
    FI("fortsatt innvilget"),
    H("henlagt / trukket tilbake"),
    HB("henlagt / bortfalt"),
    I("Innvilget"),
    IN("innvilget ny situasjon"),
    IS("ikke straffbart"),
    IT("ikke tilbakebetale"),
    MO("midlertidig opphørt"),
    MT("mottatt"),
    O("opphørt"),
    PA("politianmeldelse"),
    R("redusert"),
    SB("sak i bero"),
    TB("tilbakebetale"),
    TH("tips henlagt"),
    TO("tips oppfølging"),
    Ø("økning"),
    @JsonEnumDefaultValue
    UDEFINERT("?", "beslutningsstøtte Besl st");


    private final String kode;
    private final String termnavn;

    InfotrygdSakResultat(String termnavn) {
        this(null, termnavn);
    }

    InfotrygdSakResultat(String kode, String termnavn) {
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
