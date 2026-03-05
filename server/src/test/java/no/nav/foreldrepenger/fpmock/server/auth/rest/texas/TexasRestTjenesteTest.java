package no.nav.foreldrepenger.fpmock.server.auth.rest.texas;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import no.nav.foreldrepenger.vtp.server.auth.rest.Issuers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.texas.AuthorizationDetails;
import no.nav.foreldrepenger.vtp.server.auth.rest.texas.TexasRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.texas.TexasTokenRequest;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

class TexasRestTjenesteTest {

    private static final JwtConsumer UNVALIDATING_CONSUMER = new JwtConsumerBuilder()
            .setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        var authDetails = List.of(
                new AuthorizationDetails(
                        "urn:altinn:resource",
                        new AuthorizationDetails.Consumer("iso6523-actorid-upis", "0192:111111111"),
                        List.of("urn:altinn:systemuser:12345"),
                        "ske_kravogbetalinger"
                )
        );
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, authDetails, false);

        Response response = tjeneste.token(request);

        assertThat(response.getStatus()).isEqualTo(200);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        assertThat(tokenResponse.accessToken()).isNotNull();
    }

    @Test
    void shouldAcceptBothResourceAndAuthorizationDetails() {
        var authDetails = List.of(
                new AuthorizationDetails(
                        "urn:altinn:resource",
                        new AuthorizationDetails.Consumer("iso6523-actorid-upis", "0192:111111111"),
                        List.of("urn:altinn:systemuser:12345"),
                        "ske_kravogbetalinger"
                )
        );
        var request = new TexasTokenRequest(
                Issuers.MASKINPORTEN,
                "nav:some/scope",
                "https://example.com/api",
                authDetails,
                false
        );

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
    void shouldVerifyResourceClaimInToken() throws Exception {
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", "https://example.com/api", null, false);

        Response response = tjeneste.token(request);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();

        JwtClaims claims = parseToken(tokenResponse.accessToken());
        assertThat(claims.getClaimValue("resource")).isEqualTo("https://example.com/api");
    }

    @Test
    void shouldVerifyAuthorizationDetailsClaimInToken() throws Exception {
        var authDetails = List.of(
                new AuthorizationDetails(
                        "urn:altinn:resource",
                        new AuthorizationDetails.Consumer("iso6523-actorid-upis", "0192:111111111"),
                        List.of("urn:altinn:systemuser:12345"),
                        "ske_kravogbetalinger"
                )
        );
        var request = new TexasTokenRequest(Issuers.MASKINPORTEN, "nav:some/scope", null, authDetails, false);

        Response response = tjeneste.token(request);
        var tokenResponse = (Oauth2AccessTokenResponse) response.getEntity();

        JwtClaims claims = parseToken(tokenResponse.accessToken());

        // Verify authorization_details is serialized as JSON
        String authDetailsJson = (String) claims.getClaimValue("authorization_details");
        assertThat(authDetailsJson).isNotNull();

        // Parse back to verify structure
        List<AuthorizationDetails> parsedDetails = OBJECT_MAPPER.readValue(
                authDetailsJson,
                new TypeReference<>() {}
        );
        assertThat(parsedDetails).hasSize(1);
        assertThat(parsedDetails.getFirst().type()).isEqualTo("urn:altinn:resource");
        assertThat(parsedDetails.getFirst().system_id()).isEqualTo("ske_kravogbetalinger");
        assertThat(parsedDetails.getFirst().systemuser_id()).containsExactly("urn:altinn:systemuser:12345");
        assertThat(parsedDetails.getFirst().systemuser_org().authority()).isEqualTo("iso6523-actorid-upis");
        assertThat(parsedDetails.getFirst().systemuser_org().id()).isEqualTo("0192:111111111");
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

    private JwtClaims parseToken(String token) {
        try {
            return UNVALIDATING_CONSUMER.processToClaims(token);
        } catch (InvalidJwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}

