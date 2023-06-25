package no.nav.vtp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name= "Utilities")
@Path("/dummy/boolean")
public class DummyRestTjenesteBoolean {

    private static final Logger LOG = LoggerFactory.getLogger(DummyRestTjenesteBoolean.class);
    //TODO legge til logging av url request

    @GET
    @Path("/true/{var:.+}")
    @Operation(description = "Returnerer 200 response boolean true")
    public Response getTrue() {
        LOG.info("Fikk en forespørsel på DummyRestTjenesteTrue");
        return Response.ok().entity(true).build();
    }

    @GET
    @Path("/false/{var:.+}")
    @Operation(description = "Returnerer 200 response boolean false")
    public Response getFalse() {
        LOG.info("Fikk en forespørsel på DummyRestTjenesteFalse");
        return Response.ok().entity(false).build();
    }
}
