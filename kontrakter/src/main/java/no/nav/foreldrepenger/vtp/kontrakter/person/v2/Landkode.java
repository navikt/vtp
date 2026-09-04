package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.util.Locale;
import java.util.Set;

/**
 * Normaliserer ISO 3166-1 alpha-2 og alpha-3 til uppercase alpha-3.
 * Støtte for alpha-2 er midlertidig og kan fjernes når alle klienter sender alpha-3.
 */
public final class Landkode {

    private static final Set<String> ALPHA2 = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2);
    private static final Set<String> ALPHA3 = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3);

    private Landkode() {
    }

    public static String normaliser(String landkode) {
        if (landkode == null) {
            return null;
        }
        var normalisert = landkode.toUpperCase(Locale.ROOT);
        if (ALPHA2.contains(normalisert)) {
            return Locale.of("", normalisert).getISO3Country();
        }
        if (ALPHA3.contains(normalisert)) {
            return normalisert;
        }
        throw new IllegalArgumentException("Ugyldig landkode");
    }
}
