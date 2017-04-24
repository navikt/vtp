package no.nav.tjeneste.virksomhet.person.v2.modell;

import javax.persistence.EntityManager;
import java.util.List;

class RelasjonDbLeser {

    private EntityManager entityManager;

    public RelasjonDbLeser(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    List<TpsRelasjon> opprettTpsData() {
        List<TpsRelasjon> tpsRelasjoner = entityManager.createNamedQuery("TpsRelasjon.findAll", TpsRelasjon.class).getResultList();
        return tpsRelasjoner;
    }
}
