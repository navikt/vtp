package no.nav.fager.beskjed;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.fager.NyBeskjedMutationRequest;
import no.nav.fager.exceptions.FagerFunctionalException;

public class BeskjedFagerWiring {
    protected static final String MUTATION = "Mutation";
    private static final Logger LOG = LoggerFactory.getLogger(BeskjedFagerWiring.class);

    public static RuntimeWiring lagRuntimeWiring(BeskjedFagerCoordinator oversiktCoordinator, RuntimeWiring originalWiring) {
        return RuntimeWiring.newRuntimeWiring(originalWiring)
                .type(MUTATION, typeWiring -> typeWiring.dataFetcher(NyBeskjedMutationRequest.OPERATION_NAME, env -> {
                    try {

                        final var nyBeskjed = (Map<String, Object>) env.getArgument("nyBeskjed");

                        final var notifikasjon = (Map<String, String>) nyBeskjed.get("notifikasjon");
                        final var metadata = (Map<String, String>) nyBeskjed.get("metadata");

                        var grupperingsid = metadata.get("grupperingsid");
                        var virksomhetsnummer = metadata.get("virksomhetsnummer");

                        var merkelapp = notifikasjon.get("merkelapp");
                        var tekst = notifikasjon.get("tekst");
                        var lenke = notifikasjon.get("lenke");

                        LOG.info("mutation nyBeskjed for grupperingsid={}, org={}", grupperingsid, virksomhetsnummer);
                        return oversiktCoordinator.opprettBeskjed(grupperingsid, merkelapp, virksomhetsnummer, tekst, lenke);
                    } catch (FagerFunctionalException e) {
                        return DataFetcherResult.newResult().data(null).error(e).build();
                    }
                }))
                .build();
    }
}
