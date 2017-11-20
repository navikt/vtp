package no.nav.foreldrepenger.mock.felles;

import javax.persistence.EntityManager;

public class DbLeser {

    protected EntityManager entityManager;

    public DbLeser(EntityManager entityManager) {
        this.entityManager=entityManager;
        entityManager.clear();
    }
}
