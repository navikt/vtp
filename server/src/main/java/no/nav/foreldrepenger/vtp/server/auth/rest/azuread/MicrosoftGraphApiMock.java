package no.nav.foreldrepenger.vtp.server.auth.rest.azuread;

import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

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
