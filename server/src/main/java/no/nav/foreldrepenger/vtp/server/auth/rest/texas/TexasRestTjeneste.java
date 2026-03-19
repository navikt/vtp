package no.nav.foreldrepenger.vtp.server.auth.rest.texas;

import static java.util.UUID.randomUUID;

import java.util.Objects;

import no.nav.foreldrepenger.vtp.server.auth.rest.idporten.IdportenOidcTokenGenerator;

import org.apache.commons.lang3.NotImplementedException;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.server.auth.rest.Issuers;
import no.nav.foreldrepenger.vtp.server.auth.rest.JwtUtil;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.azuread.AzureAdRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.azuread.AzureOidcTokenGenerator;
import no.nav.foreldrepenger.vtp.server.auth.rest.maskinporten.MaskinportenOidcTokenGenerator;
import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenxRestTjeneste;

@Path(TexasRestTjeneste.TJENESTE_PATH)
public class TexasRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TexasRestTjeneste.class);

    private static final String APP = "app";
    private static final String IDTYP = "idtyp";

    protected static final String TJENESTE_PATH = "/texas"; //NOSONAR

    @POST
    @Path("/api/v1/token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response token(TexasTokenRequest tokenRequest) {
        Objects.requireNonNull(tokenRequest.identity_provider(), "provider must be provided");
        if (Boolean.TRUE.equals(tokenRequest.skip_cache())) {
            LOG.debug("skip_cache is accepted but ignored by Texas mock /api/v1/token");
        }

        return switch (tokenRequest.identity_provider()) {
            case Issuers.ENTRA_ID, Issuers.AZUREAD -> {
                var token = AzureOidcTokenGenerator.azureClientCredentialsToken(
                        randomUUID().toString(), Issuers.ENTRA_ID.getIssuer()
                );
                yield Response.ok(new Oauth2AccessTokenResponse(null, null, token, 3600, "Bearer")).build();
            }
            case Issuers.MASKINPORTEN -> {
                String token = MaskinportenOidcTokenGenerator.maskinportenToken(tokenRequest.target(), Issuers.MASKINPORTEN.getIssuer(),
                        tokenRequest.resource(), tokenRequest.authorization_details());
                yield Response.ok(new Oauth2AccessTokenResponse(null, null, token, 600, "Bearer")).build();
            }
            case Issuers.IDPORTEN -> Response.ok(IdportenOidcTokenGenerator.idportenUserToken(randomUUID().toString(), Issuers.IDPORTEN.getIssuer(), null)).build();
            case Issuers.TOKENX -> throw new NotImplementedException();
        };
    }

    @POST
    @Path("/api/v1/token/exchange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response tokenExchange(@Context HttpServletRequest req,
                                  TexasExchangeRequest tokenRequest) throws JoseException {
        return switch (tokenRequest.identity_provider()) {
            case Issuers.ENTRA_ID, Issuers.AZUREAD: {
                var claims = JwtUtil.getClaims(tokenRequest.user_token());
                if (isAzureClientCredentials(claims)) {
                    LOG.debug("Token exchange for Azure client credentials token");
                    yield AzureAdRestTjeneste.processTokenRequest("client_credentials", null, tokenRequest.user_token(), null);
                }
                yield AzureAdRestTjeneste.processTokenRequest("urn:ietf:params:oauth:grant-type:jwt-bearer", null, tokenRequest.user_token(), null);
            }
            case Issuers.TOKENX: {
                yield TokenxRestTjeneste.exchangeTokenX(req, tokenRequest.user_token(), tokenRequest.target());
            }
            case Issuers.IDPORTEN, Issuers.MASKINPORTEN: throw new NotImplementedException();
        };
    }

    private boolean isAzureClientCredentials(JwtClaims claims) {
        return Objects.equals(APP, JwtUtil.getStringClaim(claims, IDTYP));
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
    public Response hentToken(@Context HttpServletRequest req,
                              TexasExchangeRequest tokenRequest) throws JoseException {
        return tokenExchange(req, tokenRequest);
    }

    @POST
    @Path("/api/v1/introspect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response introspect(TexasIntrospectRequest request) {
        Objects.requireNonNull(request.identity_provider(), "identity_provider must be provided");
        Objects.requireNonNull(request.token(), "token must be provided");

        JwtClaims claims;
        try {
            claims = JwtUtil.getClaims(request.token());
        } catch (Exception e) {
            LOG.debug("Failed to parse JWT token", e);
            return Response.ok(TexasIntrospectResponse.inactive("invalid token: malformed JWT")).build();
        }

        try {
            return validateAndBuildResponse(claims, request.identity_provider());
        } catch (MalformedClaimException e) {
            LOG.debug("Malformed claim in JWT token", e);
            return Response.ok(TexasIntrospectResponse.inactive("invalid token: malformed claims")).build();
        }
    }

    private Response validateAndBuildResponse(JwtClaims claims, Issuers identityProvider) throws MalformedClaimException {
        // Validate iss
        var issuer = claims.getIssuer();
        if (issuer == null || issuer.isBlank()) {
            return Response.ok(TexasIntrospectResponse.inactive("invalid token: missing iss claim")).build();
        }
        if (!issuer.contains(identityProvider.getIssuer())) {
            return Response.ok(TexasIntrospectResponse.inactive("invalid token: issuer mismatch")).build();
        }

        // Validate iat - must exist and be in the past
        var iat = claims.getIssuedAt();
        if (iat == null) {
            return Response.ok(TexasIntrospectResponse.inactive("invalid token: missing iat claim")).build();
        }
        if (iat.isAfter(NumericDate.now())) {
            return Response.ok(TexasIntrospectResponse.inactive("invalid token: iat is in the future")).build();
        }

        // Validate exp - must exist and be in the future
        var exp = claims.getExpirationTime();
        if (exp == null) {
            return Response.ok(TexasIntrospectResponse.inactive("invalid token: missing exp claim")).build();
        }
        if (exp.isBefore(NumericDate.now())) {
            return Response.ok(TexasIntrospectResponse.inactive("invalid token: ExpiredSignature")).build();
        }

        // Validate aud - must be present for all providers except Maskinporten
        if (identityProvider != Issuers.MASKINPORTEN) {
            var audience = claims.getAudience();
            if (audience == null || audience.isEmpty()) {
                return Response.ok(TexasIntrospectResponse.inactive("invalid token: missing aud claim")).build();
            }
        }

        // Validate nbf - if present, must be in the past
        var nbf = claims.getNotBefore();
        if (nbf != null && nbf.isAfter(NumericDate.now())) {
            return Response.ok(TexasIntrospectResponse.inactive("invalid token: token not yet valid (nbf)")).build();
        }

        // Token is valid, build the response with all original claims
        return Response.ok(TexasIntrospectResponse.active(claims)).build();
    }
}
