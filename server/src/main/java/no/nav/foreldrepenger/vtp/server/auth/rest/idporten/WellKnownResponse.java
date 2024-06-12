package no.nav.foreldrepenger.vtp.server.auth.rest.idporten;

import java.util.List;

public record WellKnownResponse(String issuer,
                                String authorization_endpoint,
                                String jwks_uri,
                                String token_endpoint,
                                List<String> acr_values_supported,
                                List<String> ui_locales_supported) {
}
