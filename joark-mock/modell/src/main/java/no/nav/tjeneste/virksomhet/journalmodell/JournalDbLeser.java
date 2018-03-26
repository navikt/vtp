package no.nav.tjeneste.virksomhet.journalmodell;

import no.nav.foreldrepenger.mock.felles.DbLeser;
import no.nav.foreldrepenger.mock.felles.DbUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class JournalDbLeser extends DbLeser {

    public JournalDbLeser(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * Hent dokument på dokument id
     * @param dokumentId
     * @return JournalDokument
     */
    public List<JournalDokument> finnDokumentMedDokumentId(String dokumentId) {
        TypedQuery<JournalDokument> query = entityManager.createQuery("FROM JournalDokument t WHERE dokument_id = :dokumentId", JournalDokument.class); //$NON-NLS-1$
        query.setParameter("dokumentId", dokumentId);
        List<JournalDokument> journal = query.getResultList();
        return journal;
    }

    /**
     * Hent ett dokument på journal id
     * @param journalId
     * @return JournalDokument
     */
    public JournalDokument finnDokumentMedJournalId(String journalId) {
        TypedQuery<JournalDokument> query = entityManager.createQuery("FROM JournalDokument t WHERE journalpost_id = :journalId", JournalDokument.class); //$NON-NLS-1$
        query.setParameter("journalId", journalId);
        JournalDokument journal = query.getSingleResult();
        return journal;
    }

    /**
     * Hent dokumenter på journal id
     * @param journalId
     * @return List<JournalDokument></JournalDokument>
     */
    public List<JournalDokument> finnDokumenterMedJournalId(String journalId) {
        TypedQuery<JournalDokument> query = entityManager.createQuery("FROM JournalDokument t WHERE journalpost_id = :journalId", JournalDokument.class); //$NON-NLS-1$
        query.setParameter("journalId", journalId);
        List<JournalDokument> journalDokListe = query.getResultList();
        return journalDokListe;
    }

    /**
     * Henter dokument/journal på primary key
     * @param id
     * @return JournalDokument
     */
    public JournalDokument finnDokument(String id) {
        JournalDokument journal = entityManager.find(JournalDokument.class, Long.valueOf(id));
        return journal;
    }

    /**
     * Finner journalposter med sak_id
     * @param sakId
     * @return  List<JournalDokument>
     */

    public List<JournalDokument> finnJournalposterMedSakId(String sakId){
        TypedQuery<JournalDokument> query = entityManager.createQuery("FROM JournalDokument t WHERE sak_id = :sakId", JournalDokument.class); //$NON-NLS-1$
        query.setParameter("sakId", sakId);
        List<JournalDokument> journalDokumentList =  query.getResultList();
        return journalDokumentList;
    }

    public void oppdaterJournalpost(JournalDokument journalDok) {
        DbUtils.doInLocalTransaction(entityManager, () -> {
            if (entityManager.contains(journalDok)) {
                // Eksisterende og persistent - ikke gjør noe
                @SuppressWarnings("unused")
                int brkpt = 1; // NOSONAR
            } else if (journalDok.getId() != null) {
                // Eksisterende men detached - oppdater
                entityManager.merge(journalDok);
            } else {
                // Ny - insert
                entityManager.persist(journalDok);
            }
        });
    }
}
