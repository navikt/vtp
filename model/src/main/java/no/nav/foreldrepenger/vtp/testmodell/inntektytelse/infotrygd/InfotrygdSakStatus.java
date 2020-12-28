package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd;

public enum InfotrygdSakStatus {
    IP("Ikke påbegynt"),
    UB("Under behandling"),
    SG("Sendt til saksbehandler"),
    UK("Underkjent av saksbehandler"),
    RT("Returnert"),
    FB("Ferdig behandlet"),
    FI( "Ferdig iverksatt"),
    RF("Returnert feilsendt"),
    ST("Sendt"),
    VD("Videresendt direktoratet"),
    VI("Venter iverksetting"),
    VT("Videresendt trygderetten"),
    L("Løpende"),
    A("Avsluttet"),
    I("Ikke Startet");

    private final String termnavn;

    InfotrygdSakStatus(String termnavn) {
        this.termnavn = termnavn;
    }

    public String getTermnavn() {
        return termnavn;
    }
}
