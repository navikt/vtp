package no.nav.pdl.hentIdenterBolk;

import static graphql.scalars.java.JavaPrimitives.GraphQLLong;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.pdl.HentIdenterBolkResult;
import no.nav.pdl.PdlFunctionalException;
import no.nav.pdl.graphql.DateScalar;
import no.nav.pdl.graphql.DateTimeScalar;
import no.nav.pdl.hentIdenter.HentIdenterCoordinator;

public class HentIdenterBolkWiring {

    private HentIdenterBolkWiring() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(HentIdenterBolkWiring.class);

    public static RuntimeWiring  lagRuntimeWiring(HentIdenterCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .scalar(GraphQLLong)
                .type(
                        "Query",
                        typeWiring -> typeWiring.dataFetcher("hentIdenterBolk", environment -> {
                                    try {
                                        List<String> identer = environment.getArgument("identer");
                                        List<String> grupper = environment.getArgument("grupper");

                                        List<HentIdenterBolkResult> identerBolkResults = new ArrayList<>();
                                        for (var ident : identer) {
                                           var identliste = coordinator.hentIdenter(ident, grupper);
                                           var hentIDenterBolkResult = new HentIdenterBolkResult.Builder()
                                                   .setCode("ok")
                                                   .setIdent(ident)
                                                   .setIdenter(identliste.getIdenter())
                                                   .build();
                                           identerBolkResults.add(hentIDenterBolkResult);
                                        }
                                        LOG.info("Identer hentet fra pdl {} for identene={}", grupper, identer);
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
