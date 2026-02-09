package no.nav.foreldrepenger.vtp.server.auth.rest.tokenx;

/**
 * Use this data type to exchange a user token for a machine token.
 */
public record TokenExchangeRequest(
        String identity_provider,
        /*
          Force renewal of token. Defaults to false if omitted.
         */
        Boolean skip_cache,
        /*
          Scope or identifier for the target application.
         */
        String target,
        /*
          The user's access token, usually found in the _Authorization_ header in requests to your application.
         */
        String user_token
) {
}

