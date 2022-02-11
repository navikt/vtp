package no.nav.pdl.hentIdenterBolk;

import static graphql.scalars.java.JavaPrimitives.GraphQLLong;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.pdl.HentIdenterBolkResult;
import no.nav.pdl.PdlFunctionalException;
import no.nav.pdl.graphql.DateScalar;
import no.nav.pdl.graphql.DateTimeScalar;

public class HentIdenterBolkWiring {

    private HentIdenterBolkWiring() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(HentIdenterBolkWiring.class);

    public static RuntimeWiring  lagRuntimeWiring(HentIdenterBolkCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .scalar(GraphQLLong)
                .type(
                        "Query",
                        typeWiring -> typeWiring.dataFetcher("hentIdenterBolk", environment -> {
                                    try {
                                        List<String> identer = environment.getArgument("identer");


                                        LOG.info("query hentIdenterBolk for ident={}", identer);

                                        List<HentIdenterBolkResult> identerBolkResults = coordinator.hentIdenterBolk(identer);
                                        LOG.info("hentIdenterBolk hentet for ident={}", identer);

                                        return identerBolkResults;
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
