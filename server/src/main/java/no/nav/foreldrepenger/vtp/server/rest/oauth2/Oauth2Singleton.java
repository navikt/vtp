package no.nav.foreldrepenger.vtp.server.rest.oauth2;

import no.nav.security.mock.oauth2.MockOAuth2Server;

public class Oauth2Singleton {

    private static MockOAuth2Server instance = new MockOAuth2Server();

    private Oauth2Singleton() {
    }

    public static MockOAuth2Server getInstance() {
        return instance;
    }

}
