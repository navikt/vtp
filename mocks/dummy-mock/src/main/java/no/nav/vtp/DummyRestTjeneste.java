package no.nav.vtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Api(tags = {"Utilities"})
@Path("/dummy/{var:.+}")
public class DummyRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(DummyRestTjeneste.class);

    public DummyRestTjeneste() {

    }

    @GET
    @ApiOperation(value = "get", notes = ("Returnerer ett blankt resultat"))
    public Response get() {
        LOG.info("Fikk en forespørsel på DummyRestTjeneste");
        return Response.ok().build();
    }

    @POST
    @ApiOperation(value = "post", notes = ("Returnerer ett blankt resultat"))
    public Response post() {
        LOG.info("Lager en forespørsel på DummyRestTjeneste");
        return Response.ok().build();
    }

    @OPTIONS
    @ApiOperation(value = "options", notes = ("Returnerer ett blankt resultat"))
    public Response options() {
        return Response.ok().build();
    }
}
