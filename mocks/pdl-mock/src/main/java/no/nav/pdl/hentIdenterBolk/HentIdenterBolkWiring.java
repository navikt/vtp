package no.nav.pdl.hentIdenterBolk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.Scalars;
import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.pdl.Identliste;
import no.nav.pdl.PdlFunctionalException;
import no.nav.pdl.graphql.DateScalar;
import no.nav.pdl.graphql.DateTimeScalar;
import no.nav.pdl.hentIdenter.HentIdenterWiring;

public class HentIdenterBolkWiring {

    private static final Logger LOG = LoggerFactory.getLogger(HentIdenterWiring.class);

    public static RuntimeWiring  lagRuntimeWiring(HentIdenterBolkCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .scalar(Scalars.GraphQLLong)
                .type(
                        "Query",
                        typeWiring -> typeWiring.dataFetcher("hentIdenterBolk", environment -> {
                                    try {
                                        var ident = (String []) environment.getArgument("identer");
                                        LOG.info("query hentIdenterBolk for ident={}", ident);

                                        Identliste identliste = coordinator.hentIdenterBolk(ident);
                                        LOG.info("blaba hentet for ident={}", ident);

                                        return identliste;
                                    } catch (PdlFunctionalException e) {
                                        return DataFetcherResult.newResult()
                                                .data(null)
                                                .error(e)
                                                .build();
                                    }
                                }
                        )
                )
                .build();
    }
}
