package no.nav.saf.journalpost;

import no.nav.saf.domain.visningsmodell.Journalpost;

public interface JournalpostCoordinator {
    Journalpost hentJournalpost(String journalpostId);
}
