package no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.modell;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Norg2DbLeserTest {

    @Test
    public void skal_lese_alle() throws Exception {
        EntityManager entityManager = Persistence.createEntityManagerFactory("norg2").createEntityManager();
        Norg2DbLeser dbLeser = new Norg2DbLeser(entityManager);
        List<Norg2Entitet> list = dbLeser.lesAlle();
        assertThat(list.size()).isGreaterThanOrEqualTo(1);
    }
}
