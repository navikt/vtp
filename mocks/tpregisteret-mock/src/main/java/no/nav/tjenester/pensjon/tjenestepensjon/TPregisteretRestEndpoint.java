package no.nav.tjenester.pensjon.tjenestepensjon;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.OK;

@Api(tags = {"Simuler TPregisteret - rest API"})
@Path("/tpregisteret/person/")
public class TPregisteretRestEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(TPregisteretSoapEndpoint.class);

    TestscenarioRepositoryImpl testscenarioRepository;

    TPregisteretService simulerOffentligTPService =  new TPregisteretService();

    public TPregisteretRestEndpoint() {
        try {
            testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @GET
    @Path("/{fnr}/tpordninger")
    @ApiOperation(
            value = "Stub for simuler tjenestepensjon",
            notes = ("ApiOperation b")
    )
    public Response buildPermitResponse(
            @ApiParam(value = "PathParam fnr(fødselsnummer)", required = true) @NotNull @PathParam("fnr") Integer fnr
    ) {
        return Response.status(OK).entity(
                simulerOffentligTPService.validateRequestAndRespond(null)
        ).build();
    }
}