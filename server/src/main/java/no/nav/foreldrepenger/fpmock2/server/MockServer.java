package no.nav.foreldrepenger.fpmock2.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioBuilderRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioTemplateRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.modig.testcertificates.TestCertificates;

public class MockServer {

    private static final String HTTP_HOST = "0.0.0.0";
    private static final String SERVER_PORT = "8060";
    private Server server;
    private JettyHttpServer jettyHttpServer;
    private String host = HTTP_HOST;

    private final int port;

    public static void main(String[] args) throws Exception {
        TestCertificates.setupKeyAndTrustStore();
        
        PropertiesUtils.initProperties();

        MockServer mockServer = new MockServer(Integer.valueOf(System.getProperty("server.port", SERVER_PORT)));
        mockServer.start();

    }

    public MockServer(int port) {
        this.port = port;
        server = new Server();
        setConnectors(server);

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

    }

    public void start() throws Exception {
        HandlerContainer handler = (HandlerContainer) server.getHandler();

        TestscenarioTemplateRepositoryImpl templateRepositoryImpl = new TestscenarioTemplateRepositoryImpl();
        templateRepositoryImpl.load();

        DelegatingTestscenarioTemplateRepository templateRepository = new DelegatingTestscenarioTemplateRepository(templateRepositoryImpl);
        DelegatingTestscenarioRepository testScenarioRepository = new DelegatingTestscenarioRepository(new TestscenarioRepositoryImpl());

        addRestServices(handler, testScenarioRepository, templateRepository);

        addWebResources(handler);

        startServer();

        // kjør soap oppsett etter jetty har startet
        addSoapServices(testScenarioRepository, templateRepository);

    }

    protected void startServer() throws Exception {
        server.start();
        jettyHttpServer = new JettyHttpServer(server, true);
    }

    protected void addSoapServices(TestscenarioBuilderRepository testScenarioRepository,
                                   @SuppressWarnings("unused") TestscenarioTemplateRepository templateRepository) {
        new SoapWebServiceConfig(jettyHttpServer).setup(testScenarioRepository);
    }

    protected void addRestServices(HandlerContainer handler, DelegatingTestscenarioBuilderRepository testScenarioRepository,
                                   DelegatingTestscenarioTemplateRepository templateRepository) {
        new RestConfig(handler, templateRepository).setup(testScenarioRepository);
    }

    protected void addWebResources(HandlerContainer handlerContainer) {

        WebAppContext ctx = new WebAppContext(handlerContainer, Resource.newClassPathResource("/swagger"), "/swagger");
        ctx.setThrowUnavailableOnStartupException(true);
        ctx.setLogUrlOnStart(true);
        
        DefaultServlet defaultServlet = new DefaultServlet();
        ServletHolder servletHolder = new ServletHolder(defaultServlet);
        servletHolder.setInitParameter("dirAllowed", "false");

        ctx.addServlet(servletHolder, "/");
    }

    protected void setConnectors(Server server) {

        List<Connector> connectors = new ArrayList<>();

        @SuppressWarnings("resource")
        ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setPort(port);
        httpConnector.setHost(host);
        connectors.add(httpConnector);

        HttpConfiguration https = new HttpConfiguration();
        https.setSendServerVersion(false);
        https.setSendXPoweredBy(false);
        https.addCustomizer(new SecureRequestCustomizer());
        SslContextFactory sslContextFactory = new SslContextFactory();
        
        sslContextFactory.setCertAlias("localhost-ssl");
        sslContextFactory.setKeyStorePath(System.getProperty("no.nav.modig.security.appcert.keystore"));
        sslContextFactory.setKeyStorePassword(System.getProperty("no.nav.modig.security.appcert.password", "changeit"));
        sslContextFactory.setKeyManagerPassword(System.getProperty("no.nav.modig.security.appcert.password", "changeit"));

        @SuppressWarnings("resource")
        ServerConnector sslConnector = new ServerConnector(server,
            new SslConnectionFactory(sslContextFactory, "HTTP/1.1"),
            new HttpConnectionFactory(https));
        sslConnector.setPort(port + 3);
        connectors.add(sslConnector);

        server.setConnectors(connectors.toArray(new Connector[connectors.size()]));
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

}
