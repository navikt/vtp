package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena;

public enum VedtakStatus {
    IVERK("Iverksatt"),
    OPPRE("Opprettet"),
    INNST("Innstilt"),
    REGIS("Registrert"),
    MOTAT("Mottatt"),
    GODKJ("Godkjent"),
    AVSLU("Avsluttet");

    private final String termnavn;

    VedtakStatus(String termnavn) {
        this.termnavn = termnavn;
    }

    public String getTermnavn() {
        return termnavn;
    }
}
