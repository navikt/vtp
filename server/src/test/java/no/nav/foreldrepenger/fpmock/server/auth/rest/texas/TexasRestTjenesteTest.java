package no.nav.foreldrepenger.fpmock.server.auth.rest.texas;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.server.auth.rest.Issuers;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.texas.AuthorizationDetails;
import no.nav.foreldrepenger.vtp.server.auth.rest.texas.TexasIntrospectRequest;
import no.nav.foreldrepenger.vtp.server.auth.rest.texas.TexasRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.texas.TexasTokenRequest;

@Disabled("Requires keystore to be configured for signature verification - not suitable for CI environment")
class TexasRestTjenesteTest {

    private static final JwtConsumer UNVALIDATING_CONSUMER = new JwtConsumerBuilder().setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    private TexasRestTjeneste tjeneste;

    @BeforeEach
    void setup() {
        tjeneste = new TexasRestTjeneste();
    }

    @Test
    void shouldGenerateAzureAdTokenResponse() {
        var request = new TexasTokenRequest(Issuers.ENTRA_ID, null, null, null, false);

        Response response = tjeneste.token(request);

        assertThat(response.getStatus()).isEqualTo(200);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        assertThat(tokenResponse.accessToken()).isNotNull();
        assertThat(tokenResponse.expiresIn()).isEqualTo(3600);
        assertThat(tokenResponse.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void shouldGenerateMaskinportenTokenResponse() {
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, null, false);

        Response response = tjeneste.token(request);

        assertThat(response.getStatus()).isEqualTo(200);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        assertThat(tokenResponse.accessToken()).isNotNull();
        assertThat(tokenResponse.expiresIn()).isEqualTo(600);
        assertThat(tokenResponse.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void shouldAcceptResourceParameter() {
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", "https://example.com/api", null, false);

        Response response = tjeneste.token(request);

        assertThat(response.getStatus()).isEqualTo(200);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        assertThat(tokenResponse.accessToken()).isNotNull();
    }

    @Test
    void shouldAcceptAuthorizationDetails() {
        var authDetails = List.of(new AuthorizationDetails("urn:altinn:resource",
                new AuthorizationDetails.Consumer("iso6523-actorid-upis", "0192:111111111"), List.of("urn:altinn:systemuser:12345"),
                "ske_kravogbetalinger"));
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, authDetails, false);

        Response response = tjeneste.token(request);

        assertThat(response.getStatus()).isEqualTo(200);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        assertThat(tokenResponse.accessToken()).isNotNull();
    }

    @Test
    void shouldAcceptBothResourceAndAuthorizationDetails() {
        var authDetails = List.of(new AuthorizationDetails("urn:altinn:resource",
                new AuthorizationDetails.Consumer("iso6523-actorid-upis", "0192:111111111"), List.of("urn:altinn:systemuser:12345"),
                "ske_kravogbetalinger"));
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", "https://example.com/api", authDetails, false);

        Response response = tjeneste.token(request);

        assertThat(response.getStatus()).isEqualTo(200);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        assertThat(tokenResponse.accessToken()).isNotNull();
    }

    @Test
    void shouldIgnoreSkipCacheParameter() {
        var request1 = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, null, false);
        var request2 = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, null, true);

        Response response1 = tjeneste.token(request1);
        Response response2 = tjeneste.token(request2);

        // Both should succeed regardless of skip_cache value
        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response2.getStatus()).isEqualTo(200);

