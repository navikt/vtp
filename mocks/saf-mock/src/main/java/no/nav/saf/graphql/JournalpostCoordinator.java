package no.nav.saf.graphql;

import no.nav.saf.generatedsources.Journalpost;

public interface JournalpostCoordinator {
    Journalpost hentJournalpost(String journalpostId);
}
