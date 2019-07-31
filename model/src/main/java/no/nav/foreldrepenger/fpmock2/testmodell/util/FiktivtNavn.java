package no.nav.foreldrepenger.fpmock2.testmodell.util;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonNavn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FiktivtNavn {

    private static List<String> etternavn = new ArrayList<>();
    private static List<String> fornavnKvinner = new ArrayList<>();
    private static List<String> fornavnMenn = new ArrayList<>();

    private static void ensureThatListsAreLoaded() {
        loadNames("/basedata/etternavn.txt", etternavn, "Duck");
        loadNames("/basedata/fornavn-kvinner.txt", fornavnKvinner, "Dolly");
        loadNames("/basedata/fornavn-menn.txt", fornavnMenn, "Donaldo");
    }

    static public PersonNavn getRandomFemaleName() {
        ensureThatListsAreLoaded();
        String lastName = getRandomLastName();
        return getRandomFemaleName(lastName);
    }


    static public PersonNavn getRandomFemaleName(String lastName) {
        ensureThatListsAreLoaded();
        return getRandomNameFromList(fornavnKvinner, lastName);
    }

    static public PersonNavn getRandomMaleName() {
        ensureThatListsAreLoaded();
        String lastName = getRandomLastName();
        return getRandomMaleName(lastName);
    }

    static public PersonNavn getRandomMaleName(String lastName) {
        ensureThatListsAreLoaded();
        return getRandomNameFromList(fornavnMenn, lastName);
    }

    static public PersonNavn getRandomNameFromList(List<String> list, String lastName) {
        List<String> firstNames = getAlitterationsFromList(list, lastName);
        PersonNavn navn = new PersonNavn();
        if (firstNames.size() > 0) {
            navn.setFornavn(getRandomStringFromList(firstNames));
        } else {
            navn.setFornavn(getRandomStringFromList(fornavnMenn));
        }
        navn.setEtternavn(lastName);
        return navn;
    }

    static private String getRandomLastName() {
        return getRandomStringFromList(etternavn);
    }

    static private String getRandomStringFromList(List<String> nameList) {
        int rnd = new Random().nextInt(nameList.size());
        return nameList.get(rnd);
    }

    static public List<String> getAlitterationsFromList(List<String> list, String word) {
        List<String> validFirstLetters = new ArrayList<>();
        List<String> validFirstTwoLetters = new ArrayList<>();
        String firstLetter = word.substring(0, 1).toUpperCase();
        String firstTwoLetters = word.substring(0, 2).toUpperCase();

        validFirstLetters.add(firstLetter);
        validFirstTwoLetters.add(firstTwoLetters);
        if (firstTwoLetters.equals("CH") || firstTwoLetters.equals("CA")) {
            validFirstLetters.add("K");
        }

        if (firstTwoLetters.equals("AA")) {
            validFirstLetters.clear();
            validFirstLetters.add("Å");
        }

        if (firstLetter.equals("Å")) {
            validFirstTwoLetters.add("AA");
        }

        if (firstLetter.equals("J")) {
            validFirstTwoLetters.add("GJ");
        }

        if (firstTwoLetters.equals("GJ")) {
            validFirstLetters.clear();
            validFirstLetters.add("J");
        }

        if (firstLetter.equals("Z")) {
            validFirstLetters.add("S");
        }

        if (firstLetter.equals("K")) {
            validFirstTwoLetters.add("CA");
            validFirstTwoLetters.add("CH");
        }

        if (firstLetter.equals("W")) {
            validFirstLetters.add("V");
        }
        if (firstLetter.equals("V")) {
            validFirstLetters.add("W");
        }
        return list
                .stream()
                .filter(s -> validFirstLetters.contains(s.substring(0, 1).toUpperCase()) ||
                        validFirstTwoLetters.contains(s.substring(0, 2).toUpperCase()))
                .collect(Collectors.toList());
    }

    static private void loadNames(String resourceName, List<String> targetList, String fallbackName) {
        if (targetList.size() == 0) {
            try (InputStream inputStream = FiktivtNavn.class.getResourceAsStream(resourceName)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    String capitalizedName = strLine.substring(0, 1).toUpperCase() + strLine.substring(1);
                    targetList.add(capitalizedName);
                }
            } catch (Exception exp) {
                targetList.add(fallbackName);
            }
        }
    }
}
