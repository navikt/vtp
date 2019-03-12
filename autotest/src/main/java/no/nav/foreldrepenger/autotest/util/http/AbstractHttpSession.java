package no.nav.foreldrepenger.autotest.util.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpCoreContext;

public abstract class AbstractHttpSession implements HttpSession {

    private final CloseableHttpClient redirectClient;
    private final CloseableHttpClient nonredirectClient;
    protected CloseableHttpClient client;
    protected HttpClientContext context;
    protected CookieStore cookies;

    public AbstractHttpSession() {
        this.context = HttpClientContext.create();
        setCookies(new BasicCookieStore());
        this.redirectClient = opprettKlient(true);
        this.nonredirectClient = opprettKlient(false);
        this.client = this.redirectClient;
    }

    @Override
    public HttpResponse execute(HttpUriRequest request, Map<String, String> headers) throws IOException {
        applyHeaders(request, headers);
        return client.execute(request, context);
    }


    @Override
    public HttpResponse get(String url) throws IOException {
        return get(url, new HashMap<>());
    }

    @Override
    public HttpResponse get(String url, Map<String, String> headers) throws IOException {
        HttpGet request = new HttpGet(url);
        return execute(request, headers);
    }

    @Override
    public HttpResponse post(String url, HttpEntity entity, Map<String, String> headers) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setEntity(entity);

        return execute(request, headers);
    }

    @Override
    public HttpResponse put(String url, HttpEntity entity, Map<String, String> headers) throws IOException {
        HttpPut request = new HttpPut(url);
        request.setEntity(entity);
        return execute(request, headers);
    }

    @Override
    public HttpResponse delete(String url, Map<String, String> headers) throws IOException {
        HttpDelete request = new HttpDelete(url);
        return execute(request, headers);
    }

    @Override
    public void setRedirect(boolean doRedirect) {
        client = doRedirect?redirectClient:nonredirectClient;
    }

    protected abstract CloseableHttpClient opprettKlient(boolean doRedirect);

    @Override
    public RequestConfig getRequestConfig() {
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        int connectTimeoutMillis = 5000;
        requestBuilder = requestBuilder.setConnectTimeout(connectTimeoutMillis * 6);
        requestBuilder = requestBuilder.setSocketTimeout(connectTimeoutMillis * 6);
        requestBuilder = requestBuilder.setConnectionRequestTimeout(connectTimeoutMillis);

        return requestBuilder.build();
    }

    @Override
    public void setUserCredentials(String username, String password) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        context.setCredentialsProvider(provider);
    }

    @Override
    public CookieStore hentCookieStore() {
        return context.getCookieStore();
    }

    @Override
    public void setCookies(CookieStore cookieStore) {
        context.setCookieStore(cookieStore);
    }

    @Override
    public void leggTilCookie(String name, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        leggTilCookie(cookie);
    }

    @Override
    public void leggTilCookie(Cookie cookie) {
        hentCookieStore().addCookie(cookie);
    }

    @Override
    public String getCurrentUrl() {
        return ((HttpUriRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST)).getURI().toString();
    }

}
