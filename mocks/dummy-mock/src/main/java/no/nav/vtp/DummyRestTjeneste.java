package no.nav.vtp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

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