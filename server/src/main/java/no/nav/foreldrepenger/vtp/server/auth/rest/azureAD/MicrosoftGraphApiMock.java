package no.nav.foreldrepenger.vtp.server.auth.rest.azureAD;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AzureAd")
@Path("/MicrosoftGraphApi")
public class MicrosoftGraphApiMock {
    @GET
    @Produces({ "application/json;charset=UTF-8" })
    @Path("/v1.0/me")
    public Response me(@QueryParam("select") String select) {
        // TODO: read Authorization header, maybe we can guess onPremisesSamAccountName from values there
        Map<String, String> response = new HashMap<>();
        response.put("@odata.context", "https://graph.microsoft.com/v1.0/$metadata#users(" + select + ")/$entity");
        response.put("onPremisesSamAccountName", "Z123456");
        return Response.ok(response).build();
    }
}
