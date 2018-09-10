package no.nav.tjeneste.virksomhet.journal.modell;

import java.util.List;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;


public class JournalScenarioTjenesteImpl {

    private TestscenarioBuilderRepository scenarioRepository;

    public JournalScenarioTjenesteImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    /**
     * Hent dokument på dokument id
     * @param dokumentId
     * @return JournalDokument
     */
    public List<JournalDokument> finnDokumentMedDokumentId(String dokumentId) {
        throw new UnsupportedOperationException("TODO: implementer");
    }

    /**
     * Hent ett dokument på journal id
     * @param journalId
     * @return JournalDokument
     */
    public JournalDokument finnDokumentMedJournalId(String journalId) {
        throw new UnsupportedOperationException("TODO: implementer");
    }

    /**
     * Hent dokumenter på journal id
     * @param journalId
     * @return List<JournalDokument></JournalDokument>
     */
    public List<JournalDokument> finnDokumenterMedJournalId(String journalId) {
        throw new UnsupportedOperationException("TODO: implementer");
    }

    /**
     * Finner journalposter med sak_id
     * @param sakId
     * @return  List<JournalDokument>
     */

    public List<JournalDokument> finnJournalposterMedSakId(String sakId){
        throw new UnsupportedOperationException("TODO: implementer");
    }

    public void oppdaterJournalpost(JournalDokument journalDok) {
        throw new UnsupportedOperationException("TODO: implementer");
    }
}
