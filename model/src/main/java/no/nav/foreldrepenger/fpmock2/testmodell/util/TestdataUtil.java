package no.nav.foreldrepenger.fpmock2.testmodell.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class TestdataUtil {
    static public LocalDate generateRandomPlausibleBirtdayParent(){
        LocalDate startRange = LocalDate.of(1960,1,1);
        LocalDate endRange = LocalDate.of(2000,1,1);

        long startEpoch = startRange.toEpochDay();
        long endEpoch = endRange.toEpochDay();

        Random random = new Random();

        long randomEpochDay = startEpoch + (long)(random.nextDouble()*endEpoch - startEpoch);

        LocalDate randomLocalDate = LocalDate.ofEpochDay(randomEpochDay);

        return randomLocalDate;

    }

    static public LocalDate generateBirthdateNowMinusThreeYears(){

        LocalDate endRange = LocalDateTime.now().toLocalDate();
        LocalDate startRange = endRange.minus(3, ChronoUnit.YEARS);

        long startEpoch = startRange.toEpochDay();
        long endEpoch = endRange.toEpochDay();

        Random random = new Random();

        long randomEpochDay = startEpoch + (long)(random.nextDouble() * (endEpoch - startEpoch));

        LocalDate randomLocalDate = LocalDate.ofEpochDay(randomEpochDay);

        return randomLocalDate;
    }

    static public LocalDate generateBirthdayYoungerThanSixMonths(){
        //Note: this actually returns younger than five months, but ok since it's also younger than six months...
        LocalDate endRange = LocalDateTime.now().toLocalDate();
        LocalDate startRange = endRange.minus(5, ChronoUnit.MONTHS);

        long startEpoch = startRange.toEpochDay();
        long endEpoch = endRange.toEpochDay();

        Random random = new Random();

        long randomEpochDay = startEpoch + (long)(random.nextDouble() * (endEpoch - startEpoch));

        LocalDate randomLocalDate = LocalDate.ofEpochDay(randomEpochDay);

        return randomLocalDate;
    }
}
