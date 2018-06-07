package no.nav.foreldrepenger.fpmock2.felles;

import javax.persistence.EntityManager;

public class DbLeser {

    protected EntityManager entityManager;

    public DbLeser(EntityManager entityManager) {
        this.entityManager=entityManager;
        entityManager.clear();
    }
}
