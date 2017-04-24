package no.nav.tjeneste.virksomhet.person.v2.data;

import no.nav.tjeneste.virksomhet.person.v2.modell.RelasjonRad;
import no.nav.tjeneste.virksomhet.person.v2.modell.TpsRelasjon;

import static no.nav.tjeneste.virksomhet.person.v2.modell.CSVUtils.parseLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RelasjonCsvLeser {

    private static final char SEPARATOR = ';';
    private static final char QUOTE = '"';


    public static List<TpsRelasjon> lesFil(String csvFile) throws Exception {
        ClassLoader classLoader = RelasjonCsvLeser.class.getClassLoader();
        File file = new File(classLoader.getResource(csvFile).getFile());
        Scanner scanner = new Scanner(file);
        scanner.nextLine(); //ignorer 1. linje (header)

        List<TpsRelasjon> personRader = new ArrayList<>();
        while (scanner.hasNext()) {
            List<String> line = parseLine(scanner.nextLine(), SEPARATOR, QUOTE);
            RelasjonRad rad = new RelasjonRad();

            rad.FNR = line.get(0);
            rad.FNR_relasjon = line.get(1);
            rad.Relasjonstype = line.get(2);
            rad.Fornavn = line.get(3);
            rad.Etternavn = line.get(4);

            personRader.add(tilTpsRelasjon(rad));
        }
        scanner.close();

        return personRader;
    }

    private static TpsRelasjon tilTpsRelasjon(RelasjonRad rad) {
        TpsRelasjon relasjon = new TpsRelasjon();
        relasjon.fnr = rad.FNR;
        relasjon.relasjonFnr = rad.FNR_relasjon;
        relasjon.relasjonsType = rad.Relasjonstype;
        relasjon.fornavn = rad.Fornavn;
        relasjon.etternavn = rad.Etternavn;
        return relasjon;
    }


}