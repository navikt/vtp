package no.nav.foreldrepenger.vtp.server.auth.rest.idporten;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.server.MockServer;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;

import no.nav.foreldrepenger.vtp.server.auth.rest.WellKnownResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

@Tag(name = "ID-Porten")
@Path(IdportenLoginTjeneste.TJENESTE_PATH)
public class IdportenLoginTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(IdportenLoginTjeneste.class);
    protected static final String TJENESTE_PATH = "/idporten";
    private static final String ISSUER = "http://vtp/rest/idporten";

    @GET
    @Path("/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Idporten Discovery url")
    public Response wellKnown(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        LOG.info("Kall på well-known endepunkt");
        String baseUrl = getBaseUrl(req);
        var wellKnownResponse = new WellKnownResponse(ISSUER, baseUrl + "/authorize", baseUrl + "/jwks", baseUrl + "/token");
        return Response.ok(wellKnownResponse).build();
    }

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "idporten AD is OK";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/jwks")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "idporten/discovery/keys")
    public Response authorize() {
        String jwks = KeyStoreTool.getJwks();
        return Response.ok(jwks).build();
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public Response hent(@Context HttpServletRequest req, @QueryParam("redirect") URI redirectUri) {
        String tmpl = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <title>Velg bruker</title>
                    </head>
                        <body>
                        <div style="text-align:center;width:100%%;">
                           <caption><h3>Fødselsnummer:</h3></caption>
                            <form action="/rest/idporten/token" method="post">
                              <input type="hidden" name="redirect" value="%s" />
                              <input type="text" name="fnr" />
                              <input type="submit" value="Token, takk!" />
                            </form>
                        </div>
                    </body>
                    </html>
                """;
        return Response.ok(String.format(tmpl, redirectUri), MediaType.TEXT_HTML).build();
    }

    @POST
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "idporten/access_token")
    @SuppressWarnings("unused")
    public Response accessToken(@Context HttpServletRequest req,
                                @FormParam("fnr") String fnr,
                                @FormParam("redirect") URI redirectUri) {

        var token = IdportenOidcTokenGenerator.idportenUserToken(fnr, ISSUER);
        var cookieTemplate = "selvbetjening-idtoken=%s;Path=/";
        return Response.seeOther(redirectUri).header("Set-Cookie", String.format(cookieTemplate, token)).build();
    }

    @GET
    @Path("/bruker")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "idporten/access_token")
    public Response accessToken(@QueryParam("fnr") String fnr) {
        var token = IdportenOidcTokenGenerator.idportenUserToken(fnr, ISSUER);
        return Response.ok(new Oauth2AccessTokenResponse(token)).build();
    }

    private static String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort() + MockServer.CONTEXT_PATH + TJENESTE_PATH;
    }
}
