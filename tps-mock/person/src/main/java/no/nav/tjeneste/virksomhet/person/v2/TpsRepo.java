package no.nav.tjeneste.virksomhet.person.v2;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.time.LocalDate.of;
import static java.time.Month.JANUARY;
import static java.time.Month.OCTOBER;
import static no.nav.tjeneste.virksomhet.person.v2.TpsRepo.Kjønn.KVINNE;
import static no.nav.tjeneste.virksomhet.person.v2.TpsRepo.Kjønn.MANN;

class TpsRepo {

    private static TpsRepo instance;
    // Simulering av Tps sin datamodell
    private static Map<Long, String> FNR_VED_AKTØR_ID = new HashMap<>();
    private static Map<String, Long> AKTØR_ID_VED_FNR = new HashMap<>();
    private static Map<String, Person> PERSON_VED_FNR = new HashMap<>();

    // Svarteliste
    private static final String FNR_TRIGGER_SERVICE_UNAVAILABLE = "1111111111l";

    static synchronized TpsRepo init() {
        if(instance == null) {
            instance = new TpsRepo();
        }
        return instance;
    }

    private TpsRepo() {
        List<TpsPerson> tpspersoner = new ArrayList<>();
        tpspersoner.add(new TpsPersonBygger(1L, "13107221234", of(1972, OCTOBER, 13), "ANNE-BERIT", "HJARTDAL", KVINNE).bygg());
        tpspersoner.add(new TpsPersonBygger(2L, "01017912345", of(1979, JANUARY, 1), "LIV HENRIETTE", "LARSEN ULLMANN", KVINNE).bygg());
        tpspersoner.add(new TpsPersonBygger(3L, "01018012345", of(1980, JANUARY, 1), "ANDERS", "ANDERSEN", MANN).bygg());

        for (TpsPerson tpsPerson : tpspersoner) {
            FNR_VED_AKTØR_ID.put(tpsPerson.aktørId, tpsPerson.fnr);
            AKTØR_ID_VED_FNR.put(tpsPerson.fnr, tpsPerson.aktørId);
            PERSON_VED_FNR.put(tpsPerson.fnr, tpsPerson.person);
        }
    }

    Person finnPerson(String fnr) {
        sjekkSvarteliste(fnr);
        return PERSON_VED_FNR.get(fnr);
    }

    private void sjekkSvarteliste(String fnr) {
        if (FNR_TRIGGER_SERVICE_UNAVAILABLE.equals(fnr)) {
            throw new IllegalStateException("Simulerer exception for fnr: " + fnr);
        }
    }

    // Hjelpeklasser
    class TpsPerson {
        private final long aktørId;
        private final String fnr;

        private final Person person;
        TpsPerson(long aktørId, String fnr, Person person) {
            this.aktørId = aktørId;
            this.fnr = fnr;
            this.person = person;
        }
    }

    enum Kjønn {
        MANN("M"),
        KVINNE("K");

        private String verdi;

        Kjønn(String verdi) {
            this.verdi = verdi;
        }
    }

    class TpsPersonBygger {

        private String fnr;
        private String fornavn;
        private String etternavn;
        private LocalDate fødselsdato;
        private long aktørId;
        private final Kjønn kjønn;

        TpsPersonBygger(long aktørId, String fnr, LocalDate fødselsdato, String fornavn, String etternavn, Kjønn kjønn) {
            Objects.requireNonNull(fnr, "Fødselsnummer er obligatorisk");
            Objects.requireNonNull(fødselsdato, "Fødselsdato er obligatorisk");
            Objects.requireNonNull(kjønn, "Kjønn er obligatorisk");
            Objects.requireNonNull(fornavn, "Fornavn er obligatorisk");
            Objects.requireNonNull(etternavn, "Etternavn er obligatorisk");
            this.aktørId = aktørId;
            this.fnr = fnr;
            this.fødselsdato = fødselsdato;
            this.kjønn = kjønn;
            this.fornavn = fornavn;
            this.etternavn = etternavn;
        }

        TpsPerson bygg() {
            Person person = new Person();

            // Ident
            NorskIdent norskIdent = new NorskIdent();
            norskIdent.setIdent(fnr);
            Personidenter personidenter = new Personidenter();
            personidenter.setValue("fnr");
            norskIdent.setType(personidenter);
            person.setIdent(norskIdent);

            // Kjønn
            Kjoenn kjonn = new Kjoenn();
            Kjoennstyper kjonnstype = new Kjoennstyper();
            kjonnstype.setValue(kjønn.verdi);
            kjonn.setKjoenn(kjonnstype);
            person.setKjoenn(kjonn);

            // Fødselsdato
            Foedselsdato fodselsdato = new Foedselsdato();
            XMLGregorianCalendar xcal;
            try {
                GregorianCalendar gcal = GregorianCalendar.from(fødselsdato.atStartOfDay(ZoneId.systemDefault()));
                xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            } catch (DatatypeConfigurationException e) {
                throw new IllegalStateException("Kunne ikke konvertere dato", e);
            }
            fodselsdato.setFoedselsdato(xcal);
            person.setFoedselsdato(fodselsdato);

            // Navn
            Personnavn personnavn   = new Personnavn();
            personnavn.setEtternavn(etternavn.toUpperCase());
            personnavn.setFornavn(fornavn.toUpperCase());
            personnavn.setSammensattNavn(etternavn.toUpperCase() + " " + fornavn.toUpperCase());
            person.setPersonnavn(personnavn);

            // I fremtiden: Legge til flere felter, som barn osv

            return new TpsPerson(aktørId, fnr, person);
        }


    }

}
