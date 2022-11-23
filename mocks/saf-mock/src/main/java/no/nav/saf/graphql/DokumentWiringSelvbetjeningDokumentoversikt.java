package no.nav.saf.graphql;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.saf.exceptions.SafFunctionalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DokumentWiringSelvbetjeningDokumentoversikt {
    private static final Logger LOG = LoggerFactory.getLogger(DokumentWiringSelvbetjeningDokumentoversikt.class);

    public static RuntimeWiring lagRuntimeWiring(DokumentoversiktSelvbetjening coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .type("Query", typeWiring -> typeWiring.dataFetcher("dokumentoversiktSelvbetjening", environment -> {
                    try {
                        final String fnr = environment.getArgument("ident");
                        LOG.info("query selvbetjeningdokumentoversikt for fnr={}", fnr);

                        var dokumentoversikt = coordinator.hentDokumentoversikt(fnr);
                        LOG.info("selvbetjeningdokumentoversikt hentet for fnr={}", fnr);

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
