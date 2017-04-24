package no.nav.tjeneste.virksomhet.person.v2.data;

import no.nav.tjeneste.virksomhet.person.v2.modell.PersonBygger;
import no.nav.tjeneste.virksomhet.person.v2.modell.PersonRad;
import no.nav.tjeneste.virksomhet.person.v2.modell.TpsPerson;

import static java.lang.Long.parseLong;
import static no.nav.tjeneste.virksomhet.person.v2.modell.CSVUtils.parseLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersonCsvLeser {

    private static final char SEPARATOR = ';';
    private static final char QUOTE = '"';


    public static List<TpsPerson> lesFil(String csvFile) throws Exception {
        ClassLoader classLoader = PersonCsvLeser.class.getClassLoader();
        File file = new File(classLoader.getResource(csvFile).getFile());
        Scanner scanner = new Scanner(file);
        scanner.nextLine(); //ignorer 1. linje (header)

        List<TpsPerson> personRader = new ArrayList<>();
        while (scanner.hasNext()) {
            List<String> line = parseLine(scanner.nextLine(), SEPARATOR, QUOTE);
            PersonRad rad = new PersonRad();
            rad.AktoerId = line.get(0);
            rad.FNR = line.get(1);
            rad.RegDato = line.get(2);
            rad.Fornavn = line.get(3);
            rad.Mellomnavn =  line.get(4);
            rad.Etternavn = line.get(5);
            rad.PAdresse1 = line.get(6);
            rad.PAdresse2 = line.get(7);
            rad.PAdresse3 = line.get(8);
            rad.Postadr_land = line.get(9);
            rad.Dato_postadresse =  line.get(10);
            rad.Statsborgerskap = line.get(11);
            rad.BAdresse_gate = line.get(12);
            rad.Bhusnr = line.get(13);
            rad.Bgatekode = line.get(14);
            rad.Bpostnr = line.get(15);
            rad.Bflyttedato = line.get(16);
            rad.Bkomnr = line.get(17);
            rad.Spesreg = line.get(18);
            rad.Dato_spesreg = line.get(19);
            rad.Kjoenn = line.get(20);
            personRader.add(tilTpsPerson(rad));
        }
        scanner.close();

        return personRader;
    }

    private static TpsPerson tilTpsPerson(PersonRad rad) {
        return new TpsPerson(parseLong(rad.AktoerId),
                new PersonBygger(rad.FNR, rad.Fornavn, rad.Etternavn, PersonBygger.Kjønn.valueOf(rad.Kjoenn)));
    }

}