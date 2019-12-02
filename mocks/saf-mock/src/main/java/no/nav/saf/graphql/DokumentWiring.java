package no.nav.saf.graphql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.saf.generatedsources.Journalpost;
import no.nav.saf.exceptions.SafFunctionalException;
import no.nav.saf.SafMock;

public class DokumentWiring {

    private static final Logger log = LoggerFactory.getLogger(SafMock.class);

    public static RuntimeWiring lagRuntimeWiring(JournalpostCoordinator journalpostCoordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .type("Query", typeWiring -> typeWiring.dataFetcher("journalpost", environment -> {
                    try {
                        final String journalpostId = environment.getArgument("journalpostId");
//                        SafRequestContext safRequestContext = environment.getContext();
//                        safRequestContext.getSecurityContext().getOidcTokenBody();
                        log.info("query journalpost for journalpostId={}", journalpostId);
                        Journalpost journalpost = journalpostCoordinator.hentJournalpost(journalpostId/*, safRequestContext*/);
                        log.info("journalpost hentet for journalpostId={}", journalpostId);
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
