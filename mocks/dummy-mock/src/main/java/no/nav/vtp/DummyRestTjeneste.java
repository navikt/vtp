package no.nav.vtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/dummy/{var:.+}")
public class DummyRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(DummyRestTjeneste.class);

    public DummyRestTjeneste() {

    }

    @GET
    public Response get() {
        LOG.info("Fikk en forespørsel på DummyRestTjeneste");
        return Response.ok().build();
    }

    @POST
    public Response post() {
        LOG.info("Lager en forespørsel på DummyRestTjeneste");
        return Response.ok().build();
    }

    @OPTIONS
    public Response options() {
        return Response.ok().build();
    }
}
