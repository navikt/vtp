package no.nav.foreldrepenger.vtp.server.api.scenario_over_rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Api(tags = {"Testscenario-test"})
@Path("/api/testscenario/test")
public class TestscenarioOverRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(TestscenarioOverRestTjeneste.class);

    @Context
    private TestscenarioRepository testscenarioRepository;


    @POST
    @Path("/initialiser")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Initialiserer et testscenario basert på angitt json streng"), response = TestscenarioResponseDto.class)
    public TestscenarioResponseDto initialiserTestScenario(String testscenarioJson) {
        LOG.info("testscenario er sendt over og ser slikt ut: {}", testscenarioJson);
        Testscenario testscenario = testscenarioRepository.opprettTestscenarioFraJsonString(testscenarioJson);
        return null; // returnerer null foreløpelig ettersom strukturen på responsen er usikker.
    }


}

