package no.nav.foreldrepenger.vtp.testmodell.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AnnenPartModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonNavn;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SivilstandModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;

public class TestdataUtil {

    private TestdataUtil() {
    }

    private static Random RANDOM = new Random();

    public static LocalDate generateRandomPlausibleBirtdayParent() {
        var startRange = LocalDate.of(1960, 1, 1);
        var endRange = LocalDate.of(2000, 1, 1);

        var startEpoch = startRange.toEpochDay();
        var endEpoch = endRange.toEpochDay();

        var randomEpochDay = startEpoch + (long) (RANDOM.nextDouble() * endEpoch - startEpoch);

        return LocalDate.ofEpochDay(randomEpochDay);
    }

    public static LocalDate generateBirthdateNowMinusThreeYears() {
        var endRange = LocalDateTime.now().toLocalDate();
        var startRange = endRange.minus(3, ChronoUnit.YEARS);

        var startEpoch = startRange.toEpochDay();
        var endEpoch = endRange.toEpochDay();

        var randomEpochDay = startEpoch + (long) (RANDOM.nextDouble() * (endEpoch - startEpoch));

        return LocalDate.ofEpochDay(randomEpochDay);
    }

    public static LocalDate generateBirthdayYoungerThanSixMonths() {
        //Note: this actually returns younger than five months, but ok since it's also younger than six months...
        var endRange = LocalDateTime.now().toLocalDate();
        var startRange = endRange.minus(5, ChronoUnit.MONTHS);

        var startEpoch = startRange.toEpochDay();
        var endEpoch = endRange.toEpochDay();

        var randomEpochDay = startEpoch + (long) (RANDOM.nextDouble() * (endEpoch - startEpoch));

        return LocalDate.ofEpochDay(randomEpochDay);
    }

    public static PersonNavn getSokerName(SøkerModell søker) {
        PersonNavn personNavn;
        if (søker.getKjønn().equals(BrukerModell.Kjønn.K)) {
            personNavn = FiktivtNavn.getRandomFemaleName();
        } else {
            personNavn = FiktivtNavn.getRandomMaleName();
        }
        return personNavn;
    }

    /**
     *  Tar høyde for at gifte folk deler etternavn.
     */
    public static PersonNavn getAnnenPartName(SøkerModell søker, AnnenPartModell annenPart) {
        PersonNavn personNavn;
        if (søker.getSivilstand().getSivilstandType().equals(SivilstandModell.Sivilstander.GIFT)) {
            if (annenPart.getKjønn().equals(BrukerModell.Kjønn.K)) {
                personNavn = FiktivtNavn.getRandomFemaleName(søker.getEtternavn());
            } else {
                personNavn = FiktivtNavn.getRandomMaleName(søker.getEtternavn());
            }
        } else {
            if (annenPart.getKjønn().equals(BrukerModell.Kjønn.K)) {
                personNavn = FiktivtNavn.getRandomFemaleName();
            } else {
                personNavn = FiktivtNavn.getRandomMaleName();
            }
        }
        return personNavn;
    }
}
