package no.nav.tjeneste.virksomhet.person.v3.data;

import no.nav.foreldrepenger.mock.felles.DbLeser;
import no.nav.tjeneste.virksomhet.person.v3.modell.TpsRelasjon;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

public class RelasjonDbLeser extends DbLeser {

    public RelasjonDbLeser(EntityManager entityManager) {
        super(entityManager);
    }

    public List<TpsRelasjon> opprettTpsData() {
        return entityManager.createNamedQuery("TpsRelasjon.findAll", TpsRelasjon.class).getResultList();
    }

    public List<TpsRelasjon> finnRelasjon(String fnr) {
        if (fnr != null) {
            List<TpsRelasjon> tpsRelasjoner = entityManager.createQuery("SELECT t FROM TpsRelasjon t WHERE fnr = :fnr", TpsRelasjon.class).setParameter("fnr", fnr).getResultList();
            if (!tpsRelasjoner.isEmpty() && tpsRelasjoner.get(0) != null) {
                return tpsRelasjoner;
            }
        }
        return Collections.emptyList();
    }
}
