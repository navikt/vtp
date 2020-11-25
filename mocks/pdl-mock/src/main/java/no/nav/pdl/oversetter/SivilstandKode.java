package no.nav.pdl.oversetter;

public enum SivilstandKode {
    ENKE("ENKE_ELLER_ENKEMANN"),
    GIFT("GIFT"),
    GJPA("GJENLEVENDE_PARTNER"),
    GLAD("UOPPGITT"),
    REPA("REGISTRERT_PARTNER"),
    SAMB("REGISTRERT_PARTNER"),
    EPA("SEPARERT_PARTNER"),
    SEPR("SEPARERT"),
    SKIL("SKILT"),
    SKPA("SKILT_PARTNER"),
    UGIF("UGIFT");

    private final String sivilstandPDL;

    SivilstandKode(String sivilstandPDL) {
        this.sivilstandPDL = sivilstandPDL;
    }

    public String getSivilstandPDL() {
        return sivilstandPDL;
    }
}
