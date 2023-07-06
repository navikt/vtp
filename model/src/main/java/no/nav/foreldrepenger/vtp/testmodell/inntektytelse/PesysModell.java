package no.nav.foreldrepenger.vtp.testmodell.inntektytelse;

public record PesysModell(Boolean harUføretrygd) {

    public PesysModell() {
        this(false);
    }
}
