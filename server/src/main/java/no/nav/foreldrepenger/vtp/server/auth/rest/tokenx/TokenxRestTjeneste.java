package no.nav.foreldrepenger.vtp.server.auth.rest.tokenx;

import static no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenExchangeResponse.EXPIRE_IN_SECONDS;
import static org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA256;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.server.MockServer;
import no.nav.foreldrepenger.vtp.server.auth.rest.JsonWebKeyHelper;

@Tag(name = "TokenX")
@Path(TokenxRestTjeneste.TJENESTE_PATH)
public class TokenxRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TokenxRestTjeneste.class);

    protected static final String TJENESTE_PATH = "/tokenx"; //NOSONAR

    public static final JwtConsumer UNVALIDATING_CONSUMER = new JwtConsumerBuilder().setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "TokenX mock OK";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/.well-known/oauth-authorization-server")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "TokenX Discovery url")
    public Response wellKnown(@Context HttpServletRequest req) {
        LOG.info("Kall på well-known endepunkt");
        var issuer = getIssuer(req);
        var tokenEndpoint = issuer + "/token";
        var jwksEndpoint = issuer + "/jwks";
        var wellKnownResponse = new TokenXWellKnownResponse(issuer, tokenEndpoint, jwksEndpoint);
        return Response.ok(wellKnownResponse).build();
    }

    @GET
    @Path("/jwks")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "TokenX public key set")
    public Response jwks(@Context HttpServletRequest req) {
        LOG.info("Kall på /tokenx/jwks");
        var jwks = JsonWebKeyHelper.getJwks();
        LOG.trace("Jwks er {}", jwks);
        return Response.ok(jwks).build();
    }

    @POST
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "TokenX public key set")
    public Response token(@Context HttpServletRequest req,
                          @FormParam("grant_type") @DefaultValue("urn:ietf:params:oauth:grant-type:token-exchange") String grantType,
                          @FormParam("client_assertion_type") @DefaultValue("urn:ietf:params:oauth:grant-type:token-exchange") String clientAssertionType,
                          @FormParam("client_assertion") String clientAssertion,
                          @FormParam("subject_token_type") @DefaultValue("urn:ietf:params:oauth:token-type:jwt") String subjectTokenType,
                          @FormParam("subject_token") String subjectToken,
                          @FormParam("audience") String audience) throws JoseException {
        var subject = hentSubjectFraJWT(subjectToken);
        var token = accessTokenForAudienceOgSubject(req, audience, subject);
        LOG.info("Henter token for subject [{}] som kan brukes til å kalle audience [{}]", subject, audience);
        LOG.info("TokenX token: {}", token);
        return Response.ok(new TokenExchangeResponse(token)).build();
    }


    private String accessTokenForAudienceOgSubject(HttpServletRequest req, String audience, String subject) throws JoseException {
        var jwtClaims = new JwtClaims();
        jwtClaims.setIssuer(getIssuer(req));
        jwtClaims.setAudience(audience);
        jwtClaims.setSubject(subject);
        jwtClaims.setClaim("pid", subject);
        jwtClaims.setExpirationTimeMinutesInTheFuture(EXPIRE_IN_SECONDS / 60f);
        jwtClaims.setGeneratedJwtId();
        jwtClaims.setIssuedAtToNow();
        jwtClaims.setClaim("acr", System.getProperty("idporten.acr.scope", "idporten-loa-high"));
        jwtClaims.setNotBeforeMinutesInThePast(0F);

        var rsaJWK = JsonWebKeyHelper.getJsonWebKey();
        var jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKeyIdHeaderValue(rsaJWK.getKeyId());
        jws.setAlgorithmHeaderValue("RS256");
        jws.setKey(rsaJWK.getPrivateKey());
        jws.setAlgorithmHeaderValue(RSA_USING_SHA256);
        return jws.getCompactSerialization();
    }

    private String hentSubjectFraJWT(String subjectToken) {
        try {
            var claims = UNVALIDATING_CONSUMER.processToClaims(subjectToken);
            return claims.getSubject();
        } catch (InvalidJwtException | MalformedClaimException e) {
            throw new IllegalStateException("Subjekt_token er ikke av typen JWT og vi kan derfor ikke hente ut sub i claims", e);
        }
    }

    private String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort();
    }

    private String getIssuer(HttpServletRequest req) {
        return getBaseUrl(req) + MockServer.CONTEXT_PATH + TJENESTE_PATH;
    }

}
