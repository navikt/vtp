package no.nav.pdl.hentGeografiskTilknytning;

import static graphql.scalars.java.JavaPrimitives.GraphQLLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.pdl.PdlFunctionalException;
import no.nav.pdl.exceptions.ErrorCode;
import no.nav.pdl.graphql.DateScalar;
import no.nav.pdl.graphql.DateTimeScalar;


public class HentGeografiskTilknytningWiring {

    private HentGeografiskTilknytningWiring() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(HentGeografiskTilknytningWiring.class);

    public static RuntimeWiring lagRuntimeWiring(HentGeografiskTilknytningCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .scalar(GraphQLLong)
                .type("Query", typeWiring -> typeWiring.dataFetcher("hentGeografiskTilknytning", environment -> {
                    try {
                        var ident = (String) environment.getArgument("ident");
                        var geografiskTilknytning = coordinator.hentGeografiskTilknytning(ident);

                        if (geografiskTilknytning == null) {
                            return DataFetcherResult.newResult()
                                    .error(ErrorCode.NOT_FOUND.construct(environment, "Fant ikke person"))
                                    .build();
                        }

                        LOG.info("Geografisk Tilknytning hentet fra pdl for ident={}", ident);
                        return geografiskTilknytning;
                    } catch (PdlFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                })).build();
    }
}
