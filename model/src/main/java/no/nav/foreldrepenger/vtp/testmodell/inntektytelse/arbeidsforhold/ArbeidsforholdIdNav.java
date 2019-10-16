package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

public final class ArbeidsforholdIdNav {

    private static Long arbeidsforholdIdNav = 10000L;

    private ArbeidsforholdIdNav() {
    }

    public static long next() {
        return ++arbeidsforholdIdNav;
    }
}
