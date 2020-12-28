package no.nav.foreldrepenger.vtp.testmodell;

public enum Feilkode {
    UGYLDIG_INPUT("UgyldigInput"),
    PERSON_IKKE_FUNNET("PersonIkkeFunnet"),
    SIKKERHET_BEGRENSNING("Sikkerhetsbegrensning");

    private final String termnavn;

    Feilkode(String termnavn) {
        this.termnavn = termnavn;
    }

    public String getTermnavn() {
        return termnavn;
    }

}
