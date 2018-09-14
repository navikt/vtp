package no.nav.foreldrepenger.fpmock2.testmodell.journal;

import java.util.List;

import no.nav.foreldrepenger.fpmock2.testmodell.journal.dokument.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;


public class JournalScenarioTjenesteImpl {

    private TestscenarioBuilderRepository scenarioRepository;

    public JournalScenarioTjenesteImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    /**
     * Hent journal på journal id
     * @param dokumentId
     * @return List<JournalpostModell>
     */
    public DokumentModell finnDokumentMedDokumentId(String dokumentId) {
        throw new UnsupportedOperationException("TODO: implementer");
    }

    /**
     * Hent ett journal på journal id
     * @param journalId
     * @return JournalpostModell
     */
    public List<DokumentModell> finnDokumentMedJournalId(String journalId) {
        throw new UnsupportedOperationException("TODO: implementer");
    }

    /**
     * Hent dokumenter på journal id
     * @param journalId
     * @return List<DokumentModell>
     */
    public List<DokumentModell> finnDokumenterMedJournalId(String journalId) {
        throw new UnsupportedOperationException("TODO: implementer");
    }

    /**
     * Finner journalposter med sak_id
     * @param sakId
     * @return  List<JournalpostModell>
     */

    public List<JournalpostModell> finnJournalposterMedSakId(String sakId){
        throw new UnsupportedOperationException("TODO: implementer");
    }

    public JournalpostModell finnJournalpostMedJournalpostId(String journalpostId){
        throw new UnsupportedOperationException("TODO: implementer");
    }

}
