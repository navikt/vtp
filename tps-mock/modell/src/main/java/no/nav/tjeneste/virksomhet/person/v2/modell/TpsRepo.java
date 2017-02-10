package no.nav.tjeneste.virksomhet.person.v2.modell;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.time.LocalDate.of;
import static no.nav.tjeneste.virksomhet.person.v2.modell.TpsRepo.Kjønn.KVINNE;
import static no.nav.tjeneste.virksomhet.person.v2.modell.TpsRepo.Kjønn.MANN;

public class TpsRepo {

    private static TpsRepo instance;
    // Simulering av Tps sin datamodell
    private static Map<Long, String> FNR_VED_AKTØR_ID = new HashMap<>();
    private static Map<String, Long> AKTØR_ID_VED_FNR = new HashMap<>();
    private static Map<String, Person> PERSON_VED_FNR = new HashMap<>();

    // Svarteliste
    private static final String FNR_TRIGGER_SERVICE_UNAVAILABLE = "1111111111l";
    private static final long AKTOER_ID_SERVICE_UNAVAILABLE = 2L;

    public static synchronized TpsRepo init() {
        if(instance == null) {
            instance = new TpsRepo();
        }
        return instance;
    }

    private TpsRepo() {
        List<TpsPerson> tpspersoner = new ArrayList<>();
        tpspersoner.add(new TpsPersonBygger(1L, "13107221234", "ANNE-BERIT", "HJARTDAL", KVINNE).bygg());
        tpspersoner.add(new TpsPersonBygger(3L, "01017912345", "LIV HENRIETTE", "LARSEN ULLMANN", KVINNE).bygg());
        tpspersoner.add(new TpsPersonBygger(4L, "01018012345", "ANDERS", "ANDERSEN", MANN).bygg());

        for (TpsPerson tpsPerson : tpspersoner) {
            FNR_VED_AKTØR_ID.put(tpsPerson.aktørId, tpsPerson.fnr);
            AKTØR_ID_VED_FNR.put(tpsPerson.fnr, tpsPerson.aktørId);
            PERSON_VED_FNR.put(tpsPerson.fnr, tpsPerson.person);
        }
    }

    public String finnIdent(long aktoerId) {
        sjekkSvartelistedeAktører(aktoerId);
        return FNR_VED_AKTØR_ID.get(aktoerId);
    }

    public Long finnAktoerId(String fnr) {
        sjekkSvartelistedeFødselsnummere(fnr);
        return AKTØR_ID_VED_FNR.get(fnr);
    }

    public Person finnPerson(String fnr) {
        sjekkSvartelistedeFødselsnummere(fnr);
        return PERSON_VED_FNR.get(fnr);
    }

    private void sjekkSvartelistedeAktører(long aktoerId) {
        if (AKTOER_ID_SERVICE_UNAVAILABLE == aktoerId) {
            throw new IllegalStateException("Simulerer exception for aktoerId: " + aktoerId);
        }
    }

    private void sjekkSvartelistedeFødselsnummere(String fnr) {
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
        private long aktørId;
        private final Kjønn kjønn;

        TpsPersonBygger(long aktørId, String fnr, String fornavn, String etternavn, Kjønn kjønn) {
            Objects.requireNonNull(fnr, "Fødselsnummer er obligatorisk");
            Objects.requireNonNull(kjønn, "Kjønn er obligatorisk");
            Objects.requireNonNull(fornavn, "Fornavn er obligatorisk");
            Objects.requireNonNull(etternavn, "Etternavn er obligatorisk");
            this.aktørId = aktørId;
            this.fnr = fnr;
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
                // Simpel algoritme for å konvertere fnr. Må utbedres ved behov.
                DateFormat format = new SimpleDateFormat("ddmmyy");
                Date dato = format.parse(fnr.substring(0, 6));
                GregorianCalendar gcal = new GregorianCalendar();
                gcal.setTime(dato);
                xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            } catch (DatatypeConfigurationException | ParseException e) {
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
