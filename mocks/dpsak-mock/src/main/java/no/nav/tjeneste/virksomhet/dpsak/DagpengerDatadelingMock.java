package no.nav.tjeneste.virksomhet.dpsak;

import java.time.LocalDate;
import java.util.ArrayList;
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


@Tag(name = "Dagpengermock")
@Path("/dagpenger/datadeling/v1")
public class DagpengerDatadelingMock {

    private static final Logger LOG = LoggerFactory.getLogger(DagpengerDatadelingMock.class);
    private static final String LOG_PREFIX = "Dagpenger Rest kall til {}";

    private final TestscenarioBuilderRepository scenarioRepository;

    public DagpengerDatadelingMock(@Context TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/perioder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer perioder med dagpenger fra DPsak og Arena")
    public DagpengerRettighetsperioder postDagpengerPerioder(PersonRequest personRequest) {
        // TODO: Utvide IAY-modell med Dp-datadeling-Dagpenger og populere response med data fra testscenario
        var tomRespons = new DagpengerRettighetsperioder(personRequest.personIdent(), List.of());
        return tomRespons;
    }

    @POST
    @Path("/beregninger")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer utbetalte dagpenger fra DPsak")
    public DagpengerUtbetalingsdag[] postDagpengerBeregning(PersonRequest personRequest) {
        // TODO: Utvide IAY-modell med Dp-datadeling-Dagpenger og populere response med data fra testscenario
        List<DagpengerUtbetalingsdag> tomrespons = new ArrayList<>();
        return tomrespons.toArray(DagpengerUtbetalingsdag[]::new);
    }


    public record PersonRequest(String personIdent, LocalDate fraOgMedDato, LocalDate tilOgMedDato) { }

}
