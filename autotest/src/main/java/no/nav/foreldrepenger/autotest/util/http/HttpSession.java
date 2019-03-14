package no.nav.foreldrepenger.autotest.util.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

public interface HttpSession {
    static Map<String, String> createEmptyHeaders() {
        return new HashMap<>();
    }

    static String readResponse(HttpResponse response){
        try{
            HttpEntity entity = response.getEntity();
            if(entity == null){
                return "";
            }
            return EntityUtils.toString(entity, "UTF-8");
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    HttpResponse execute(HttpUriRequest request, Map<String, String> headers) throws IOException;

    HttpResponse get(String url) throws IOException;

    HttpResponse get(String url, Map<String, String> headers) throws IOException;

    HttpResponse post(String url, HttpEntity entity, Map<String, String> headers) throws IOException;

    HttpResponse put(String url, HttpEntity entity, Map<String, String> headers) throws IOException;

    HttpResponse delete(String url, Map<String, String> headers) throws IOException;

    void setRedirect(boolean doRedirect);

    default void applyHeaders(HttpUriRequest request, Map<String, String> headers) {
        for (String headerKey : headers.keySet()) {
            request.addHeader(headerKey, headers.get(headerKey));
        }

        //Hack for missing cookies in header (Client refuses to set cookies from one domain to another)
        StringBuilder cookies = new StringBuilder();
        CookieStore cookieStore = hentCookieStore();
        List<Cookie> cookiesList = cookieStore.getCookies();


        for (Cookie cookie : cookiesList) {
            cookies.append(String.format("%s=%s; ", cookie.getName(), cookie.getValue()));
        }
        request.addHeader("Cookie", cookies.toString());
    }

    void setUserCredentials(String username, String password);

    CookieStore hentCookieStore();

    void setCookies(CookieStore cookieStore);

    void leggTilCookie(String name, String value, String domain, String path);

    void leggTilCookie(Cookie cookie);

    String getCurrentUrl();
}
