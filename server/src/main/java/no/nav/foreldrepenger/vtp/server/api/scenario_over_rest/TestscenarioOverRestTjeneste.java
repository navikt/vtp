package no.nav.foreldrepenger.vtp.server.api.scenario_over_rest;


import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/api/testscenario/test")
public class TestscenarioOverRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(TestscenarioOverRestTjeneste.class);

    @Context
    private TestscenarioRepository testscenarioRepository;

    public void setTestscenarioRepository(TestscenarioRepository testscenarioRepository) {
        this.testscenarioRepository = testscenarioRepository;
    }


    @POST
    @Path("/load")
    @Produces(MediaType.APPLICATION_JSON)
    public TestscenarioResponseDto loadTestScenario(String testscenarioJson) {
        LOG.info("testscenario er sendt over og ser slikt ut: {}", testscenarioJson);
        Testscenario testscenario = testscenarioRepository.opprettTestscenarioFraJsonString(testscenarioJson);
        return null;
    }


}

