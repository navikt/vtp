package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import no.nav.vtp.person.personopplysninger.Kjønn;

public class FiktivtNavn {

    private static final List<String> ETTERNAVN = loadNames("/basedata/etternavn.txt");
    private static final List<String> FORNAVN_KVINNER = loadNames("/basedata/fornavn-kvinner.txt");
    private static final List<String> FORNAVN_MENN = loadNames("/basedata/fornavn-menn.txt");

    private FiktivtNavn() {}

    public static PersonNavn getRandomFemaleName() {
        return getRandomFemaleName(getRandom(ETTERNAVN));
    }

    public static PersonNavn getRandomFemaleName(String lastName) {
        return new PersonNavn(getRandom(FORNAVN_KVINNER), lastName, Kjønn.K);
    }

    public static PersonNavn getRandomMaleName() {
        return getRandomMaleName(getRandom(ETTERNAVN));
    }

    public static PersonNavn getRandomMaleName(String lastName) {
        return new PersonNavn(getRandom(FORNAVN_MENN), lastName, Kjønn.M);
    }

    private static String getRandom(List<String> liste) {
        return liste.get(ThreadLocalRandom.current().nextInt(liste.size()));
    }

    private static List<String> loadNames(String resourceName) {
        try (var br = new BufferedReader(new InputStreamReader(FiktivtNavn.class.getResourceAsStream(resourceName)))) {
            return br.lines()
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
