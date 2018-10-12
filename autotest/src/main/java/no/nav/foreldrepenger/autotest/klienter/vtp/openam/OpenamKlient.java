package no.nav.foreldrepenger.autotest.klienter.vtp.openam;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;

public class OpenamKlient extends VTPKlient{

    public static final String ISSO_URL = "/isso";
    public static final String ISSO_JSON_AUTHENTICATE_URL = ISSO_URL + "/json/authenticate";
    public static final String ISSO_OAUTH_URL = ISSO_URL + "/oauth2";
    public static final String ISSO_OAUTH_ACCESS_TOKEN_URL = ISSO_OAUTH_URL + "/access_token";
    public static final String ISSO_OAUTH_AUTHORIZE_URL = ISSO_OAUTH_URL + "/authorize";
    public static final String ISSO_OAUTH_CONNECT_JWK_URI_URL = ISSO_OAUTH_URL + "/connect/jwk_uri";
    
    public OpenamKlient(HttpSession session) {
        super(session);
    }
    
    public void authenticate() {
        
    }
    
    public void accessToken() {
        
    }
    
    public void authorize() {
        
    }
    
    public void connectJwkUri() {
        
    }
    
    
}
