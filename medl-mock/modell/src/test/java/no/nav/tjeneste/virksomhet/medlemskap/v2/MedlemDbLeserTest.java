package no.nav.tjeneste.virksomhet.medlemskap.v2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.Test;

import no.nav.tjeneste.virksomhet.medlemskap.v2.data.MedlemDbLeser;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.Medlemsperiode;

public class MedlemDbLeserTest {

    @Test
    public void skal_lese_medlemsperioder(){
        EntityManager entityManager = Persistence.createEntityManagerFactory("medl").createEntityManager();
        List<Medlemsperiode>  medlemsperiodeList = new MedlemDbLeser(entityManager).finnMedlemsperioder("08088049846");
        assertThat(medlemsperiodeList).isNotEmpty();
    }
}
