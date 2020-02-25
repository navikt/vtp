package no.nav.tjenester.pensjon.tjenestepensjon.v1;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.IOException;

@Api(tags = {"soap/tjenestepensjon/simuler/test"})
@Path("/api/test")
public class SimulerOffentligTjenestepensjonTjenesteSoapEndPoint {
    private static final Logger LOG = LoggerFactory.getLogger(SimulerOffentligTjenestepensjonTjenesteSoapEndPoint.class);

    TestscenarioRepositoryImpl testscenarioRepository;

    public SimulerOffentligTjenestepensjonTjenesteSoapEndPoint() {
        try {
            testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "beregnetskatt", notes = ("Returnerer beregnetskatt fra Sigrun"))
    public Response buildPermitResponse(@Context UriInfo ui, @Context HttpHeaders httpHeaders) {

        return Response.status(200).entity(null).build();
    }
}