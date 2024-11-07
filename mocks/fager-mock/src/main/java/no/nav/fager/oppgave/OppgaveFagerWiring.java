package no.nav.fager.oppgave;

import java.util.Map;

import no.nav.fager.NyOppgaveMutationRequest;

import no.nav.fager.OppgaveUtfoertMutationRequest;

import no.nav.fager.OppgaveUtgaattMutationRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.fager.exceptions.FagerFunctionalException;

public class OppgaveFagerWiring {
    private static final Logger LOG = LoggerFactory.getLogger(OppgaveFagerWiring.class);
    protected static final String MUTATION = "Mutation";

    public static RuntimeWiring lagRuntimeWiring(OppgaveFagerCoordinator oppgaveFagerCoordinator, RuntimeWiring originalWiring) {
        return RuntimeWiring.newRuntimeWiring(originalWiring)
                .type(MUTATION, typeWiring -> typeWiring.dataFetcher(NyOppgaveMutationRequest.OPERATION_NAME, env -> {
                    try {
                        final var nyOppgave = (Map<String, Object>) env.getArgument("nyOppgave");

                        final var notifikasjon = (Map<String, String>) nyOppgave.get("notifikasjon");
                        final var metadata = (Map<String, String>) nyOppgave.get("metadata");

                        var grupperingsid = metadata.get("grupperingsid");
                        var virksomhetsnummer = metadata.get("virksomhetsnummer");

                        var merkelapp = notifikasjon.get("merkelapp");
                        var tekst = notifikasjon.get("tekst");
                        var lenke = notifikasjon.get("lenke");

                        LOG.info("FAGER: nyOppgave opprettet grupperingsid={}, org={}", grupperingsid, virksomhetsnummer);
                        return oppgaveFagerCoordinator.opprettOppgave(grupperingsid, merkelapp, virksomhetsnummer, tekst, lenke);
                    } catch (FagerFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                }))
                .type(MUTATION, typeWiring -> typeWiring.dataFetcher(OppgaveUtfoertMutationRequest.OPERATION_NAME, environment -> {
                    try {
                        final String id = environment.getArgument("id");
                        LOG.info("mutation oppgaveUtfoert for id={}", id);
                        return oppgaveFagerCoordinator.utførttOppgave(id);
                    } catch (FagerFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                }))
                .type(MUTATION, typeWiring -> typeWiring.dataFetcher(OppgaveUtgaattMutationRequest.OPERATION_NAME, environment -> {
                    try {
                        final String id = environment.getArgument("id");
                        LOG.info("mutation oppgaveUtgaat for id={}", id);
                        return oppgaveFagerCoordinator.utgåttOppgave(id);
                    } catch (FagerFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                }))
                .build();
    }
}
