package no.nav.vtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/dummy/boolean")
public class DummyRestTjenesteBoolean {

    private static final Logger LOG = LoggerFactory.getLogger(DummyRestTjenesteBoolean.class);
    //TODO legge til logging av url request

    @GET
    @Path("/true/{var:.+}")
    public Response getTrue() {
        LOG.info("Fikk en forespørsel på DummyRestTjenesteTrue");
        return Response.ok().entity(true).build();
    }

    @GET
    @Path("/false/{var:.+}")
    public Response getFalse() {
        LOG.info("Fikk en forespørsel på DummyRestTjenesteFalse");
        return Response.ok().entity(false).build();
    }
}
