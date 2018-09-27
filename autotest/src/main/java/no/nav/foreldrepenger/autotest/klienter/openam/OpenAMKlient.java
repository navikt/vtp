package no.nav.foreldrepenger.autotest.klienter.openam;

import no.nav.foreldrepenger.autotest.klienter.openam.dto.OpenAMAccessToken;
import no.nav.foreldrepenger.autotest.klienter.openam.dto.OpenAMSessionAuth;
import no.nav.foreldrepenger.autotest.klienter.openam.dto.OpenAMSessionToken;
import no.nav.foreldrepenger.autotest.klienter.openam.dto.OpenAMTokenLogin;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenAMKlient extends JsonRest {

    private String baseUrl;
    private String redirectUriEncoded;
    private String redirectBase = "http://localhost:8080";

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    public OpenAMKlient(HttpSession session){
        super(session);
    }
    
    public OpenAMKlient(HttpSession session, String baseUrl) throws UnsupportedEncodingException {
        super(session);
        this.baseUrl = baseUrl;
        this.redirectUriEncoded = URLEncoder.encode(redirectBase + "/vedtak/cb", UTF_8);
    }
    
    public void loginMock(String issuer) {
        // TODO fikses når man kan bruke autotest internt i server
        //String token = new OidcTokenGenerator().withIssuer(issuer).create();
        //addCookie("ID_token", token, "devillo.no", "/");
    }
    
    public void loginSession(String username, String password, String oicdUser, String oicdPass) throws IOException {
    	try {
    		OpenAMAccessToken token = login(username,
            		password,
            		oicdUser,
            		oicdPass);
            
            addCookie("ID_token", token.id_token, "devillo.no", "/");
            addCookie("refresh_token", token.refresh_token, "devillo.no", "/");
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Kunne ikke logge inn via OpenAM: " + e.getMessage());
        }
    } 

    public OpenAMAccessToken login(String username, String password, String oicdUser, String oicdPass) throws IOException {
        String sessionId = hentSessionId();
        String sessionToken = hentSessionToken(sessionId, username, password);
        String authorizationCode = getAuthorizationCode(sessionToken);
        return hentAuthorizationToken(authorizationCode, oicdUser, oicdPass);
    }

    private String hentSessionId() throws IOException {
        HttpResponse response;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Negotiate 34f32q3g");
        headers.put("X-NoSession", "true");
        headers.put("X-Password", "anonymous");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Username", "anonymous");

        response = post(baseUrl + "/json/authenticate?realm=/&goto=" + baseUrl + "/oauth2/authorize?session=winssochain&authIndexType=service&authIndexValue=winssochain&response_type=code&scope=openid&client_id=OIDC&state=state_1234&redirect_uri=" + redirectUriEncoded,
                new StringEntity(""), headers);
        ValidateResponse(response, 200);

        String result = hentResponseBody(response);

        return hentObjectMapper().readValue(result, OpenAMSessionAuth.class).authId;
    }

    private String hentSessionToken(String sessionId, String username, String password) throws IOException {
        String url = baseUrl + "/json/authenticate";

        OpenAMTokenLogin tokenLogin = new OpenAMTokenLogin(sessionId, username, password);
        
        return postOgHentJson(url, tokenLogin, OpenAMSessionToken.class, StatusRange.STATUS_SUCCESS).tokenId;
    }

    private String getAuthorizationCode(String sessionToken) throws IOException {
        String url = baseUrl + "/oauth2/authorize?response_type=code&scope=openid&client_id=fpsak-localhost&state=dummy&redirect_uri=" + redirectUriEncoded;

        //Cookies
        BasicClientCookie cookieReal = new BasicClientCookie("nav-isso", sessionToken);
        cookieReal.setDomain("isso-t.adeo.no");
        cookieReal.setPath("/");
        session.hentCookieStore().addCookie(cookieReal);

        //Headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        //RESPONSE
        session.setRedirect(false);
        HttpResponse response = get(url, headers);
        System.out.println(hentResponseBody(response));
        session.setRedirect(true);

        //Parse Result
        String locationHeader = response.getFirstHeader("Location").getValue();
        Pattern pattern = Pattern.compile("code=(.+?)&");
        Matcher matcher = pattern.matcher(locationHeader);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new RuntimeException("Fant ikke authrorization code i header: " + locationHeader);
        }
        
        
    }

    public OpenAMAccessToken hentAuthorizationToken(String authorizationCode, String username, String password) throws UnsupportedCharsetException, IOException {
        String url = baseUrl + "/oauth2/access_token";

        //Cookies

        //Headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", basicAuthenticationHeaderValue(username, password));
        headers.put("Cache-Control", "no-cache");
        headers.put("Content-type", "application/x-www-form-urlencoded");

        //Request
        String realm = "/";
        String data = "grant_type=authorization_code"
                + "&realm=" + realm
                + "&redirect_uri=" + redirectUriEncoded
                + "&code=" + authorizationCode;
        
        HttpResponse response =  post(url, new StringEntity(data, StandardCharsets.UTF_8.name()),headers);
        ValidateResponse(response, StatusRange.STATUS_SUCCESS);
        return hentObjectMapper().readValue(hentResponseBody(response), OpenAMAccessToken.class);
    }

    @Override
    public String hentRestRotUrl() {
        return null;
    }

    
}


