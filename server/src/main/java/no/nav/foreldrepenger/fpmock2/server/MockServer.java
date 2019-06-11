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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.felles.KeystoreUtils;
import no.nav.foreldrepenger.fpmock2.felles.PropertiesUtils;
import no.nav.foreldrepenger.fpmock2.kafkaembedded.LocalKafkaServer;
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
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;


public class MockServer {

    private static final String HTTP_HOST = "0.0.0.0";
    private static final String SERVER_PORT = "8060";
    private Server server;
    private JettyHttpServer jettyHttpServer;
    private String host = HTTP_HOST;

    private final int port;
    private final LdapServer ldapServer;

    private static final Logger LOG = LoggerFactory.getLogger(MockServer.class);

    public static void main(String[] args) throws Exception {

        PropertiesUtils.initProperties();

        MockServer mockServer = new MockServer();

        mockServer.start();

    }

    public MockServer() throws Exception {
        LOG.info("Dummyprop er satt til: " + System.getenv("DUMMYPROP"));
        this.port = Integer.valueOf(System.getProperty("autotest.vtp.port", SERVER_PORT));

        // Bør denne settes fra ENV_VAR?
        System.setProperty("server.url", "https://localhost:" + getSslPort());

        server = new Server();
        setConnectors(server);

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

        ldapServer = new LdapServer(new File(KeystoreUtils.getKeystoreFilePath()), KeystoreUtils.getKeyStorePassword().toCharArray());

    }

    public void start() throws Exception {
        startKafkaServer();

        startLdapServer();
        startWebServer();
    }

    private void startKafkaServer() {

       // Properties.setProp("java.security.auth.login.config", "KafkaServerJaas.conf");

        Integer kafkaBrokerPort = Integer.parseInt(System.getProperty("kafkaBrokerPort","9092"));
        Integer zookeeperPort = Integer.parseInt(System.getProperty("zookeeper.port","2181"));
        LocalKafkaServer.startKafka(zookeeperPort,kafkaBrokerPort,List.of("privat-foreldrepenger-mottatBehandling-fpsak","privat-foreldrepenger-aksjonspunkthendelse-fpsak","privat-foreldrepenger-fprisk-utfor-t4","privat-foreldrepenger-tilkjentytelse-v1-local"));
    }

    private void startWebServer() throws IOException, Exception {
        HandlerContainer handler = (HandlerContainer) server.getHandler();

        TestscenarioTemplateRepositoryImpl templateRepositoryImpl = TestscenarioTemplateRepositoryImpl.getInstance();
        templateRepositoryImpl.load();


        DelegatingTestscenarioTemplateRepository templateRepository = new DelegatingTestscenarioTemplateRepository(templateRepositoryImpl);
        DelegatingTestscenarioRepository testScenarioRepository = new DelegatingTestscenarioRepository(TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        GsakRepo gsakRepo = new GsakRepo();
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();

        addRestServices(handler, testScenarioRepository, templateRepository, gsakRepo);

        addWebResources(handler);
        addWebGui(handler);

        startServer();

        // kjør soap oppsett etter jetty har startet
        addSoapServices(testScenarioRepository, templateRepository, journalRepository, gsakRepo);
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
                                   JournalRepository journalRepository,
                                   GsakRepo gsakRepo) {
        new SoapWebServiceConfig(jettyHttpServer).setup(testScenarioRepository, journalRepository, gsakRepo);
    }

    protected void addRestServices(HandlerContainer handler, DelegatingTestscenarioBuilderRepository testScenarioRepository,
                                   DelegatingTestscenarioTemplateRepository templateRepository,
                                   GsakRepo gsakRepo) {
        new RestConfig(handler, templateRepository).setup(testScenarioRepository, gsakRepo);
    }

    protected void addWebResources(HandlerContainer handlerContainer) {
        WebAppContext ctx = new WebAppContext(handlerContainer, Resource.newClassPathResource("/swagger"), "/swagger");



        ctx.setThrowUnavailableOnStartupException(true);
        ctx.setLogUrlOnStart(true);

        DefaultServlet defaultServlet = new DefaultServlet();

        ServletHolder servletHolder = new ServletHolder(defaultServlet);
        servletHolder.setInitParameter("dirAllowed", "false");

        ctx.addServlet(servletHolder, "/swagger");

    }

    protected void addWebGui(HandlerContainer handlerContainer) {
        WebAppContext ctx = new WebAppContext(handlerContainer, Resource.newClassPathResource("/webapps/frontend"), "/");
        //ctx.setDefaultsDescriptor(null);
        ctx.setThrowUnavailableOnStartupException(true);
        ctx.setLogUrlOnStart(true);

        DefaultServlet defaultServlet = new DefaultServlet();

        ServletHolder servletHolder = new ServletHolder(defaultServlet);
        servletHolder.setInitParameter("dirAllowed", "true");

        ctx.addServlet(servletHolder, "/webapps/frontend");

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

        if(null != System.getenv("ENABLE_CUSTOM_TRUSTSTORE") && System.getenv("ENABLE_CUSTOM_TRUSTSTORE").equalsIgnoreCase("true")) {
            sslContextFactory.setCertAlias("fpmock2");
            sslContextFactory.setKeyStorePath(System.getenv("CUSTOM_KEYSTORE_PATH"));
            sslContextFactory.setKeyStorePassword(System.getenv("CUSTOM_KEYSTORE_PASSWORD"));
            sslContextFactory.setKeyManagerPassword(System.getenv("CUSTOM_KEYSTORE_PASSWORD"));
        } else {
            sslContextFactory.setCertAlias("localhost-ssl");
            sslContextFactory.setKeyStorePath(KeystoreUtils.getKeystoreFilePath());
            sslContextFactory.setKeyStorePassword(KeystoreUtils.getKeyStorePassword());
            sslContextFactory.setKeyManagerPassword(KeystoreUtils.getKeyStorePassword());
        }

        @SuppressWarnings("resource")
        ServerConnector sslConnector = new ServerConnector(server,
            new SslConnectionFactory(sslContextFactory, "HTTP/1.1"),
            new HttpConnectionFactory(https));
        sslConnector.setPort(getSslPort());
        connectors.add(sslConnector);

        server.setConnectors(connectors.toArray(new Connector[connectors.size()]));
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
