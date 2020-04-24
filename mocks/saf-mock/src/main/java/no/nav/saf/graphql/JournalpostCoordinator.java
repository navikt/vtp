package no.nav.saf.graphql;

import no.nav.saf.Journalpost;

public interface JournalpostCoordinator {
    Journalpost hentJournalpost(String journalpostId);
}
