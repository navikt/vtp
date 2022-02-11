package no.nav.pdl.hentperson;

import static graphql.scalars.java.JavaPrimitives.GraphQLLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.pdl.PdlFunctionalException;
import no.nav.pdl.exceptions.ErrorCode;
import no.nav.pdl.graphql.DateScalar;
import no.nav.pdl.graphql.DateTimeScalar;


public class HentPersonWiring {

    private static final Logger LOG = LoggerFactory.getLogger(HentPersonWiring.class);

    private HentPersonWiring() {
    }

    public static RuntimeWiring lagRuntimeWiring(HentPersonCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .scalar(GraphQLLong)
                .type("Query", typeWiring -> typeWiring.dataFetcher("hentPerson", environment -> {
                    try {
                        var ident = (String) environment.getArgument("ident");
                        var historikk = environment.getField()
                                .getSelectionSet()
                                .getSelections()
                                .stream()
                                .anyMatch(felt -> felt.toString().contains("name='navn'")
                                        && felt.toString().contains("Argument{name='historikk', value=BooleanValue{value=true}"));

                        LOG.info("query hentPerson for ident={}, historikk", ident);

                        var person = coordinator.hentPerson(ident, historikk);
                        if (person == null) {
                            return DataFetcherResult.newResult()
                                    .error(ErrorCode.NOT_FOUND.construct(environment, "Fant ikke person"))
                                    .build();
                        }
                        LOG.info("person hentet for ident={}", ident);

                        return person;
                    } catch (PdlFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                })).build();
    }
}
