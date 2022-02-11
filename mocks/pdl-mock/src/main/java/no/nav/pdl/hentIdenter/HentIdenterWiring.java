package no.nav.pdl.hentIdenter;

import static graphql.scalars.java.JavaPrimitives.GraphQLLong;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.pdl.Identliste;
import no.nav.pdl.PdlFunctionalException;
import no.nav.pdl.exceptions.ErrorCode;
import no.nav.pdl.graphql.DateScalar;
import no.nav.pdl.graphql.DateTimeScalar;


public class HentIdenterWiring {

    private static final Logger LOG = LoggerFactory.getLogger(HentIdenterWiring.class);

    private HentIdenterWiring() {
    }

    public static RuntimeWiring lagRuntimeWiring(HentIdenterCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .scalar(GraphQLLong)
                .type(
                        "Query",
                        typeWiring -> typeWiring.dataFetcher("hentIdenter", environment -> {
                                    try {
                                        var ident = (String) environment.getArgument("ident");
                                        var grupper = (List<String>) environment.getArgument("grupper");
                                        LOG.info("query hentPerson for ident={}", ident);

                                        Identliste identliste = coordinator.hentIdenter(ident, grupper);
                                        if (identliste == null) {
                                            return DataFetcherResult.newResult()
                                                    .error(ErrorCode.NOT_FOUND.construct(environment, "Fant ikke person"))
                                                    .build();
                                        }
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
