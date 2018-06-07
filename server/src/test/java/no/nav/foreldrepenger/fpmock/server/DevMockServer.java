package no.nav.foreldrepenger.fpmock.server;

import org.eclipse.jetty.http.spi.JettyHttpServerProvider;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import no.nav.foreldrepenger.fpmock2.server.MockServer;
import no.nav.modig.testcertificates.TestCertificates;

public class DevMockServer extends MockServer {

    private String keystorePath;

    public static void main(String[] args) throws Exception {
        System.setProperty("com.sun.net.httpserver.HttpServerProvider", JettyHttpServerProvider.class.getName());

        TestCertificates.setupKeyAndTrustStore();
        String keystorePath = MockServer.class.getClassLoader().getResource("no/nav/modig/testcertificates/keystore.jks").toExternalForm();

        DevMockServer mockServer = new DevMockServer(keystorePath);
        mockServer.start();
    }

    public DevMockServer(String keystorePath) {
        super(8088);
        this.keystorePath = keystorePath;
    }

    @Override
    protected void setConnectors(Server server) {

        HttpConfiguration https = new HttpConfiguration();

        https.addCustomizer(new SecureRequestCustomizer());

        SslContextFactory sslContextFactory;
        sslContextFactory = new SslContextFactory(keystorePath);
        sslContextFactory.setTrustAll(true);
        sslContextFactory.setKeyStorePassword("devillokeystore1234");
        sslContextFactory.setKeyManagerPassword("devillokeystore1234");

        try (ServerConnector sslConnector = new ServerConnector(server,
            new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https))) {
            sslConnector.setPort(getPort());
            sslConnector.setHost(getHost());
            server.setConnectors(new Connector[] { sslConnector });
        }
    }

}
