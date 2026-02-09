package no.nav.foreldrepenger.vtp.server.auth.rest.texas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.server.auth.rest.azuread.AzureAdRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenExchangeRequest;

import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenxRestTjeneste;

import org.apache.commons.lang3.NotImplementedException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Texas")
@Path(TexasRestTjeneste.TJENESTE_PATH)
public class TexasRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TexasRestTjeneste.class);

    protected static final String TJENESTE_PATH = "/texas"; //NOSONAR

    @POST
    @Path("/token/exchange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Texas mock for token exchange")
    public Response tokenJson(@Context HttpServletRequest req,
                              TokenExchangeRequest tokenRequest) throws JoseException {

        return switch (tokenRequest.identity_provider()) {
            case "azuread": {
                yield AzureAdRestTjeneste.processTokenRequest("urn:ietf:params:oauth:grant-type:jwt-bearer", null, tokenRequest.user_token(), null);
            }
            case "tokenx": {
                yield TokenxRestTjeneste.exchangeTokenX(req, tokenRequest.user_token(), tokenRequest.target());
            }
            case "maskinporten", "idporten": throw new NotImplementedException();
            default: {
                LOG.error("Unsupported token request type: {}", tokenRequest.identity_provider());
                yield Response.status(400).build();
            }
        };
    }
}
