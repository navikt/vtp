package no.nav.pdl.hentIdenter;

import graphql.Scalars;
import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.pdl.Identliste;
import no.nav.pdl.PdlFunctionalException;
import no.nav.pdl.Person;
import no.nav.pdl.graphql.DateScalar;
import no.nav.pdl.graphql.DateTimeScalar;
import no.nav.pdl.hentperson.HentPersonCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HentIdenterWiring {

    private static final Logger LOG = LoggerFactory.getLogger(HentIdenterWiring.class);

    public static RuntimeWiring lagRuntimeWiring(HentIdenterCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .scalar(Scalars.GraphQLLong)
                .type(
                        "Query",
                        typeWiring -> typeWiring.dataFetcher("hentIdenter", environment -> {
                                    try {
                                        var ident = (String) environment.getArgument("ident");
                                        LOG.info("query hentPerson for ident={}", ident);

                                        Identliste identliste = coordinator.hentIdenter(ident);
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
