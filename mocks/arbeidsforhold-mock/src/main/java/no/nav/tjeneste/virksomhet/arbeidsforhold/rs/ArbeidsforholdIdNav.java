package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

public final class ArbeidsforholdIdNav {

    private static Long arbeidsforholdIdNav = 10000L;

    private ArbeidsforholdIdNav() {
    }

    public static long next() {
        return ++arbeidsforholdIdNav;
    }
}
