package no.nav.fager.sak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.execution.DataFetcherResult;
import graphql.schema.idl.RuntimeWiring;
import no.nav.fager.HardDeleteSakMutationRequest;
import no.nav.fager.NySakMutationRequest;
import no.nav.fager.NyStatusSakMutationRequest;
import no.nav.fager.TilleggsinformasjonSakMutationRequest;
import no.nav.fager.exceptions.FagerFunctionalException;
import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.SakModell;

public class SakFagerWiring {
    protected static final String MUTATION = "Mutation";
    private static final Logger LOG = LoggerFactory.getLogger(SakFagerWiring.class);

    public static RuntimeWiring lagRuntimeWiring(SakFagerCoordinator oversiktCoordinator, RuntimeWiring originalWiring) {
        return RuntimeWiring.newRuntimeWiring(originalWiring)
                .type(MUTATION, typeWiring -> typeWiring.dataFetcher(NySakMutationRequest.OPERATION_NAME, environment -> {
                    try {
                        final String grupperingsid = environment.getArgument("grupperingsid");
                        final String tittel =  environment.getArgument("tittel");
                        final String virksomhetsnummer =  environment.getArgument("virksomhetsnummer");
                        final String merkelapp =  environment.getArgument("merkelapp");
                        final String lenke =  environment.getArgument("lenke");
                        final String initiellStatus =  environment.getArgument("initiellStatus");
                        final String overstyrStatustekstMed =  environment.getArgument("overstyrStatustekstMed");

                        LOG.info("mutation nySak for grupperingsid={}, org={}", grupperingsid, virksomhetsnummer);
                        return oversiktCoordinator.opprettSak(grupperingsid, merkelapp, virksomhetsnummer, tittel, lenke, SakFagerCoordinator.SakStatus.valueOf(initiellStatus), overstyrStatustekstMed);
                    } catch (FagerFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                }))
                .type(MUTATION, typeWiring -> typeWiring.dataFetcher(NyStatusSakMutationRequest.OPERATION_NAME, environment -> {
                    try {
                        final String id = environment.getArgument("id");
                        final String nyStatus = environment.getArgument("nyStatus");
                        final String overstyrStatustekstMed = environment.getArgument("overstyrStatustekstMed");

                        LOG.info("mutation nyStatusSak for id={}", id);
                        return oversiktCoordinator.nyStatusSak(id, SakModell.SakStatus.valueOf(nyStatus), overstyrStatustekstMed);
                    } catch (FagerFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                }))
                .type(MUTATION, typeWiring -> typeWiring.dataFetcher(TilleggsinformasjonSakMutationRequest.OPERATION_NAME, environment -> {
                    try {
                        final String id = environment.getArgument("id");
                        final String tilleggsinformasjon = environment.getArgument("tilleggsinformasjon");

                        LOG.info("mutation tilleggsinformasjonSak for id={}", id);
                        return oversiktCoordinator.tilleggsinformasjonSak(id, tilleggsinformasjon);
                    } catch (FagerFunctionalException e) {
                        return DataFetcherResult.newResult()
                                .data(null)
                                .error(e)
                                .build();
                    }
                }))
                .type(MUTATION, typeWiring -> typeWiring.dataFetcher(HardDeleteSakMutationRequest.OPERATION_NAME, environment -> {
                    try {
                        final String id = environment.getArgument("id");
                        LOG.info("mutation hardDeleteSak for id={}", id);
                        return oversiktCoordinator.hardDeleteSak(id);
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
