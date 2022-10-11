package no.nav.foreldrepenger.vtp.server.auth.rest;

import static org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA256;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import com.fasterxml.jackson.annotation.JsonValue;
import com.nimbusds.jose.JOSEObject;
import com.nimbusds.jwt.JWTClaimsSet;

public record Token(@JsonValue String value, Integer expiresIn) {

    public Token(String value) {
        this(value, null);
    }

    public JWTClaimsSet parseToken() throws ParseException {
        return JWTClaimsSet.parse(JOSEObject.parse(value).getPayload().toJSONObject());
    }

    public static Token fra(JWTClaimsSet claims) {
        var jwk = JsonWebKeyHelper.getJsonWebKey();
        var jws = new JsonWebSignature();
        jws.setPayload(claims.toPayload().toString());
        jws.setKeyIdHeaderValue(jwk.getKeyId());
        jws.setKey(jwk.getPrivateKey());
        jws.setAlgorithmHeaderValue(RSA_USING_SHA256);
        try {
            return new Token(jws.getCompactSerialization(), expiresInSeconds(claims));
        } catch (JoseException e) {
            throw new RuntimeException("Noe gikk galt ved seralisering av JWT", e);
        }
    }

    private static int expiresInSeconds(JWTClaimsSet claims) {
        var issuedTime = claims.getIssueTime();
        var expirationTime = claims.getExpirationTime();
        return (int) ChronoUnit.SECONDS.between(issuedTime.toInstant(), expirationTime.toInstant());
    }


    @Override
    public String value() {
        return value;
    }
}
