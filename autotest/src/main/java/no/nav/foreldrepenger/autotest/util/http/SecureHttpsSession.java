package no.nav.foreldrepenger.autotest.util.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;

public class SecureHttpsSession extends AbstractHttpSession {
    private static final ThreadLocal<SecureHttpsSession> sessions = ThreadLocal.withInitial(() -> new SecureHttpsSession());

    private static final CloseableHttpClient redirectClient = getKlient(true);
    private static final CloseableHttpClient nonRedirectClient = getKlient(false);

    public static SecureHttpsSession session() {
        return sessions.get();
    }

    @Override
    protected CloseableHttpClient opprettKlient(boolean doRedirect) {
        return doRedirect ? redirectClient : nonRedirectClient;
    }

    private static CloseableHttpClient getKlient(boolean doRedirect) {
        try {
            HttpClientBuilder builder = HttpClients.custom().useSystemProperties();
            TrustManager[] sertifikater = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                        }
                    }
            };

            SSLContext sslContext;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, sertifikater, null);
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                throw new IllegalStateException("Kunne ikke initialisere TLS", e);
            }
            builder.setSSLContext(sslContext);
            builder.setMaxConnPerRoute(20);
            builder.setMaxConnTotal(200);
            
            if (doRedirect) {
                builder = builder.setRedirectStrategy(new LaxRedirectStrategy());
            } else {
                builder = builder.disableRedirectHandling();
            }

            builder.setKeepAliveStrategy(createKeepAliveStrategy(30));
            
            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            int connectTimeoutMillis = 5000;
            requestBuilder = requestBuilder.setConnectTimeout(connectTimeoutMillis * 6);
            requestBuilder = requestBuilder.setSocketTimeout(connectTimeoutMillis * 6);
            
            builder.setDefaultRequestConfig(requestBuilder.build());
            builder.setRetryHandler(new StandardHttpRequestRetryHandler());

            return builder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
