package no.nav.saf.graphql;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.saf.Dokumentoversikt;
import no.nav.saf.SafMock;
import no.nav.saf.exceptions.SafFunctionalException;

public class DokumentWiringDokumentoversikt {

    private static final Logger LOG = LoggerFactory.getLogger(SafMock.class);

    public static RuntimeWiring lagRuntimeWiring(DokumentoversiktFagsakCoordinator oversiktCoordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .type("Query", typeWiring -> typeWiring.dataFetcher("dokumentoversiktFagsak", environment -> {
                    try {
                        final String fagsakId = ((Map<String, String>)environment.getArgument("fagsak")).get("fagsakId");
                        final String fagsaksystem = ((Map<String, String>)environment.getArgument("fagsak")).get("fagsaksystem");

                        LOG.info("query dokumentoversiktFagsak for fagsakId={}, fagsaksystem={}", fagsakId, fagsaksystem);

                        Dokumentoversikt dokumentoversikt = oversiktCoordinator.hentDokumentoversikt(fagsakId, fagsaksystem );
                        LOG.info("dokumentoversiktFagsak hentet for fagsakId={}, fagsaksystem={}", fagsakId, fagsaksystem);

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
