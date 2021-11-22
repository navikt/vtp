package no.nav.foreldrepenger.vtp.server.selftest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
