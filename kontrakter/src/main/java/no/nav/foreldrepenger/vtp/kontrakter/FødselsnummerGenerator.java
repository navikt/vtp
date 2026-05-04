package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn;

public class FødselsnummerGenerator {
    private static final Set<String> BRUKTE_FØDSELSNUMMER = new ConcurrentSkipListSet<>();

    private static final Logger LOG = LoggerFactory.getLogger(FødselsnummerGenerator.class);
    private static final Integer NAV_SYNTETISK_IDENT_OFFSET_MND = 40;
    private static final int MAX_GENERATE_ATTEMPTS = 1000;

    private final Kjønn kjønn;
    private final LocalDate fødselsdato;

    private FødselsnummerGenerator(Builder fgb) {
        this.kjønn = Objects.requireNonNullElseGet(fgb.kjønn, FødselsnummerGenerator::randomKjønn);
        this.fødselsdato = Objects.requireNonNullElseGet(fgb.fødselsdato,
                FødselsnummerGenerator::generateRandomPlausibleBirthdayParent);
    }

    private static Kjønn randomKjønn() {
        return ThreadLocalRandom.current().nextBoolean() ? Kjønn.K : Kjønn.M;
    }

    private static int getDigit(String text, int index) {
        return Integer.parseInt(text.substring(index, index + 1));
    }

    private static boolean betweenInclusive(int x, int min, int max) {
        return x >= min && x <= max;
    }


    private String generate() {
        var day = String.format("%02d", this.fødselsdato.getDayOfMonth());
        var month = String.format("%02d", this.fødselsdato.getMonthValue() + NAV_SYNTETISK_IDENT_OFFSET_MND);
        var year = Integer.toString(this.fødselsdato.getYear()).substring(2);
        int fullYear = this.fødselsdato.getYear();

        for (int attempt = 0; attempt < MAX_GENERATE_ATTEMPTS; attempt++) {
            int birthNumber;
            if (this.kjønn == Kjønn.K) {
                birthNumber = 100 + ThreadLocalRandom.current().nextInt(900 / 2) * 2;
            } else if (this.kjønn == Kjønn.M) {
                birthNumber = 100 + ThreadLocalRandom.current().nextInt(900 / 2) * 2 + 1;
            } else {
                birthNumber = 999;
            }

            boolean validRange;
            if (betweenInclusive(fullYear, 1854, 1899)) {
                validRange = betweenInclusive(birthNumber, 500, 749);
            } else if (betweenInclusive(fullYear, 1940, 1999)) {
                // NB: 1940-1999 må sjekkes før 1900-1999 pga overlapp
                validRange = betweenInclusive(birthNumber, 0, 499) || betweenInclusive(birthNumber, 900, 999);
            } else if (betweenInclusive(fullYear, 1900, 1999)) {
                validRange = betweenInclusive(birthNumber, 0, 499);
            } else if (betweenInclusive(fullYear, 2000, 2039)) {
                validRange = betweenInclusive(birthNumber, 500, 999);
            } else {
                LOG.info("Kunne ikke identifisere fødselsnummerserie");
                validRange = true;
            }
            if (!validRange) {
                continue;
            }

            String withoutControlDigits = day + month + year + birthNumber;

            var d1 = getDigit(withoutControlDigits, 0);
            var d2 = getDigit(withoutControlDigits, 1);
            var m1 = getDigit(withoutControlDigits, 2);
            var m2 = getDigit(withoutControlDigits, 3);
            var y1 = getDigit(withoutControlDigits, 4);
            var y2 = getDigit(withoutControlDigits, 5);
            var i1 = getDigit(withoutControlDigits, 6);
            var i2 = getDigit(withoutControlDigits, 7);
            var i3 = getDigit(withoutControlDigits, 8);

            var control1 = 11 - ((3 * d1 + 7 * d2 + 6 * m1 + 1 * m2 + 8 * y1 + 9 * y2 + 4 * i1 + 5 * i2 + 2 * i3) % 11);
            if (control1 == 11) {
                control1 = 0;
            }
            var control2 = 11 - ((5 * d1 + 4 * d2 + 3 * m1 + 2 * m2 + 7 * y1 + 6 * y2 + 5 * i1 + 4 * i2 + 3 * i3 + 2 * control1) % 11);
            if (control2 == 11) {
                control2 = 0;
            }
            if (control1 == 10 || control2 == 10) {
                continue;
            }
            return withoutControlDigits + control1 + control2;
        }
        throw new IllegalStateException(
                "Klarte ikke generere gyldig fødselsnummer etter " + MAX_GENERATE_ATTEMPTS + " forsøk for dato " + fødselsdato);
    }


    public static class Builder {
        private Kjønn kjønn;
        private LocalDate fødselsdato;

        public Builder kjønn(Kjønn k) {
            this.kjønn = k;
            return this;
        }

        public Builder fødselsdato(LocalDate lt) {
            this.fødselsdato = lt;
            return this;
        }

        public String buildAndGenerate() {
            for (int attempt = 0; attempt < MAX_GENERATE_ATTEMPTS; attempt++) {
                var nyttFnr = new FødselsnummerGenerator(this).generate();
                if (!BRUKTE_FØDSELSNUMMER.add(nyttFnr)) {
                    LOG.warn("FØDSELSNUMMER FINNES, GENERER NYTT (forsøk {})", attempt + 1);
                    continue;
                }
                return nyttFnr;
            }
            throw new IllegalStateException(
                    "Klarte ikke generere unikt fødselsnummer etter " + MAX_GENERATE_ATTEMPTS + " forsøk. Antall brukte: "
                            + BRUKTE_FØDSELSNUMMER.size());
        }

    }

    private static LocalDate generateRandomPlausibleBirthdayParent() {
        var startRange = LocalDate.of(1960, 1, 1);
        var endRange = LocalDate.of(2000, 1, 1);

        var startEpoch = startRange.toEpochDay();
        var endEpoch = endRange.toEpochDay();

        var randomEpochDay = startEpoch + ThreadLocalRandom.current().nextLong(endEpoch - startEpoch);

        return LocalDate.ofEpochDay(randomEpochDay);
    }

}
