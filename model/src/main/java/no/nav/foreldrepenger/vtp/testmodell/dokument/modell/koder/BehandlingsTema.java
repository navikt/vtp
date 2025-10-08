package no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BehandlingsTema {

    ENGANGSSTØNAD("ENGST", "ab0327", "Engangsstønad"),
    ENGANGSSTØNAD_FØDSEL("ENGST_FODS", "ab0050", "Engangsstønad ved fødsel"),
    ENGANGSSTØNAD_ADOPSJON("ENGST_ADOP", "ab0027", "Engangsstønad ved adopsjon"),

    FORELDREPENGER("FORP", "ab0326", "Foreldrepenger"),
    FORELDREPENGER_FØDSEL("FORP_FODS", "ab0047", "Foreldrepenger ved fødsel"),
    FORELDREPENGER_ADOPSJON("FORP_ADOP", "ab0072", "Foreldrepenger ved adopsjon"),

    SVANGERSKAPSPENGER("SVP", "ab0126", "Svangerskapspenger"),

    OMS("OMS", "ab0271", "Omsorgspenger, Pleiepenger og opplæringspenger"),
    OMS_OMSORG("OMS_OMSORG", "ab0149", "Omsorgspenger"),
    OMS_OPP("OMS_OPP", "ab0141", "Opplæringspenger"),
    OMS_PLEIE_BARN("OMS_PLEIE_BARN", "ab0069", "Pleiepenger sykt barn"),
    OMS_PLEIE_BARN_NY("OMS_PLEIE_BARN_NY", "ab0320", "Pleiepenger sykt barn ny ordning"),
    OMS_PLEIE_INSTU("OMS_PLEIE_INSTU", "ab0153", "Pleiepenger ved institusjonsopphold"),

    UDEFINERT("-", "-", "Ikke definert"),
    ;

    private final String kode;
    private final String offisiellKode;
    private final String termnavn;

    BehandlingsTema(String kode, String offisiellKode, String termnavn) {
        this.kode = kode;
        this.offisiellKode = offisiellKode;
        this.termnavn = termnavn;
    }

    @JsonCreator
    public static BehandlingsTema fraKode(String kode) {
        return Arrays.stream(BehandlingsTema.values())
                .filter(value -> value.getOffisiellKode().equalsIgnoreCase(kode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ikke støttet BehandlingsTema " + kode));
    }

    public String getKode() {
        return kode;
    }

    public String getOffisiellKode() {
        return offisiellKode;
    }

    public String getTermnavn() {
        return termnavn;
    }
}
