package no.nav.foreldrepenger.fpmock2.felles;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class DbUtils {

    public static void doInLocalTransaction(EntityManager entityManager, Doer doer) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();

            doer.doIt();

            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @FunctionalInterface
    public interface Doer {
        void doIt();
    }
}
