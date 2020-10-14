package no.nav.pdl.hentperson;

import graphql.Scalars;
import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.pdl.PdlFunctionalException;
import no.nav.pdl.Person;
import no.nav.pdl.graphql.DateScalar;
import no.nav.pdl.graphql.DateTimeScalar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HentPersonWiring {

    private static final Logger LOG = LoggerFactory.getLogger(HentPersonWiring.class);

    public static RuntimeWiring lagRuntimeWiring(HentPersonCoordinator coordinator) {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.DATE_TIME)
                .scalar(Scalars.GraphQLLong)
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

                        Person person = coordinator.hentPerson(ident, historikk);
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
