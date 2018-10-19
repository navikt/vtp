package no.nav.foreldrepenger.fpmock2.server;

import java.io.File;
import java.io.IOException;
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

import no.nav.foreldrepenger.fpmock2.felles.PropertiesUtils;
import no.nav.foreldrepenger.fpmock2.ldap.LdapServer;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioBuilderRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioTemplateRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.JournalRepositoryImpl;
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
    private final LdapServer ldapServer;

    public static void main(String[] args) throws Exception {
        TestCertificates.setupKeyAndTrustStore();
        
        PropertiesUtils.initProperties();

        
        MockServer mockServer = new MockServer();
        
        mockServer.start();

    }

    public MockServer() throws Exception {
        this.port = Integer.valueOf(System.getProperty("server.port", SERVER_PORT));

        System.setProperty("server.url", "https://localhost:" + getSslPort());

        server = new Server();
        setConnectors(server);

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);
        
        ldapServer = new LdapServer(new File(getKeystoreFilePath()), getKeyStorePassword().toCharArray());

    }

    public void start() throws Exception {
        startLdapServer();
        startWebServer();
    }

    private void startWebServer() throws IOException, Exception {
        HandlerContainer handler = (HandlerContainer) server.getHandler();

        TestscenarioTemplateRepositoryImpl templateRepositoryImpl = TestscenarioTemplateRepositoryImpl.getInstance();
        templateRepositoryImpl.load();

        DelegatingTestscenarioTemplateRepository templateRepository = new DelegatingTestscenarioTemplateRepository(templateRepositoryImpl);
        DelegatingTestscenarioRepository testScenarioRepository = new DelegatingTestscenarioRepository(TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();

        addRestServices(handler, testScenarioRepository, templateRepository);

        addWebResources(handler);

        startServer();

        // kjør soap oppsett etter jetty har startet
        addSoapServices(testScenarioRepository, templateRepository, journalRepository);
    }

    private void startLdapServer() {
        Thread ldapThread = new Thread(() -> ldapServer.start(), "LdapServer");
        ldapThread.setDaemon(true);
        ldapThread.start();
    }

    protected void startServer() throws Exception {
        server.start();
        jettyHttpServer = new JettyHttpServer(server, true);
    }

    protected void addSoapServices(TestscenarioBuilderRepository testScenarioRepository,
                                   @SuppressWarnings("unused") TestscenarioTemplateRepository templateRepository,
                                   JournalRepository journalRepository) {
        new SoapWebServiceConfig(jettyHttpServer).setup(testScenarioRepository, journalRepository);
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

        if(null != System.getProperty("NAV_TRUSTSTORE_PATH")) {
            sslContextFactory.setKeyStorePath(System.getProperty("NAV_TRUSTSTORE_PATH"));
            sslContextFactory.setKeyStorePassword(System.getProperty("NAV_TRUSTSTORE_PASSWORD", "changeit"));
            sslContextFactory.setKeyManagerPassword(System.getProperty("NAV_TRUSTSTORE_PASSWORD", "changeit"));
        } else {
            sslContextFactory.setCertAlias("localhost-ssl");
            sslContextFactory.setKeyStorePath(getKeystoreFilePath());
            sslContextFactory.setKeyStorePassword(getKeyStorePassword());
            sslContextFactory.setKeyManagerPassword(getKeyStorePassword());
        }

        @SuppressWarnings("resource")
        ServerConnector sslConnector = new ServerConnector(server,
            new SslConnectionFactory(sslContextFactory, "HTTP/1.1"),
            new HttpConnectionFactory(https));
        sslConnector.setPort(getSslPort());
        connectors.add(sslConnector);

        server.setConnectors(connectors.toArray(new Connector[connectors.size()]));
    }

    private String getKeyStorePassword() {
        return System.getProperty("no.nav.modig.security.appcert.password", "changeit");
    }

    private String getKeystoreFilePath() {
        return System.getProperty("no.nav.modig.security.appcert.keystore");
    }

    private Integer getSslPort() {
        Integer sslPort = Integer.valueOf(System.getProperty("server.https.port", "" + (port + 3)));
        return sslPort;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

}
