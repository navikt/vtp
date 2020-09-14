package no.nav.foreldrepenger.vtp.felles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;


public class AzureOidcTokenGenerator {


    private List<String> aud = Arrays.asList(
            "OIDC"
    );
    private NumericDate expiration = NumericDate.fromSeconds(NumericDate.now().getValue() + 3600*6);
    private String issuer;
    private NumericDate issuedAt = NumericDate.now();
    private final String subject;
    private String kid = KeyStoreTool.getJsonWebKey().getKeyId();
    private String nonce;
    private Map<String, String> additionalClaims = new HashMap<>();

    public AzureOidcTokenGenerator(String brukerId, String nonce) {
        additionalClaims.put("azp", "OIDC");
        additionalClaims.put("acr", "Level4");
        this.subject = brukerId;
        this.nonce = nonce;

    }

    public void addAud(String e) {
        this.aud = new ArrayList<>(aud);
        this.aud.add(e);
    }

    AzureOidcTokenGenerator withoutAzp() {
        additionalClaims.remove("azp");
        return this;
    }

    AzureOidcTokenGenerator withExpiration(NumericDate expiration) {
        this.expiration = expiration;
        return this;
    }

    public AzureOidcTokenGenerator withIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    AzureOidcTokenGenerator withIssuedAt(NumericDate issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    AzureOidcTokenGenerator withKid(String kid) {
        this.kid = kid;
        return this;

    }

    AzureOidcTokenGenerator withClaim(String name, String value) {
        additionalClaims.put(name, value);
        return this;
    }

    AzureOidcTokenGenerator withAud(List<String> aud) {
        this.aud = aud;
        return this;
    }

    public String create() {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setExpirationTime(expiration);
        claims.setGeneratedJwtId();
        claims.setIssuedAt(issuedAt);
        claims.setSubject(subject);
        if (Objects.nonNull(nonce) && !nonce.isBlank()) {
            claims.setClaim("nonce", nonce);
        }
        if (aud.size() == 1) {
            claims.setAudience(aud.get(0));
        } else {
            claims.setAudience(aud);
        }
        for (Map.Entry<String, String> entry : additionalClaims.entrySet()) {
            claims.setStringClaim(entry.getKey(), entry.getValue());
        }
        RsaJsonWebKey senderJwk = KeyStoreTool.getJsonWebKey();
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKeyIdHeaderValue(kid);
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