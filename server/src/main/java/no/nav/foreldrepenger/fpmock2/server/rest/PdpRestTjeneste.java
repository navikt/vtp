package no.nav.foreldrepenger.fpmock2.server.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "ABAC-PDP-Mock" })
@Path("/asm-pdp/authorize")
public class PdpRestTjeneste {

    @SuppressWarnings("unused")
    @POST
    @Produces("application/xacml+json")
    @ApiOperation(value = "asm-pdp/authorize", notes = ("Mock impl av ABAC PDP authorize"))
    public Response authorize(InputStream entity) throws IOException {
        return Response.ok(buildPermitResponse()).build();
    }

    @SuppressWarnings("unused")
    private String buildDenyResponse() {
        return " { \"Response\" : {\"Decision\" : \"Deny\",\"InfotrygdSakStatus\" : {\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}}";
    }

    private String buildPermitResponse() {
        return " { \"Response\" : {\"Decision\" : \"Permit\",\"InfotrygdSakStatus\" : {\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}}";
    }
}
