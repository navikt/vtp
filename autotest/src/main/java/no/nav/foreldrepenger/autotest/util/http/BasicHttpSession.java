package no.nav.foreldrepenger.autotest.util.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

public class BasicHttpSession extends AbstractHttpSession {
    private static final ThreadLocal<BasicHttpSession> sessions = ThreadLocal.withInitial(() -> new BasicHttpSession());

    public static BasicHttpSession session(){
        return sessions.get();
    }

    private BasicHttpSession() {
    }

    protected CloseableHttpClient opprettKlient(boolean doRedirect) {
        HttpClientBuilder builder = HttpClientBuilder.create();

        builder = builder.setDefaultRequestConfig(getRequestConfig());

        if (doRedirect) {
            builder = builder.setRedirectStrategy(new LaxRedirectStrategy());
        } else {
            builder = builder.disableRedirectHandling();
        }
        builder.setMaxConnPerRoute(30);
        builder.setMaxConnTotal(60);
        return builder.build();
    }

}
