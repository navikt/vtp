package no.nav.vtp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Api(tags = {"Utilities"})
@Path("/dummy/{var:.+}")
public class DummyRestTjeneste {

    public DummyRestTjeneste() {

    }

    @GET
    @ApiOperation(value = "get", notes = ("Returnerer ett blankt resultat"))
    public Response get() {
        return Response.ok().build();
    }

    @POST
    @ApiOperation(value = "post", notes = ("Returnerer ett blankt resultat"))
    public Response post() {
        return Response.ok().build();
    }

    @OPTIONS
    @ApiOperation(value = "options", notes = ("Returnerer ett blankt resultat"))
    public Response options() {
        return Response.ok().build();
    }
}