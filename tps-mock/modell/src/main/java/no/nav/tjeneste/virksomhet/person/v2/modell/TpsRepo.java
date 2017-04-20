package no.nav.tjeneste.virksomhet.person.v2.modell;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static no.nav.tjeneste.virksomhet.person.v2.modell.PersonBygger.Kjønn.KVINNE;
import static no.nav.tjeneste.virksomhet.person.v2.modell.PersonBygger.Kjønn.MANN;

public class TpsRepo {

    private static final Logger LOG = LoggerFactory.getLogger(TpsRepo.class);

    public static LocalDate sistOppdatert = null;

    public static final long STD_MANN_AKTØR_ID = 1000021543419L;
    public static final String STD_MANN_FNR = "06016518156";
    public static final String STD_MANN_FORNAVN = "AL-HAMIDI";
    public static final String STD_MANN_ETTERNAVN = "KHADIM MUJULY H";
    private static TpsRepo instance;
    // Simulering av Tps sin datamodell
    private static Map<Long, String> FNR_VED_AKTØR_ID = new HashMap<>();
    private static Map<String, Long> AKTØR_ID_VED_FNR = new HashMap<>();
    private static Map<String, Person> PERSON_VED_FNR = new HashMap<>();

    // Konstanter for standardbrukere (kan refereres eksternt)
    public static final long STD_KVINNE_AKTØR_ID = 1000021552067L;
    public static final String STD_KVINNE_FNR = "06016921295";
    public static final String STD_KVINNE_FORNAVN = "MARIA L BELANDRES";
    public static final String STD_KVINNE_ETTERNAVN = "HOLMSEN";
    public static final int STD_KVINNE_FØDT_DAG = 06;
    public static final int STD_KVINNE_FØDT_DAG_MND = 01;
    public static final int STD_KVINNE_FØDT_DAG_ÅR = 1969;

    public static final String STD_BARN_FNR = "07111183524";
    public static final long STD_BARN_AKTØR_ID = 666L;
    public static final String STD_BARN_FORNAVN = "EMIL";
    public static final String STD_BARN_ETTERNAVN = "MALVIK";

    // Svarteliste
    public static final String FNR_TRIGGER_SERVICE_UNAVAILABLE = "1111111111l";
    public static final long AKTOER_ID_SERVICE_UNAVAILABLE = 2L;

    public static synchronized TpsRepo init() {
        if(instance == null) {
            instance = new TpsRepo();
        }
        return instance;
    }

    private TpsRepo() {
        opprettTpsData_db();
    }

    private void opprettTpsData_db() {
        EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory( "tps" );
        EntityManager entityManager = sessionFactory.createEntityManager();
        TpsPerson tpsPerson = entityManager.find(TpsPerson.class, new Long(1));
        System.out.println("-------------------------------- " + tpsPerson);
    }

    private void opprettTpsData_csv() {
        // Må oppdateres etter datoskift, ettersom noen datoer i testsettet er relative i tid, ikke statiske.
        List<TpsPerson> tpspersoner = new ArrayList<>();
        tpspersoner.add(new TpsPerson(STD_KVINNE_AKTØR_ID, new PersonBygger(STD_KVINNE_FNR, STD_KVINNE_FORNAVN, STD_KVINNE_ETTERNAVN, KVINNE)
                .medRelasjon("BARN", STD_BARN_FNR, STD_BARN_FORNAVN, STD_BARN_ETTERNAVN)));
        tpspersoner.add(new TpsPerson(STD_BARN_AKTØR_ID, new PersonBygger(STD_BARN_FNR, STD_BARN_FORNAVN, STD_BARN_ETTERNAVN, MANN)
                .medRelasjon("MORA", STD_KVINNE_FNR, STD_KVINNE_FORNAVN, STD_KVINNE_ETTERNAVN)));
        tpspersoner.add(new TpsPerson(STD_MANN_AKTØR_ID, new PersonBygger(STD_MANN_FNR, STD_MANN_FORNAVN, STD_MANN_ETTERNAVN, MANN)
                .medRelasjon("BARN", STD_BARN_FNR, STD_BARN_FORNAVN, STD_BARN_ETTERNAVN)));

        tpspersoner.add(new TpsPerson(1000076788465L, new PersonBygger("41014100138", "BALLARIN", "AYORA MANUEL", MANN)
                .medFødseldato(LocalDate.of(1941, Month.JANUARY, 1))));

        // Legg til personer og relasjoner fra csv
        List<TpsPerson> personerFraFil = lesPersonerFraFil("personer.csv");
        List<TpsRelasjon> relasjonerFraFil = lesRelasjonerFraFil("relasjoner.csv");
        relasjonerFraFil.forEach(relasjon -> {
            Optional<TpsPerson> funnetPerson = personerFraFil.stream()
                    .filter(person -> person.fnr.equals(relasjon.fnr))
                    .findFirst();
            funnetPerson.ifPresent(tpsPerson -> new RelasjonBygger(relasjon).byggFor(tpsPerson.person));
        });
        tpspersoner.addAll(personerFraFil);

        FNR_VED_AKTØR_ID.clear();
        AKTØR_ID_VED_FNR.clear();
        PERSON_VED_FNR.clear();

        for (TpsPerson tpsPerson : tpspersoner) {
            FNR_VED_AKTØR_ID.put(tpsPerson.aktørId, tpsPerson.fnr);
            AKTØR_ID_VED_FNR.put(tpsPerson.fnr, tpsPerson.aktørId);
            PERSON_VED_FNR.put(tpsPerson.fnr, tpsPerson.person);
        }
    }

    private List<TpsPerson> lesPersonerFraFil(String csvFile) {
        try {
            return PersonCsvLeser.lesFil(csvFile);
        } catch (Exception e) {
            LOG.warn("Klarte ikke lese fil " + csvFile);
            return Collections.emptyList();
        }
    }

    private List<TpsRelasjon> lesRelasjonerFraFil(String csvFile) {
        try {
            return RelasjonCsvLeser.lesFil(csvFile);
        } catch (Exception e) {
            LOG.warn("Klarte ikke lese fil " + csvFile);
            return Collections.emptyList();
        }
    }

    public String finnIdent(long aktoerId) {
        oppfriskVedDatoskift();
        sjekkSvartelistedeAktører(aktoerId);
        return FNR_VED_AKTØR_ID.get(aktoerId);
    }

    public Long finnAktoerId(String fnr) {
        oppfriskVedDatoskift();
        sjekkSvartelistedeFødselsnummere(fnr);
        return AKTØR_ID_VED_FNR.get(fnr);
    }

    public Person finnPerson(String fnr) {
        oppfriskVedDatoskift();
        sjekkSvartelistedeFødselsnummere(fnr);
        return PERSON_VED_FNR.get(fnr);
    }

    private void oppfriskVedDatoskift() {
        if(sistOppdatert == null || !sistOppdatert.equals(LocalDate.now())) {
            opprettTpsData_db();
            sistOppdatert = LocalDate.now();
        }
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

}
