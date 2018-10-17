package no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder;

//TODO: OL: Skriv om til klasse i konvensjon som ellers;
public enum Behandlingstema {

    ENGANGSSTONAD_ADOPSJON("ab0027"),
    ENGANGSSTONAD_FOEDSEL("ab0050"),
    ENGANGSSTONAD("ab0327"),
    FORELDREPENGER("ab0326"),
    FORELDREPENGER_FOEDSEL("ab0047"),
    FORELDREPENGER_ADOPSJON("ab0072");

    private String behandlingstemakode;

    Behandlingstema(String behandlingstemakode) { this.behandlingstemakode = behandlingstemakode;}

    public String value() { return name();}

    public static Behandlingstema fromValue(String v) { return valueOf(v);}

    public String getBehandlingstemakode() { return this.behandlingstemakode;}
}