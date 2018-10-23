package no.nav.foreldrepenger.autotest.util.http.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;

public abstract class Rest {

    protected HttpSession session;

    protected static final String ACCEPT_TEXT_HEADER = "application/text";
    
    private static final String WRONG_STATUS_MESSAGE_FORMAT = "Request returned unexpected status code expected range %s got %s\n%s";
    private static final String AUTHORIZATION_FORMAT = "Basic %s";

    public Rest(HttpSession session) {
        this.session = session;
    }

    public void setUserCredentials(String username, String password) {
        session.setUserCredentials(username, password);
    }

    /*
     * GET
     */
    
    protected HttpResponse get(String url) throws IOException {

        return get(url, HttpSession.createEmptyHeaders());
    }

    protected HttpResponse get(String url, Map<String, String> headers) throws IOException {

        return session.get(url, headers);
    }

    /*
     * POST
     */

    protected HttpResponse post(String url, HttpEntity entity) throws IOException {
        return post(url, entity, HttpSession.createEmptyHeaders());
    }
    
    protected HttpResponse post(String url, String request, Map<String, String> headers) throws UnsupportedEncodingException, IOException {
        return post(url, new StringEntity(request), headers);
    }
    
    protected HttpResponse post(String url, String request) throws UnsupportedEncodingException, IOException {
        return post(url, request, HttpSession.createEmptyHeaders());
    }

    protected HttpResponse post(String url, HttpEntity entity, Map<String, String> headers) throws IOException {
        return session.post(url, entity, headers);
    }
    
    /*
     * PUT
     */

    protected HttpResponse put(String url, HttpEntity entity) throws IOException {
        return put(url, entity, HttpSession.createEmptyHeaders());
    }

    protected HttpResponse put(String url, HttpEntity entity, Map<String, String> headers) throws IOException {
        return session.put(url, entity, headers);
    }
    
    /*
     * DELETE
     */

    protected HttpResponse delete(String url) throws IOException {
        return delete(url, HttpSession.createEmptyHeaders());
    }

    protected HttpResponse delete(String url, Map<String, String> headers) throws IOException {
        return session.delete(url, headers);
    }
    
    

    protected String basicAuthenticationHeaderValue(String username, String password) {
        String encodedAuth = Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes(StandardCharsets.UTF_8));
        return String.format(AUTHORIZATION_FORMAT, encodedAuth);
    }
    
    protected String hentResponseBody(HttpResponse response) {
        return HttpSession.readResponse(response);
    }
    
    /*
     * COOKIES
     */

    protected void addCookie(String name, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        addCookie(cookie);
    }

    protected void addCookie(Cookie cookie) {
        session.leggTilCookie(cookie);
    }
    
    /*
     * STATUSCODE VALIDATION
     */

    protected void ValidateResponse(HttpResponse response, int expectedStatus) {
        ValidateResponse(response, new StatusRange(expectedStatus, expectedStatus), "");
    }
    
    protected void ValidateResponse(HttpResponse response, int expectedStatus, String body) {
        ValidateResponse(response, new StatusRange(expectedStatus, expectedStatus), body);
    }
    
    protected void ValidateResponse(HttpResponse response, StatusRange expectedRange) {
        ValidateResponse(response, expectedRange, "");
    }

    protected void ValidateResponse(HttpResponse response, StatusRange expectedRange, String body) {
        int statuscode = response.getStatusLine().getStatusCode();
        
        if(!expectedRange.inRange(statuscode)) {
            if(body.equals("")) {
                body = hentResponseBody(response);
            }
            
            throw new RuntimeException(String.format(WRONG_STATUS_MESSAGE_FORMAT, expectedRange, statuscode, body));
        }
    }

    /*
     * URL ENCODING
     */
    public String UrlCompose(String url, Map<String, String> data) {
        return url + UrlEncodeQuery(data);
    }
    
    public String UrlEncodeQuery(Map<String, String> data) {
        StringBuilder query = new StringBuilder("?");
        for (Map.Entry<String, String> item : data.entrySet()) {
            if (item.getValue() != null && !item.getKey().isEmpty() && !item.getValue().isEmpty()) {
                String queryKey = UrlEncodeItem(item.getKey());
                String queryValue = UrlEncodeItem(item.getValue());
                query.append(String.format("%s=%s&", queryKey, queryValue));
            }
        }
        return query.substring(0, query.length() - 1);
    }

    public String UrlEncodeItem(String item) {
        try {
            return URLEncoder.encode(item, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Unable to encode '" + item + "': " + e.getMessage());
        }
    }

    public abstract String hentRestRotUrl();
}
