package no.nav.foreldrepenger.vtp.server.auth.rest.azureAD;

import static no.nav.foreldrepenger.vtp.server.auth.rest.azureAD.AADRestTjeneste.createIdToken;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"loginservice"})
@Path("/loginservice")
public class LoginserviceLoginTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(LoginserviceLoginTjeneste.class);

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
                    <form action="/rest/loginservice/token" method="post">
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
    @ApiOperation(value = "azureAd/access_token", notes = ("Mock impl av Azure AD access_token"))
    @SuppressWarnings("unused")
    public Response accessToken(@Context HttpServletRequest req,
                                @FormParam("fnr") String fnr,
                                @FormParam("redirect") URI redirectUri) {

        var token = createIdToken(req, fnr,"loginservice");
        var cookieTemplate = "selvbetjening-idtoken=%s;Path=/";
        return Response.seeOther(redirectUri)
                .header("Set-Cookie", String.format(cookieTemplate, token, redirectUri.getHost()))
                .build();
    }
}
