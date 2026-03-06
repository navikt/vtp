package no.nav.foreldrepenger.vtp.server.auth.rest.texas;

import no.nav.foreldrepenger.vtp.server.auth.rest.Issuers;

/**
 * Use this data type to exchange a user token for a machine token.
 */
public record TexasIntrospectRequest(
        Issuers identity_provider,
        String token
) {
}

