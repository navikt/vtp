package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena;

public enum RelatertYtelseTema {
    AAP("arbeidsavklaringspenger"),
    DAG("Dagpenger"),
    FA("Foreldrepenger"),
    EF("Enslig forsørger"),
    SP("Sykepenger"),
    BS("Pårørende sykdom");

    private final String termnavn;

    RelatertYtelseTema(String termnavn) {
        this.termnavn = termnavn;
    }

    public String getTermnavn() {
        return termnavn;
    }
}
