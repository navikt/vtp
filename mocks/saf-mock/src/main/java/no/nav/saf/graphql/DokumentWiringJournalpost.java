package no.nav.saf.graphql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.saf.Journalpost;
import no.nav.saf.exceptions.SafFunctionalException;
import no.nav.saf.SafMock;

public class DokumentWiringJournalpost {

    private static final Logger LOG = LoggerFactory.getLogger(SafMock.class);

    public static RuntimeWiring lagRuntimeWiring(JournalpostCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .type("Query", typeWiring -> typeWiring.dataFetcher("journalpost", environment -> {
                    try {
                        final String journalpostId = environment.getArgument("journalpostId");
                        LOG.info("query journalpost for journalpostId={}", journalpostId);

                        Journalpost journalpost = coordinator.hentJournalpost(journalpostId);
                        LOG.info("journalpost hentet for journalpostId={}", journalpostId);

                        return journalpost;
                    } catch (SafFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                })).build();
    }
}
