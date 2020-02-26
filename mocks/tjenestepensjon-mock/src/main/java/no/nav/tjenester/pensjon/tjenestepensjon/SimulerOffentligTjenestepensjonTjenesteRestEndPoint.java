package no.nav.tjenester.pensjon.tjenestepensjon;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.tjenester.pensjon.tjenestepensjon.model.SimulerPensjonRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.OK;

@Api(tags = {"Simuler tjenestepensjon - rest API"})
@Path("/tpregisteret/person/")
public class SimulerOffentligTjenestepensjonTjenesteRestEndPoint {
    private static final Logger LOG = LoggerFactory.getLogger(SimulerOffentligTjenestepensjonTjenesteSoapEndPoint.class);

    TestscenarioRepositoryImpl testscenarioRepository;

    SimulerOffentligTjenestepensjonService simulerOffentligTPService =  new SimulerOffentligTjenestepensjonService();

    public SimulerOffentligTjenestepensjonTjenesteRestEndPoint() {
        try {
            testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }




    @POST
    @Path("/{fnr}/tpordninger")
    @ApiOperation(
            value = "Stub for simuler tjenestepensjon",
            notes = ("ApiOperation b")
    )
    public Response buildPermitResponse(
            @ApiParam(value = "PathParam fnr(fødselsnummer)", required = true) @NotNull @PathParam("fnr") Integer fnr,
            @ApiParam(value = "SimulerPensjonRequest body", required = true) @NotNull SimulerPensjonRequest body
    ) {
        return Response.status(OK).entity(
                simulerOffentligTPService.start(body)
        ).build();
    }
}