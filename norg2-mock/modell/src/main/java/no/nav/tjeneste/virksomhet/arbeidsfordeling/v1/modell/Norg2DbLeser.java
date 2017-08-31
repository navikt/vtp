package no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.modell;

import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class Norg2DbLeser {

    private EntityManager entityManager;

    public Norg2DbLeser(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Norg2Entitet> lesAlle() {
        TypedQuery<Norg2Entitet> query = entityManager.createQuery("from Norg2", Norg2Entitet.class);
        query.setHint(QueryHints.HINT_READONLY, "true");
        return query.getResultList();
    }
}
