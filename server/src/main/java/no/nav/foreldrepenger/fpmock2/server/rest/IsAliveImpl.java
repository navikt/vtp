package no.nav.foreldrepenger.fpmock2.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "isAlive" })
@Path("/isAlive")
public class IsAliveImpl {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "isAlive", notes = ("Sjekker om systemet er alive for NAIS"))
    public String buildPermitResponse() {
        return "OK";
    }
}
