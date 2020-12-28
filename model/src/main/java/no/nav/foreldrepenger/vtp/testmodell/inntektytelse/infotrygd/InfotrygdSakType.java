package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum InfotrygdSakType {
    S("Søknad"),
    R("Revurdering"),
    T("Tilbakebetalingssak"),
    A("Anke"),
    K("Klage"),
    I("Informasjonssak"),
    J("Journalsak"),
    DF("Dispensasjon foreldelse"),
    DI("dokumentinnsyn"),
    EG("etterlyse girokort"),
    FS("forespørsel"),
    JP("Journalsak fra privatperson"),
    JT("Journalsak fra trygdekontor"),
    JU("Journalsak fra utenl trm"),
    KE("Klage ettergivelse"),
    KS("Kontrollsak"),
    KT("Klage tilbakebetaling"),
    SE("Søknad om ettergivelse"),
    SV("Strafferettslig vurdering"),
    TE("Tilbakebetaling endring"),
    TK("Tidskonto"),
    TU("Tipsutredning"),
    UA("Utbetalt til annen"),
    @JsonEnumDefaultValue
    UDEFINERT("-", "Ikke definert");

    private final String kode;
    private final String termnavn;

    InfotrygdSakType(String termnavn) {
        this(null, termnavn);
    }

    InfotrygdSakType(String kode, String termnavn) {
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
