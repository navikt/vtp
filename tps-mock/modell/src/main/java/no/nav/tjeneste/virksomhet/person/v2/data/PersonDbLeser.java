package no.nav.tjeneste.virksomhet.person.v2.data;

import no.nav.tjeneste.virksomhet.person.v2.modell.PersonBygger;
import no.nav.tjeneste.virksomhet.person.v2.modell.TpsPerson;

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
}