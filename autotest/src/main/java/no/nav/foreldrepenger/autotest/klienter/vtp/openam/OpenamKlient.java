package no.nav.foreldrepenger.autotest.klienter.vtp.openam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;

public class OpenamKlient extends VTPKlient{

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
        HttpResponse resp = get("http://localhost:8080/fpsak/jetty/login");
        
        //henter ut state fra redirect url
        Pattern pattern = Pattern.compile("(.*)state=(.+?)(&.*)?$");
        Matcher matcher = pattern.matcher(session.getCurrentUrl());
        matcher.matches();
        String state = matcher.group(2);
        
        Map<String, String> data = new HashMap<String, String>();
        data.put("scope", "openid");
        data.put("state", state);
        data.put("client_id", "fpsak-localhost");
        data.put("iss", "https://localhost:8063/isso/oauth2");
        data.put("redirect_uri", "http://localhost:8080/fpsak/cb");
        data.put("code", rolle);
        
        String url = "http://localhost:8080/fpsak/cb";
        get(UrlCompose(url, data));
    }
    
    
}
