package no.nav.saf.graphql;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.saf.Dokumentoversikt;
import no.nav.saf.Journalpost;
import no.nav.saf.SafMock;
import no.nav.saf.exceptions.SafFunctionalException;

public class DokumentoversiktWiring {

    private static final Logger log = LoggerFactory.getLogger(SafMock.class);

    public static RuntimeWiring lagRuntimeWiring(DokumentoversiktFagsakCoordinator oversiktCoordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .type("Query", typeWiring -> typeWiring.dataFetcher("dokumentoversiktFagsak", environment -> {
                    try {
                        final String fagsakId = ((Map<String, String>)environment.getArgument("fagsak")).get("fagsakId");
                        final String fagsaksystem = ((Map<String, String>)environment.getArgument("fagsak")).get("fagsaksystem");

                        log.info("query dokumentoversiktFagsak for fagsakId={}, fagsaksystem={}", fagsakId, fagsaksystem);

                        Dokumentoversikt dokumentoversikt = oversiktCoordinator.hentDokumentoversikt(fagsakId, fagsaksystem );
                        return dokumentoversikt;
                    } catch (SafFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                })).build();
    }
}
