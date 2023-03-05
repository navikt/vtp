package no.nav.foreldrepenger.vtp.server.auth.rest.foraad;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;


public final class AzureOidcTokenGenerator {
    private AzureOidcTokenGenerator() {
    }

    public static String azureUserToken(List<String> aud, String sub, String issuer, Map<String, String> additionalClaims, String nonce, String sid) {

        var issuedAt = NumericDate.now();
        JwtClaims claims = createCommonClaims(sub, issuer, issuedAt);
        additionalClaims.forEach(claims::setStringClaim);
        Optional.ofNullable(nonce).filter(n -> !n.isEmpty()).ifPresent(n -> claims.setClaim("nonce", n));
        claims.setStringClaim("ver", "2.0");

        // Groups claims.setClaim("groups", List<String>)
        Optional.ofNullable(sid).ifPresent(s -> claims.setClaim("sid", s));

        if (aud.size() == 1) {
            claims.setAudience(aud.get(0));
        } else {
            claims.setAudience(aud);
        }

        return createToken(claims);
    }

    public static String azureClientCredentialsToken(List<String> aud, String sub, String issuer,  Map<String, String> additionalClaims) {
        var issuedAt = NumericDate.now();
        JwtClaims claims = createCommonClaims(sub, issuer, issuedAt);
        additionalClaims.forEach(claims::setStringClaim);

        if (aud.size() == 1) {
            claims.setAudience(aud.get(0));
        } else {
            claims.setAudience(aud);
        }

        claims.setClaim("ver", "2.0");
        claims.setClaim("oid", sub); // Konvensjon

        return createToken(claims);
    }

    private static JwtClaims createCommonClaims(String sub, String issuer, NumericDate issuedAt) {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setExpirationTime(NumericDate.fromSeconds(issuedAt.getValue() + 3600 * 6));
        claims.setGeneratedJwtId();
        claims.setIssuedAt(issuedAt);
        claims.setNotBefore(issuedAt);
        claims.setSubject(sub);
        claims.setStringClaim("acr", "Level4");
        return claims;
    }

    private static String createToken(JwtClaims claims) {
        RsaJsonWebKey senderJwk = KeyStoreTool.getJsonWebKey();
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKeyIdHeaderValue(KeyStoreTool.getJsonWebKey().getKeyId());
        jws.setAlgorithmHeaderValue("RS256");
        jws.setKey(senderJwk.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }

}
