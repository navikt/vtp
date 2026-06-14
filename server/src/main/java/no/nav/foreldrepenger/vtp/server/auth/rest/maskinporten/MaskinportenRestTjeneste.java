package no.nav.foreldrepenger.vtp.server.auth.rest.maskinporten;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.server.auth.rest.Issuers;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;

@Path(MaskinportenRestTjeneste.TJENESTE_PATH)
public class MaskinportenRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(MaskinportenRestTjeneste.class);

    protected static final String TJENESTE_PATH = "/maskinporten"; //NOSONAR

    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(@FormParam("grant_type") String grantType,
                          @FormParam("assertion") String assertion) {
        LOG.info("Maskinporten mock: Mottar token-forespørsel med grant_type={}", grantType);
        String token = MaskinportenOidcTokenGenerator.maskinportenToken(
                "digdir:dialogporten.serviceprovider", Issuers.MASKINPORTEN.getIssuer(), null, null);
        return Response.ok(new Oauth2AccessTokenResponse(null, null, token, 600, "Bearer")).build();
    }
}
