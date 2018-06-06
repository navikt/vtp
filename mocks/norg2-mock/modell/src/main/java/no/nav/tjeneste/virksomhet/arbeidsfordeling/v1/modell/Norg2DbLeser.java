package no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.modell;

import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import no.nav.foreldrepenger.mock.felles.DbLeser;

public class Norg2DbLeser extends DbLeser {

    public Norg2DbLeser(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Norg2Entitet> lesAlle() {
        TypedQuery<Norg2Entitet> query = entityManager.createQuery("from Norg2", Norg2Entitet.class);
        query.setHint(QueryHints.HINT_READONLY, "true");
        return query.getResultList();
    }
}
