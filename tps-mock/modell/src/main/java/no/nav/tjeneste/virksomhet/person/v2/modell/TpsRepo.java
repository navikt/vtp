package no.nav.tjeneste.virksomhet.person.v2.modell;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static no.nav.tjeneste.virksomhet.person.v2.modell.PersonBygger.Kjønn.KVINNE;
import static no.nav.tjeneste.virksomhet.person.v2.modell.PersonBygger.Kjønn.MANN;

public class TpsRepo {

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
        List<TpsPerson> tpspersoner = new ArrayList<>();
        tpspersoner.add(new TpsPerson(STD_KVINNE_AKTØR_ID, new PersonBygger(STD_KVINNE_FNR, STD_KVINNE_FORNAVN, STD_KVINNE_ETTERNAVN, KVINNE)));
        tpspersoner.add(new TpsPerson(1000021543419L, new PersonBygger("06016518156", "AL-HAMIDI", "KHADIM MUJULY H", MANN)));
        tpspersoner.add(new TpsPerson(1000076788465L, new PersonBygger("41014100138", "BALLARIN", "AYORA MANUEL", MANN)
                .medFødseldato(LocalDate.of(1941, Month.JANUARY, 1))));

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

        TpsPerson(long aktørId, PersonBygger personBygger) {
            this.aktørId = aktørId;
            this.fnr = personBygger.getFnr();
            this.person = personBygger.bygg();
        }
    }

}
