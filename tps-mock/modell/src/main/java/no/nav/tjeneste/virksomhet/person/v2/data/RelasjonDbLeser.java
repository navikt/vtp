package no.nav.tjeneste.virksomhet.person.v2.data;

import no.nav.tjeneste.virksomhet.person.v2.modell.TpsRelasjon;

import javax.persistence.EntityManager;
import java.util.List;

public class RelasjonDbLeser {

    private EntityManager entityManager;

    public RelasjonDbLeser(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<TpsRelasjon> opprettTpsData() {
        List<TpsRelasjon> tpsRelasjoner = entityManager.createNamedQuery("TpsRelasjon.findAll", TpsRelasjon.class).getResultList();
        return tpsRelasjoner;
    }
}
