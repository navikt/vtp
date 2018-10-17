package no.nav.foreldrepenger.autotest.klienter.vtp.openam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import org.apache.http.impl.cookie.BasicClientCookie;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.fpmock2.server.rest.OidcTokenGenerator;
import no.nav.modig.testcertificates.TestCertificates;

public class OpenamKlient extends VTPKlient{

    static {
        TestCertificates.setupKeyAndTrustStore();
    }
    
    public static final String ISSO_URL = "/isso";
    public static final String ISSO_JSON_AUTHENTICATE_URL = ISSO_URL + "/json/authenticate";
    public static final String ISSO_OAUTH_URL = ISSO_URL + "/oauth2";
    public static final String ISSO_OAUTH_BYPASS_URL = ISSO_OAUTH_URL + "/bypass?username=%s";
    public static final String ISSO_OAUTH_ACCESS_TOKEN_URL = ISSO_OAUTH_URL + "/access_token";
    public static final String ISSO_OAUTH_AUTHORIZE_URL = ISSO_OAUTH_URL + "/authorize";
    public static final String ISSO_OAUTH_CONNECT_JWK_URI_URL = ISSO_OAUTH_URL + "/connect/jwk_uri";
    
    public OpenamKlient(HttpSession session) {
        super(session);
    }
    
    public void bypass(String username) throws IOException {
        String url = String.format(ISSO_OAUTH_BYPASS_URL, username);
        getJson(url);
    }
    
    public void authenticate() {
        
    }
    
    public void accessToken() {
        
    }
    
    public void authorize() {
        
    }
    
    public void connectJwkUri() {
        
    }

    public void logInnMedRolle(String rolle) throws IOException {
        String issuer = "https://localhost:8063/isso/oauth2";
        String token = new OidcTokenGenerator(rolle).withIssuer(issuer).create();
        
        BasicClientCookie cookie = new BasicClientCookie("ID_token", token);
        cookie.setPath("/");
        cookie.setDomain("");
        cookie.setExpiryDate(new Date(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        session.leggTilCookie(cookie);
    }
    
    
}
