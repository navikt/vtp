package no.nav.foreldrepenger.vtp.server.auth.rest;

import java.util.List;
import java.util.UUID;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;

import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;

public class TokenClaims {
    private static final int DEFAULT_EXPIRE_TIME_MINUTES = 60;
    private static final int EXPIRE_TIME_MINUTES = tokenExpireTime();
    public static final String AZP = "azp";
    public static final String ACR = "acr";
    public static final String SCP = "scp";
    public static final String NONCE = "nonce";
    public static final String AZP_NAME = "azp_name";
    public static final String NAVIDENT = "NAVident";

    private TokenClaims() {
        // Statisk implementasjon
    }

    public static JwtClaims stsTokenClaims(String sub, String issuer, String scope) {
        var claims = defaultJwtClaims(sub, issuer, sub);
        claims.setClaim(SCP, scope);
        claims.setClaim(AZP, sub);
        return claims;
    }

    public static JwtClaims openAmTokenClaims(String sub, String issuer, String nonce) {
        var claims = defaultJwtClaims(sub, issuer, "OIDC");
        claims.setClaim(AZP, "OIDC");
        claims.setClaim(ACR, "Level4");
        if (nonce !=  null) claims.setClaim(NONCE, nonce);
        return claims;
    }

    public static JwtClaims azureOnBehalfOfTokenClaims(NAVAnsatt ansatt,
                                                                  List<String> aud,
                                                                  String clientId,
                                                                  String issuer,
                                                                  String scope,
                                                                  String tenant) {
        var claims = defaultJwtClaims(clientId + ":" + ansatt.cn(), issuer, aud);
        claims.setClaim("oid", UUID.nameUUIDFromBytes(ansatt.cn().getBytes()).toString());
        claims.setClaim("tid", tenant);
        claims.setClaim("preferred_username", ansatt.email());
        claims.setClaim("ver", "2.0");
        claims.setClaim(NAVIDENT, ansatt.cn());
        claims.setClaim(SCP, scope);
        claims.setClaim(AZP, clientId);
        claims.setClaim(AZP_NAME, clientId);
        return claims;
    }

    public static JwtClaims azureSystemTokenClaims(List<String> aud,
                                                              String sub,
                                                              String clientId,
                                                              String issuer,
                                                              String tenantId) {
        var claims = defaultJwtClaims(sub, issuer, aud);
        claims.setClaim(AZP_NAME, clientId);
        claims.setClaim(AZP, clientId);
        claims.setClaim("oid", sub);
        claims.setClaim("tid", tenantId);
        claims.setClaim("ver", "2.0");
        claims.setClaim("roles", List.of("access_as_application"));
        return claims;
    }

    public static JwtClaims tokenXClaims(String sub, String issuer, String aud) {
        var claims =  defaultJwtClaims(sub, issuer, aud);
        claims.setClaim("pid", sub);
        claims.setClaim(ACR, "Level4");
        return claims;
    }

    public static JwtClaims defaultJwtClaims(String sub, String issuer, String audience) {
        return defaultJwtClaims(sub, issuer, List.of(audience));
    }

    public static JwtClaims defaultJwtClaims(String sub, String issuer, List<String> audience) {
        var claims = new JwtClaims();
        claims.setAudience(audience);
        claims.setIssuer(issuer);
        claims.setSubject(sub);
        claims.setIssuedAtToNow();
        claims.setNotBefore(NumericDate.now());
        claims.setExpirationTimeMinutesInTheFuture(EXPIRE_TIME_MINUTES);
        claims.setJwtId(UUID.randomUUID().toString());
        return claims;
    }

    private static int tokenExpireTime() {
        var token_expire_time_minutes = System.getenv("OVERRIDE_TOKEN_EXPIRE_TIME_MINUTES");
        if (token_expire_time_minutes != null && kanBliParsetTilInteger(token_expire_time_minutes) ) {
            return Integer.parseInt(token_expire_time_minutes);
        }
        return DEFAULT_EXPIRE_TIME_MINUTES;
    }

    public static boolean kanBliParsetTilInteger(String streng) {
        if (streng == null) {
            return false;
        }
        try {
            Integer.parseInt(streng);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
