package no.nav.tjeneste.virksomhet.person.v3.data;

import no.nav.tjeneste.virksomhet.person.v3.modell.PersonBygger;
import no.nav.tjeneste.virksomhet.person.v3.modell.TpsPerson;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;

import javax.persistence.EntityManager;
import java.util.List;

public class PersonDbLeser {

    private EntityManager entityManager;

    public PersonDbLeser(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<TpsPerson> opprettTpsData() {
        List<TpsPerson> tpsPersoner = entityManager.createNamedQuery("TpsPerson.findAll", TpsPerson.class).getResultList();
        tpsPersoner.forEach(tpsPerson -> tpsPerson.person = new PersonBygger(tpsPerson).bygg());

        return tpsPersoner;
    }

    public List<TpsPerson> lesTpsData() {
        List<TpsPerson> tpsPersoner = entityManager.createNamedQuery("TpsPerson.findAll", TpsPerson.class).getResultList();

        return tpsPersoner;
    }

    public Bruker finnPerson(String fnr) {
        if (fnr != null) {
            List<TpsPerson> tpsPersoner = entityManager.createQuery("SELECT t FROM TpsPerson t WHERE fnr = :fnr", TpsPerson.class).setParameter("fnr", fnr).getResultList();
            if (!tpsPersoner.isEmpty() && tpsPersoner.get(0) != null)
            {
                tpsPersoner.get(0).person = new PersonBygger(tpsPersoner.get(0)).bygg();
                return tpsPersoner.get(0).person;
            }
        }
        return null;
    }

    public String finnIdent(String aktoerId) {
        List<TpsPerson> tpsPersoner = entityManager.createQuery("SELECT t FROM TpsPerson t WHERE aktorid = :aktorid", TpsPerson.class).setParameter("aktorid", aktoerId).getResultList();
        if (!tpsPersoner.isEmpty()
                && tpsPersoner.get(0) != null) {
            return tpsPersoner.get(0).getFnr();
        }

        return null;
    }

    public Long finnAktoerId(String fnr) {
        if (fnr != null){
            List<TpsPerson> tpsPersoner = entityManager.createQuery("SELECT t FROM TpsPerson t WHERE fnr = :fnr", TpsPerson.class).setParameter("fnr", fnr).getResultList();
            if (!tpsPersoner.isEmpty()
                    && tpsPersoner.get(0) != null){
                return tpsPersoner.get(0).getAktørId();
            }
        }
        return null;
    }
}