        var tokenResponse1 = (Oauth2AccessTokenResponse) response1.getEntity();
        var tokenResponse2 = (Oauth2AccessTokenResponse) response2.getEntity();
        assertThat(tokenResponse1.accessToken()).isNotNull();
        assertThat(tokenResponse2.accessToken()).isNotNull();
    }

    @Test
    void shouldGenerateMaskinportenTokenWithNullTarget() {
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, null, null, null, false);

        Response response = tjeneste.token(request);

        assertThat(response.getStatus()).isEqualTo(200);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        assertThat(tokenResponse.accessToken()).isNotNull();
    }

    // Tests below verify JWT claims - requires keystore to be configured

    @Test
    void shouldVerifyAzureAdTokenClaims() throws Exception {
        var request = new TexasTokenRequest(Issuers.ENTRA_ID, null, null, null, false);

        Response response = tjeneste.token(request);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();

        JwtClaims claims = parseToken(tokenResponse.accessToken());
        assertThat(claims.getIssuer()).contains(Issuers.ENTRA_ID.getIssuer());
        assertThat(claims.getAudience()).containsExactly("vtp");
        assertThat(claims.getSubject()).isNotNull();
        assertThat(claims.getClaimValue("oid")).isEqualTo(claims.getSubject());
        assertThat(claims.getClaimValue("idtyp")).isEqualTo("app");
        assertThat(claims.getStringListClaimValue("roles")).containsExactly("access_as_application");
    }

    @Test
    void shouldVerifyMaskinportenTokenClaims() throws Exception {
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, null, false);

        Response response = tjeneste.token(request);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();

        JwtClaims claims = parseToken(tokenResponse.accessToken());
        assertThat(claims.getIssuer()).contains(Issuers.MASKINPORTEN.getIssuer());
        assertThat(claims.getAudience()).containsExactly("vtp");
        assertThat(claims.getClaimValue("scope")).isEqualTo("nav:some/scope");
        assertThat(claims.getClaimValue("client_id")).isEqualTo("vtp-maskinporten-client");
        assertThat(claims.getClaimValue("consumer")).isNotNull();
    }

    @Test
    void shouldVerifyResourceClaimInToken() {
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", "https://example.com/api", null, false);

        Response response = tjeneste.token(request);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();

        JwtClaims claims = parseToken(tokenResponse.accessToken());
        assertThat(claims.getClaimValue("resource")).isEqualTo("https://example.com/api");
    }

    @Test
    void shouldVerifyAuthorizationDetailsClaimInToken() {
        var authDetails = List.of(new AuthorizationDetails("urn:altinn:resource",
                new AuthorizationDetails.Consumer("iso6523-actorid-upis", "0192:111111111"), List.of("urn:altinn:systemuser:12345"),
                "ske_kravogbetalinger"));
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, authDetails, false);

        Response response = tjeneste.token(request);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();

        JwtClaims claims = parseToken(tokenResponse.accessToken());

        // authorization_details is a proper JSON array — assert directly on the map entries
        @SuppressWarnings("unchecked") var rawList = (List<Map<String, Object>>) claims.getClaimValue("authorization_details");
        assertThat(rawList).hasSize(1);

        var entry = rawList.getFirst();
        assertThat(entry).containsEntry("type", "urn:altinn:resource")
                .containsEntry("system_id", "ske_kravogbetalinger")
                .containsEntry("systemuser_id", List.of("urn:altinn:systemuser:12345"));

        @SuppressWarnings("unchecked") var org = (Map<String, Object>) entry.get("systemuser_org");
        assertThat(org).containsEntry("authority", "iso6523-actorid-upis").containsEntry("ID", "0192:111111111");
    }

    @Test
    void shouldVerifyTokenExpiration() throws Exception {
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, null, false);

        Response response = tjeneste.token(request);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();

        JwtClaims claims = parseToken(tokenResponse.accessToken());
        assertThat(claims.getExpirationTime()).isNotNull();
        assertThat(claims.getIssuedAt()).isNotNull();

        // Verify expiration is 3599 seconds from issued time
        long expectedExpiration = claims.getIssuedAt().getValue() + 3599;
        assertThat(claims.getExpirationTime().getValue()).isEqualTo(expectedExpiration);
    }

    @Test
    void shouldGenerateUniqueJtiForEachToken() throws Exception {
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, null, false);

        Response response1 = tjeneste.token(request);
        Response response2 = tjeneste.token(request);

        var tokenResponse1 = (Oauth2AccessTokenResponse) response1.getEntity();
        var tokenResponse2 = (Oauth2AccessTokenResponse) response2.getEntity();

        JwtClaims claims1 = parseToken(tokenResponse1.accessToken());
        JwtClaims claims2 = parseToken(tokenResponse2.accessToken());

        assertThat(claims1.getJwtId()).isNotNull();
        assertThat(claims2.getJwtId()).isNotNull();
        assertThat(claims1.getJwtId()).isNotEqualTo(claims2.getJwtId());
    }

    // --- Introspect endpoint tests ---

    @Test
    void introspectShouldReturnActiveForValidEntraIdToken() {
        // First generate a valid token
        var tokenRequest = new TexasTokenRequest(Issuers.ENTRA_ID, null, null, null, false);
        var tokenResponse = (Oauth2AccessTokenResponse) tjeneste.token(tokenRequest).getEntity();

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, tokenResponse.accessToken());
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", true)
                .doesNotContainKey("error")
                .containsEntry("iss", Issuers.ENTRA_ID.getIssuer())
                .containsKeys("sub", "exp", "iat");
    }

    @Test
    void introspectShouldReturnActiveForValidMaskinportenToken() {
        var tokenRequest = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, null, false);
        var tokenResponse = (Oauth2AccessTokenResponse) tjeneste.token(tokenRequest).getEntity();

        var introspectRequest = new TexasIntrospectRequest(Issuers.MASKINPORTEN, tokenResponse.accessToken());
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", true).containsEntry("iss", Issuers.MASKINPORTEN.getIssuer());
    }

    @Test
    void introspectShouldReturnInactiveForMalformedToken() {
        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, "not-a-valid-jwt");
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", false).containsEntry("error", "invalid token: malformed JWT");
    }

    @Test
    void introspectShouldReturnInactiveForIssuerMismatch() {
        // Generate a token with ENTRA_ID issuer but introspect with MASKINPORTEN
        var tokenRequest = new TexasTokenRequest(Issuers.ENTRA_ID, null, null, null, false);
        var tokenResponse = (Oauth2AccessTokenResponse) tjeneste.token(tokenRequest).getEntity();

        var introspectRequest = new TexasIntrospectRequest(Issuers.MASKINPORTEN, tokenResponse.accessToken());
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", false).containsEntry("error", "invalid token: issuer mismatch");
    }

    @Test
    void introspectShouldReturnInactiveForExpiredToken() {
        // Create a token that is already expired
        var claims = new JwtClaims();
        claims.setIssuer(Issuers.ENTRA_ID.getIssuer());
        claims.setAudience("vtp");
        claims.setSubject("test-sub");
        claims.setIssuedAt(NumericDate.fromSeconds(NumericDate.now().getValue() - 7200));
        claims.setExpirationTime(NumericDate.fromSeconds(NumericDate.now().getValue() - 3600));
        claims.setGeneratedJwtId();
        String expiredToken = createUnsignedToken(claims);

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, expiredToken);
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", false).containsEntry("error", "invalid token: ExpiredSignature");
    }

    @Test
    void introspectShouldReturnInactiveForMissingIssuer() {
        var claims = new JwtClaims();
        claims.setAudience("vtp");
        claims.setSubject("test-sub");
        claims.setIssuedAtToNow();
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setGeneratedJwtId();
        String token = createUnsignedToken(claims);

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, token);
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", false).containsEntry("error", "invalid token: missing iss claim");
    }

    @Test
    void introspectShouldReturnInactiveForMissingIat() {
        var claims = new JwtClaims();
        claims.setIssuer(Issuers.ENTRA_ID.getIssuer());
        claims.setAudience("vtp");
        claims.setSubject("test-sub");
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setGeneratedJwtId();
        String token = createUnsignedToken(claims);

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, token);
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", false).containsEntry("error", "invalid token: missing iat claim");
    }

    @Test
    void introspectShouldReturnInactiveForMissingExp() {
        var claims = new JwtClaims();
        claims.setIssuer(Issuers.ENTRA_ID.getIssuer());
        claims.setAudience("vtp");
        claims.setSubject("test-sub");
        claims.setIssuedAtToNow();
        claims.setGeneratedJwtId();
        String token = createUnsignedToken(claims);

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, token);
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", false).containsEntry("error", "invalid token: missing exp claim");
    }

    @Test
    void introspectShouldReturnInactiveForMissingAudOnNonMaskinporten() {
        var claims = new JwtClaims();
        claims.setIssuer(Issuers.ENTRA_ID.getIssuer());
        claims.setSubject("test-sub");
        claims.setIssuedAtToNow();
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setGeneratedJwtId();
        String token = createUnsignedToken(claims);

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, token);
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", false).containsEntry("error", "invalid token: missing aud claim");
    }

    @Test
    void introspectShouldNotRequireAudForMaskinporten() {
        // Maskinporten tokens do not require aud claim
        var claims = new JwtClaims();
        claims.setIssuer(Issuers.MASKINPORTEN.getIssuer());
        claims.setSubject("test-sub");
        claims.setIssuedAtToNow();
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setGeneratedJwtId();
        String token = createUnsignedToken(claims);

        var introspectRequest = new TexasIntrospectRequest(Issuers.MASKINPORTEN, token);
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", true);
    }

    @Test
    void introspectShouldReturnInactiveForFutureNbf() {
        var claims = new JwtClaims();
        claims.setIssuer(Issuers.ENTRA_ID.getIssuer());
        claims.setAudience("vtp");
        claims.setSubject("test-sub");
        claims.setIssuedAtToNow();
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setNotBefore(NumericDate.fromSeconds(NumericDate.now().getValue() + 3600));
        claims.setGeneratedJwtId();
        String token = createUnsignedToken(claims);

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, token);
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();

        assertThat(introspectResponse).containsEntry("active", false)
                .containsEntry("error", "invalid token: token not yet valid (nbf)");
    }

    @Test
    void introspectShouldReturnAllStandardClaims() {
        var tokenRequest = new TexasTokenRequest(Issuers.ENTRA_ID, null, null, null, false);
        var tokenResponse = (Oauth2AccessTokenResponse) tjeneste.token(tokenRequest).getEntity();

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, tokenResponse.accessToken());
        Response response = tjeneste.introspect(introspectRequest);

        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();

        assertThat(introspectResponse).containsEntry("active", true)
                .containsKey("iss")
                .containsKey("sub")
                .containsKey("aud")
                .containsKey("exp")
                .containsKey("iat")
                .containsKey("jti");

        // Verify iat is in the past and exp is in the future
        assertThat(((Number) introspectResponse.get("iat")).longValue()).isLessThanOrEqualTo(NumericDate.now().getValue());
        assertThat(((Number) introspectResponse.get("exp")).longValue()).isGreaterThan(NumericDate.now().getValue());
    }

    @Test
    void introspectShouldReturnAllOriginalTokenClaims() {
        // Verify that non-standard claims (like oid, idtyp, roles) are also returned
        var tokenRequest = new TexasTokenRequest(Issuers.ENTRA_ID, null, null, null, false);
        var tokenResponse = (Oauth2AccessTokenResponse) tjeneste.token(tokenRequest).getEntity();

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, tokenResponse.accessToken());
        Response response = tjeneste.introspect(introspectRequest);

        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();

        assertThat(introspectResponse).containsEntry("active", true)
                .containsEntry("idtyp", "app")
                .containsKey("oid")
                .containsKey("roles");
    }

    @Test
    void introspectShouldReturnMaskinportenSpecificClaims() {
        var tokenRequest = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, null, false);
        var tokenResponse = (Oauth2AccessTokenResponse) tjeneste.token(tokenRequest).getEntity();

        var introspectRequest = new TexasIntrospectRequest(Issuers.MASKINPORTEN, tokenResponse.accessToken());
        Response response = tjeneste.introspect(introspectRequest);

        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", true)
                .containsEntry("scope", "nav:some/scope")
                .containsEntry("client_id", "vtp-maskinporten-client")
                .containsKey("consumer");
    }

    @Test
    void introspectShouldReturnInactiveForIatInFuture() {
        var claims = new JwtClaims();
        claims.setIssuer(Issuers.ENTRA_ID.getIssuer());
        claims.setAudience("vtp");
        claims.setSubject("test-sub");
        claims.setIssuedAt(NumericDate.fromSeconds(NumericDate.now().getValue() + 7200));
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setGeneratedJwtId();
        String token = createUnsignedToken(claims);

        var introspectRequest = new TexasIntrospectRequest(Issuers.ENTRA_ID, token);
        Response response = tjeneste.introspect(introspectRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        @SuppressWarnings("unchecked") var introspectResponse = (Map<String, Object>) response.getEntity();
        assertThat(introspectResponse).containsEntry("active", false).containsEntry("error", "invalid token: iat is in the future");
    }

    /**
     * Creates an unsigned JWT token (using "none" algorithm) for testing purposes.
     */
    private String createUnsignedToken(JwtClaims claims) {
        // Format: base64url(header).base64url(payload).
        String header = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"none\"}".getBytes());
        String payload = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(claims.toJson().getBytes());
        return header + "." + payload + ".";
    }

    private JwtClaims parseToken(String token) {
        try {
            return UNVALIDATING_CONSUMER.processToClaims(token);
        } catch (InvalidJwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}

