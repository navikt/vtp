package no.nav.pdl.hentpersonBolk;

import static graphql.scalars.java.JavaPrimitives.GraphQLLong;

import java.util.ArrayList;
import java.util.List;

import no.nav.pdl.HentPersonBolkResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.pdl.PdlFunctionalException;
import no.nav.pdl.exceptions.ErrorCode;
import no.nav.pdl.graphql.DateScalar;
import no.nav.pdl.graphql.DateTimeScalar;
import no.nav.pdl.hentperson.HentPersonCoordinator;


public class HentPersonBolkWiring {

    private static final Logger LOG = LoggerFactory.getLogger(HentPersonBolkWiring.class);

    private HentPersonBolkWiring() {
    }

    public static RuntimeWiring lagRuntimeWiring(HentPersonCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .scalar(GraphQLLong)
                .type(
                        "Query",
                        typeWiring -> typeWiring.dataFetcher("hentPersonBolk", environment -> {
                            try {
                                List<String> identer = environment.getArgument("identer");
                                List<HentPersonBolkResult> personer = new ArrayList<>();
                                for (var ident : identer) {
                                    var person = coordinator.hentPerson(ident, false);
                                    if (person == null) {
                                        return DataFetcherResult.newResult()
                                                .error(ErrorCode.NOT_FOUND.construct(environment, "Fant ikke person"))
                                                .build();
                                    }
                                    personer.add(new HentPersonBolkResult(ident, person, null));
                                }
                                LOG.info("Personinfo hentet fra pdl for identene={}", identer);
                                return personer;
                            } catch (PdlFunctionalException e) {
                                return DataFetcherResult.newResult()
                                        .data(null)
                                        .error(e)
                                        .build();
                            }
                })).build();
    }
}
