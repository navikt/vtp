package no.nav.foreldrepenger.fpmock2.server;

import java.io.File;

import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.http.spi.JettyHttpServerProvider;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockServer {

    private static final Logger log = LoggerFactory.getLogger(MockServer.class);
    private static final String HTTP_HOST = "0.0.0.0";
    private Server server;
    private JettyHttpServer jettyHttpServer;
    private String host = HTTP_HOST;

    private final int port;

    public static void main(String[] args) throws Exception {
        PropertiesUtils.lagPropertiesFilFraTemplate();
        PropertiesUtils.initProperties();

        System.setProperty("com.sun.net.httpserver.HttpServerProvider", JettyHttpServerProvider.class.getName());

        MockServer mockServer = new MockServer(Integer.getInteger("server.port"));
        mockServer.start();

    }

    public MockServer(int port) {
        this.port=port;
    }

    protected void start() throws Exception {
        server = new Server();
        setConnectors(server);

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

        new RestConfig(contextHandlerCollection).setup();

        final ServletContextHandler context = new ServletContextHandler(contextHandlerCollection, "/web");
        DefaultServlet defaultServlet = new DefaultServlet();
        ServletHolder servletHolder = new ServletHolder(defaultServlet);
        if (new File("./webapp").exists()) {
            // kjører på docker (forventer at webapp er opprettet i Dockerfile
            servletHolder.setInitParameter("resourceBase", "./webapp/");
        } else {
            // kjører i utvikling IDE
            servletHolder.setInitParameter("resourceBase", "./server/src/main/webapp/");
        }
        servletHolder.setInitParameter("dirAllowed", "false");

        context.addServlet(servletHolder, "/");

        server.start();
        jettyHttpServer = new JettyHttpServer(server, true);
        // kjør soap oppsett etter
        new SoapWebServiceConfig(jettyHttpServer).setup();

        log.info("{} har startet, port={}", getClass().getName(), port);
    }

    protected void setConnectors(Server server) {
        try (ServerConnector connector = new ServerConnector(server)) {
            connector.setPort(port);
            connector.setHost(host);
            server.setConnectors(new Connector[] { connector });
        }
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

}
