package no.nav.tjeneste.virksomhet.spokelse.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
    @GET
    @Path("/grunnlag")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer sykepenger fra Spøkelse")
    public SykepengeVedtak[] getSykepenger(@QueryParam("fodselsnummer") String fnr, @QueryParam("fom") String fom) {
        List<SykepengeVedtak> tomrespons = new ArrayList<>();
        return tomrespons.toArray(SykepengeVedtak[]::new);
    }

}
