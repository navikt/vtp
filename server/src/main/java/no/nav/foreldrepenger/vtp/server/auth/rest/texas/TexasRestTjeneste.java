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
import no.nav.foreldrepenger.vtp.server.auth.rest.Issuers;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.azuread.AzureAdRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.azuread.AzureOidcTokenGenerator;
import no.nav.foreldrepenger.vtp.server.auth.rest.maskinporten.MaskinportenOidcTokenGenerator;
import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenxRestTjeneste;

import org.apache.commons.lang3.NotImplementedException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static java.util.UUID.*;

@Tag(name = "Texas")
@Path(TexasRestTjeneste.TJENESTE_PATH)
public class TexasRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TexasRestTjeneste.class);

    protected static final String TJENESTE_PATH = "/texas"; //NOSONAR

    @POST
    @Path("/api/v1/token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Texas mock for acquiring access token for machine-to-machine use")
    public Response token(TexasTokenRequest tokenRequest) {
        if (Boolean.TRUE.equals(tokenRequest.skip_cache())) {
            LOG.debug("skip_cache is accepted but ignored by Texas mock /api/v1/token");
        }

        Objects.requireNonNull(tokenRequest.identity_provider(), "provider must be provided");

        return switch (tokenRequest.identity_provider()) {
            case Issuers.ENTRA_ID -> {
                String token = AzureOidcTokenGenerator.azureClientCredentialsToken(
                        randomUUID().toString(), Issuers.ENTRA_ID.getIssuer()
                );
                yield Response.ok(new Oauth2AccessTokenResponse(null, null, token, 3600, "Bearer")).build();
            }
            case Issuers.MASKINPORTEN -> {
                String token = MaskinportenOidcTokenGenerator.maskinportenToken(tokenRequest.target(), Issuers.MASKINPORTEN.getIssuer(),
                        tokenRequest.resource(), tokenRequest.authorization_details());
                yield Response.ok(new Oauth2AccessTokenResponse(null, null, token, 600, "Bearer")).build();
            }
            case Issuers.IDPORTEN, Issuers.TOKENX -> throw new NotImplementedException();
        };
    }

    @POST
    @Path("/api/v1/token/exchange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Texas mock for token exchange")
    public Response tokenExchange(@Context HttpServletRequest req,
                                  TexasExchangeRequest tokenRequest) throws JoseException {
        return switch (tokenRequest.identity_provider()) {
            case Issuers.ENTRA_ID: {
                yield AzureAdRestTjeneste.processTokenRequest("urn:ietf:params:oauth:grant-type:jwt-bearer", null, tokenRequest.user_token(), null);
            }
            case Issuers.TOKENX: {
                yield TokenxRestTjeneste.exchangeTokenX(req, tokenRequest.user_token(), tokenRequest.target());
            }
            case Issuers.IDPORTEN, Issuers.MASKINPORTEN: throw new NotImplementedException();
        };
    }

    /**
     * @deprecated Use /api/v1/token/exchange instead. This method is retained for backward compatibility and will be removed in a future release.
     * @param req
     * @param tokenRequest
     * @return
     * @throws JoseException
     */
    @Deprecated(since = "05.03.2026", forRemoval = true)
    @POST
    @Path("/token/exchange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Texas mock for token exchange")
    public Response hentToken(@Context HttpServletRequest req,
                              TexasExchangeRequest tokenRequest) throws JoseException {
        return tokenExchange(req, tokenRequest);
    }
}
