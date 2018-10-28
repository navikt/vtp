package no.nav.foreldrepenger.autotest.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpSession{

    protected CloseableHttpClient client;
    protected HttpClientContext context;
    protected CookieStore cookies;

    public HttpSession() {
        this.context = HttpClientContext.create();
        setCookies(new BasicCookieStore());
        this.client = opprettKlient(true);
    }

    public HttpResponse execute(HttpUriRequest request, Map<String, String> headers) throws IOException {
        applyHeaders(request, headers);
        return client.execute(request, context);
    }

    
    public HttpResponse get(String url) throws IOException {
        return get(url, new HashMap<>());
    }
    
    public HttpResponse get(String url, Map<String, String> headers) throws IOException {
        HttpGet request = new HttpGet(url);
        return execute(request, headers);
    }

    public HttpResponse post(String url, HttpEntity entity, Map<String, String> headers) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setEntity(entity);

        return execute(request, headers);
    }

    public HttpResponse put(String url, HttpEntity entity, Map<String, String> headers) throws IOException {
        HttpPut request = new HttpPut(url);
        request.setEntity(entity);
        return execute(request, headers);
    }

    public HttpResponse delete(String url, Map<String, String> headers) throws IOException {
        HttpDelete request = new HttpDelete(url);
        return execute(request, headers);
    }

    public void setRedirect(boolean doRedirect) {
        client = opprettKlient(doRedirect);
    }

    protected CloseableHttpClient opprettKlient(boolean doRedirect) {
        HttpClientBuilder builder = HttpClientBuilder.create();

        builder = builder.setDefaultRequestConfig(getRequestConfig());

        if (doRedirect) {
            builder = builder.setRedirectStrategy(new LaxRedirectStrategy());
        } else {
            builder = builder.disableRedirectHandling();
        }
        return builder.build();
    }

    protected RequestConfig getRequestConfig() {
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        int connectTimeoutMillis = 5000;
        requestBuilder = requestBuilder.setConnectTimeout(connectTimeoutMillis * 6);
        requestBuilder = requestBuilder.setSocketTimeout(connectTimeoutMillis * 6);
        requestBuilder = requestBuilder.setConnectionRequestTimeout(connectTimeoutMillis);
        
        return requestBuilder.build();
    }

    private void applyHeaders(HttpUriRequest request, Map<String, String> headers) {
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

    public void setUserCredentials(String username, String password) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        context.setCredentialsProvider(provider);
    }

    public CookieStore hentCookieStore() {
        return context.getCookieStore();
    }

    public void setCookies(CookieStore cookieStore) {
        context.setCookieStore(cookieStore);
    }

    public static Map<String, String> createEmptyHeaders() {
        return new HashMap<>();
    }

    protected void leggTilCookie(String name, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        leggTilCookie(cookie);
    }

    public void leggTilCookie(Cookie cookie) {
        hentCookieStore().addCookie(cookie);
    }
    
    public String getCurrentUrl() {
        return ((HttpUriRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST)).getURI().toString();
    }
    
    public static String readResponse(HttpResponse response){
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
}
