package no.nav.foreldrepenger.vtp.server.auth.rest.texas;

import org.jose4j.jwt.JwtClaims;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helper for building introspect responses based on RFC 7662, section 2.
 * Returns a {@code Map<String, Object>} containing all original token claims plus {@code active}.
 * When the token is invalid, the map contains only {@code active: false} and an {@code error} message.
 */
public final class TexasIntrospectResponse {

    private TexasIntrospectResponse() {
    }

    public static Map<String, Object> inactive(String error) {
        var map = new LinkedHashMap<String, Object>();
        map.put("active", false);
        map.put("error", error);
        return map;
    }

    public static Map<String, Object> active(JwtClaims claims) {
        var map = new LinkedHashMap<String, Object>();
        map.put("active", true);
        // Include all claims from the original token
        map.putAll(claims.getClaimsMap());
        return map;
    }
}

