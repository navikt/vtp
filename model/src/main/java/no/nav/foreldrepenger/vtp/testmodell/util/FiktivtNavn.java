package no.nav.foreldrepenger.vtp.testmodell.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell.Kjønn;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonNavn;

public class FiktivtNavn {
    
    private enum Navnelager {
        SINGLETON;
        
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
        
        private static String getRandom(List<String> liste) {
            return liste.get(new Random().nextInt(liste.size()));
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

    static public PersonNavn getRandomFemaleName() {
        return getRandomFemaleName(Navnelager.SINGLETON.getRandomEtternavn());
    }

    static public PersonNavn getRandomFemaleName(String lastName) {
        return new PersonNavn(Navnelager.SINGLETON.getRandomFornavnKvinne(), lastName, Kjønn.K);
    }

    static public PersonNavn getRandomMaleName() {
        return getRandomMaleName(Navnelager.SINGLETON.getRandomEtternavn());
    }

    static public PersonNavn getRandomMaleName(String lastName) {
        return new PersonNavn(Navnelager.SINGLETON.getRandomFornavnMann(), lastName, Kjønn.M);
    }

}
