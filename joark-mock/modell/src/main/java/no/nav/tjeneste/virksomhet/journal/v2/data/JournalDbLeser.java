package no.nav.tjeneste.virksomhet.journal.v2.data;

import no.nav.tjeneste.virksomhet.journal.v2.modell.JournalDokument;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class JournalDbLeser {
    private EntityManager entityManager;
    //protected byte[] dokument;
    //protected JournalDokument journal;
    public JournalDbLeser(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    /**
     * Hent dokument på dokument id
     * @param dokumentId
     * @return JournalDokument
     */
    public JournalDokument finnDokumentMedDokumentId(String dokumentId) {
        TypedQuery<JournalDokument> query = entityManager.createQuery("FROM JournalDokument t WHERE dokument_id = :dokumentId", JournalDokument.class); //$NON-NLS-1$
        query.setParameter("dokumentId", dokumentId);
        JournalDokument journal = query.getSingleResult();
        return journal;
    }

    /**
     * Hent dokument på journal id
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
}
