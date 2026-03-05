package no.nav.foreldrepenger.vtp.server.auth.rest.texas;

import no.nav.foreldrepenger.vtp.server.auth.rest.Issuers;

import java.util.List;

/**
 * Request for acquiring an access token for machine-to-machine use.
 */
public record TexasTokenRequest(
        Issuers identity_provider,
        String target,
        String resource,
        List<AuthorizationDetails> authorization_details,
        // Accepted for API compatibility; Texas mock does not cache and therefore ignores this value.
        Boolean skip_cache
) {
}
