package no.nav.vtp.person.personopplysninger;

import java.util.Locale;
import java.util.Set;

public final class Landkode {

    public static final String NORGE = "NOR";
    private static final Set<String> ALPHA3 = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3);

    private Landkode() {
    }

    public static String normaliser(String landkode) {
        if (landkode == null) {
            return null;
        }
        var normalisert = landkode.toUpperCase(Locale.ROOT);
        if (ALPHA3.contains(normalisert)) {
            return normalisert;
        }
        throw new IllegalArgumentException("Ugyldig landkode '%s'. Kun landkoder med 3 tegn støttes".formatted(landkode));
    }
}
