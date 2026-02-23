package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import no.nav.vtp.person.personopplysninger.Kjønn;

public class FiktivtNavn {

    private enum Navnelager {
        SINGLETON;

        private static final Random RANDOM = new Random();
        private List<String> etternavn = loadNames("/basedata/etternavn.txt");
        private List<String> fornavnKvinner = loadNames("/basedata/fornavn-kvinner.txt");
        private List<String> fornavnMenn = loadNames("/basedata/fornavn-menn.txt");

        String getRandomFornavnMann() {
            return getRandom(fornavnMenn);
        }

        String getRandomFornavnKvinne() {
            return getRandom(fornavnKvinner);
        }

        String getRandomEtternavn() {
            return getRandom(etternavn);
        }

        private static synchronized String getRandom(List<String> liste) {
            return liste.get(RANDOM.nextInt(liste.size()));
        }

        private static List<String> loadNames(String resourceName) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(FiktivtNavn.class.getResourceAsStream(resourceName)))) {
                final List<String> resultat = new ArrayList<>();
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    String capitalizedName = strLine.substring(0, 1).toUpperCase() + strLine.substring(1);
                    resultat.add(capitalizedName);
                }
                return resultat;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static PersonNavn getRandomFemaleName() {
        return getRandomFemaleName(Navnelager.SINGLETON.getRandomEtternavn());
    }

    public static PersonNavn getRandomFemaleName(String lastName) {
        return new PersonNavn(Navnelager.SINGLETON.getRandomFornavnKvinne(), lastName, Kjønn.K);
    }

    public static PersonNavn getRandomMaleName() {
        return getRandomMaleName(Navnelager.SINGLETON.getRandomEtternavn());
    }

    public static PersonNavn getRandomMaleName(String lastName) {
        return new PersonNavn(Navnelager.SINGLETON.getRandomFornavnMann(), lastName, Kjønn.M);
    }

}
