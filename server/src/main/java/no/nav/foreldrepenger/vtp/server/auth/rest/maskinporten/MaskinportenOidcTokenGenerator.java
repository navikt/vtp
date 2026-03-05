package no.nav.foreldrepenger.vtp.server.auth.rest.maskinporten;

import no.nav.foreldrepenger.vtp.server.auth.rest.texas.AuthorizationDetails;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.vtp.server.auth.rest.JsonWebKeyHelper;

import java.util.List;
import java.util.Optional;

public final class MaskinportenOidcTokenGenerator {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private MaskinportenOidcTokenGenerator() {
    }

    public static String maskinportenToken(String scope, String issuer, String resource, List<AuthorizationDetails> authorizationDetails) {
        JwtClaims claims = createMaskinportenClaims(scope, issuer, resource, authorizationDetails);
        return createToken(claims);
    }

    private static JwtClaims createMaskinportenClaims(String scope, String issuer, String resource, List<AuthorizationDetails> authorizationDetails) {
        var issuedAt = NumericDate.now();
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setExpirationTime(NumericDate.fromSeconds(issuedAt.getValue() + 3599)); // 3599 seconds
        claims.setGeneratedJwtId();
        claims.setIssuedAt(issuedAt);
        claims.setNotBefore(issuedAt);
        claims.setAudience("vtp");
        claims.setStringClaim("scope", scope);
        claims.setStringClaim("client_id", "vtp-maskinporten-client");
        claims.setStringClaim("consumer", "{\"authority\":\"iso6523-actorid-upis\",\"ID\":\"0192:999999999\"}");
        Optional.ofNullable(resource).ifPresent(r -> claims.setClaim("resource", r));
        Optional.ofNullable(authorizationDetails).ifPresent(authDetails -> {
            try {
                String authDetailsJson = OBJECT_MAPPER.writeValueAsString(authDetails);
                claims.setStringClaim("authorization_details", authDetailsJson);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Failed to serialize authorization_details to JSON", e);
            }
        });
        return claims;
    }

    private static String createToken(JwtClaims claims) {
        RsaJsonWebKey senderJwk = JsonWebKeyHelper.getJsonWebKey();
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKeyIdHeaderValue(JsonWebKeyHelper.getJsonWebKey().getKeyId());
        jws.setAlgorithmHeaderValue("RS256");
        jws.setKey(senderJwk.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new IllegalStateException(e);
        }
    }
}

