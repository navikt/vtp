package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Infotrygd stønadsklasse 1.
 *
 * @see https://confluence.adeo.no/pages/viewpage.action?pageId=220537850
 */
public enum InfotrygdTema {
    FA("Foreldrepenger"),
    EF("Enslig forsørger"),
    SP("Sykepenger"),
    BS("Barns sykdom");

    @JsonValue
    private final String kode;

    InfotrygdTema(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }

}
