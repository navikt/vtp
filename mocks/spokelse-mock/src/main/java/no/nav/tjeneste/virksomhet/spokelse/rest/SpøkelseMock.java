package no.nav.tjeneste.virksomhet.spokelse.rest;

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

@Tag(name = "Spokelsemock")
@Path("/spokelse")
public class SpøkelseMock {

    private static final Logger LOG = LoggerFactory.getLogger(SpøkelseMock.class);
    private static final String LOG_PREFIX = "Spøkelse Rest kall til {}";

    private final TestscenarioBuilderRepository scenarioRepository;

    public SpøkelseMock(@Context TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer sykepenger fra Spøkelse")
    public SykepengeVedtak[] postSykepenger(PersonRequest personRequest) {
        // TODO: Utvide IAY-modell med Spøkelse-SP og populere response med data fra testscenario
        List<SykepengeVedtak> tomrespons = new ArrayList<>();
        return tomrespons.toArray(SykepengeVedtak[]::new);
    }

    public record PersonRequest(String fodselsnummer, LocalDate fom) { }

}
