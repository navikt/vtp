package no.nav.tjeneste.virksomhet.kelvin;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Tag(name = "Kelvinmock")
@Path("/kelvin")
public class KelvinMock {

    private static final Logger LOG = LoggerFactory.getLogger(KelvinMock.class);
    private static final String LOG_PREFIX = "Kevlin Rest kall til {}";

    private final TestscenarioBuilderRepository scenarioRepository;

    public KelvinMock(@Context TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/maksimum")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer arbeidsavklaringspenger fra Kelvin og Arena")
    public ArbeidsavklaringspengerResponse postAAP(PersonRequest personRequest) {
        // TODO: Utvide IAY-modell med Kelvin-AAP og populere response med data fra testscenario
        return new ArbeidsavklaringspengerResponse(List.of());
    }

    public record PersonRequest(String personidentifikator, LocalDate fraOgMedDato, LocalDate tilOgMedDato) { }

}
