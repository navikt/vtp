package no.nav.tjeneste.virksomhet.person.v2.modell;


import no.nav.tjeneste.virksomhet.person.v2.data.PersonDbLeser;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonDbLeserTest {

    @Test
    public void skal_lese_personer() throws Exception {
        EntityManager entityManager = Persistence.createEntityManagerFactory("tps").createEntityManager();
        List<TpsPerson> tpsPersoner = new PersonDbLeser(entityManager).opprettTpsData();
        assertThat(tpsPersoner).hasSize(52);
    }

}