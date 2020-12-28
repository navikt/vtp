package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena;

public enum SakStatus {
    AKTIV("Aktiv"),
    INAKT("Inaktiv"),
    AVSLU("Avsluttet");

    private final String termnavn;

    SakStatus(String termnavn) {
        this.termnavn = termnavn;
    }

    public String getTermnavn() {
        return termnavn;
    }
}
