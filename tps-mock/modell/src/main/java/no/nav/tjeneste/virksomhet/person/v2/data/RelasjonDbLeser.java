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

    public TpsRelasjon finnRelasjon(String fnr){
        if (fnr != null){
            List<TpsRelasjon> tpsRelasjoner = entityManager.createQuery("SELECT t FROM TpsRelasjon t WHERE fnr = :fnr", TpsRelasjon.class).setParameter("fnr", fnr).getResultList();
            if (!tpsRelasjoner.isEmpty()
                    && tpsRelasjoner.get(0) != null){
                return tpsRelasjoner.get(0);
            }
        }
        return null;
    }
}
