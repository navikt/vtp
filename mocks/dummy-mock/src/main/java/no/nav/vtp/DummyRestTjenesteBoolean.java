package no.nav.vtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Api(tags = {"Utilities"})
@Path("/dummy/boolean")
public class DummyRestTjenesteBoolean {

    private static final Logger LOG = LoggerFactory.getLogger(DummyRestTjenesteBoolean.class);
    //TODO legge til logging av url request

    @GET
    @Path("/true/{var:.+}")
    @ApiOperation(value = "get", notes = ("Returnerer 200 response boolean true"))
    public Response getTrue() {
        LOG.info("Fikk en forespørsel på DummyRestTjenesteTrue");
        return Response.ok().entity(true).build();
    }

    @GET
    @Path("/false/{var:.+}")
    @ApiOperation(value = "get", notes = ("Returnerer 200 response boolean false"))
    public Response getFalse() {
        LOG.info("Fikk en forespørsel på DummyRestTjenesteFalse");
        return Response.ok().entity(false).build();
    }
}
