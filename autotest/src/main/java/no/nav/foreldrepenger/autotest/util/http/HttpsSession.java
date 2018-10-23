package no.nav.foreldrepenger.autotest.util.http;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpsSession extends HttpSession {

    @Override
    protected CloseableHttpClient opprettKlient(boolean doRedirect) {
        try {
            HttpClientBuilder builder = HttpClients.custom().useSystemProperties();
            TrustManager[] sertifikater = new TrustManager[]{
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

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, sertifikater, null);
            
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext);
            PlainConnectionSocketFactory plainFactory = new PlainConnectionSocketFactory();

            
            if (doRedirect) {
                builder = builder.setRedirectStrategy(new LaxRedirectStrategy());
            } else {
                builder = builder.disableRedirectHandling();
            }

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register(
                    "https", factory).register("http", plainFactory).build();

            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
            connManager.setValidateAfterInactivity(100);
            builder.setConnectionManager(connManager);
            builder.setDefaultRequestConfig(getRequestConfig());
            return builder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
}
