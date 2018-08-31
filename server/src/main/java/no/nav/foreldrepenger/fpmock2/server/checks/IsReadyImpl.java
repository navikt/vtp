package no.nav.foreldrepenger.fpmock2.server.checks;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "isReady" })
@Path("/isReady")
public class IsReadyImpl {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "isReady", notes = ("Sjekker om systemet er ready for NAIS"))
    public String buildPermitResponse() {
        return " { \"Response\" : {\"InfotrygdSakStatus\" : {\"StatusCode\" : {\"Value\" : " +
            "\"status:ok\"}}}}";
    }
}
