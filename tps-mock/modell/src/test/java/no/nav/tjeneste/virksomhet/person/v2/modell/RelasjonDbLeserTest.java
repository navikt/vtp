package no.nav.tjeneste.virksomhet.person.v2.modell;


import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RelasjonDbLeserTest {

    @Test
    public void skal_lese_personer() throws Exception {
        EntityManager entityManager = Persistence.createEntityManagerFactory("tps").createEntityManager();
        List<TpsRelasjon> tpsRelasjoner = new RelasjonDbLeser(entityManager).opprettTpsData();
        assertThat(tpsRelasjoner).hasSize(12);
    }

}