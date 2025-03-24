package no.nav.foreldrepenger.vtp.server;

import no.nav.foreldrepenger.util.KeystoreUtils;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaServer;
import no.nav.foreldrepenger.vtp.ldap.LdapServer;
import no.nav.foreldrepenger.vtp.testmodell.repo.ArbeidsgiverPortalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.ArbeidsgiverPortalRepositoryImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;


public class MockServer {

    private static final String HTTP_HOST = "0.0.0.0";
    private static final String SERVER_PORT = "8060";
    private static final Logger LOG = LoggerFactory.getLogger(MockServer.class);

    private static final String TRUSTSTORE_PASSW_PROP = "javax.net.ssl.trustStorePassword";
    private static final String TRUSTSTORE_PATH_PROP = "javax.net.ssl.trustStore";
    private static final String KEYSTORE_PASSW_PROP = "no.nav.modig.security.appcert.password";
    private static final String KEYSTORE_PATH_PROP = "no.nav.modig.security.appcert.keystore";
    public static final String CONTEXT_PATH = System.getProperty("server.context.path", "/rest");

    private final int port;
    private final LdapServer ldapServer;
    private final LocalKafkaServer kafkaServer;
    private Server server;
    private String host = HTTP_HOST;

    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    public MockServer() throws Exception {
        this.port = Integer.parseInt(System.getProperty("autotest.vtp.port", SERVER_PORT));

        // Bør denne settes fra ENV_VAR?
        System.setProperty("server.url", "https://localhost:" + getSslPort());

        server = new Server();
        setConnectors(server);

        ldapServer = new LdapServer(new File(KeystoreUtils.getKeystoreFilePath()), KeystoreUtils.getKeyStorePassword().toCharArray());
        var kafkaBrokerPort = Integer.parseInt(System.getProperty("kafkaBrokerPort", "9092"));
        kafkaServer = new LocalKafkaServer(kafkaBrokerPort, getBootstrapTopics());
    }

    public static void main(String[] args) throws Exception {
        PropertiesUtils.initProperties();
        var mockServer = new MockServer();
        mockServer.start();
    }

    public void start() throws Exception {
        startLdapServer();
        startKafkaServer();
        startWebServer();
    }

    private void startKafkaServer() {
        kafkaServer.start();
    }

    private Set<String> getBootstrapTopics() {
        return new HashSet<>(getEnvValueList("CREATE_TOPICS"));
    }

    private static List<String> getEnvValueList(String envName) {
        return Arrays.stream((null != System.getenv(envName) ? System.getenv(envName) : "").split(","))
                .map(String::trim)
                .toList();
    }

    @SuppressWarnings("resource")
    private void startWebServer() throws Exception {
        var instance = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        var testScenarioRepository = new DelegatingTestscenarioRepository(instance);
        var gsakRepo = new GsakRepo();
        var journalRepository = JournalRepositoryImpl.getInstance();
        var fagerPortalRepository = ArbeidsgiverPortalRepositoryImpl.getInstance();

        addRestServices(testScenarioRepository, instance, gsakRepo, journalRepository, fagerPortalRepository);

        startServer();
    }

    private void addRestServices(DelegatingTestscenarioRepository testScenarioRepository, TestscenarioRepositoryImpl instance, GsakRepo gsakRepo, JournalRepositoryImpl journalRepository, ArbeidsgiverPortalRepository fagerPortalRepository) {
        var config = new ApplicationConfigJersey()
                .setup(testScenarioRepository,
                        instance,
                        gsakRepo,
                        kafkaServer.getLocalProducer(),
                        journalRepository,
                        fagerPortalRepository);

        var context = new ServletContextHandler();
        context.setContextPath(CONTEXT_PATH);
        var jerseyServlet = new ServletHolder(new ServletContainer(config));
        jerseyServlet.setInitOrder(1);
        context.addServlet(jerseyServlet, "/*");
        server.setHandler(context);
    }

    private void startLdapServer() {
        var ldapThread = new Thread(ldapServer::start, "LdapServer");
        ldapThread.setDaemon(true);
        ldapThread.start();
    }

    protected void startServer() throws Exception {
        server.start();
        server.join();
    }


    protected void setConnectors(Server server) {

        var connectors = new ArrayList<>();

        @SuppressWarnings("resource") var httpConnector = new ServerConnector(server);
        httpConnector.setPort(port);
        httpConnector.setHost(host);
        connectors.add(httpConnector);

        var https = new HttpConfiguration();
        https.setSendServerVersion(false);
        https.setSendXPoweredBy(false);
        https.addCustomizer(new SecureRequestCustomizer());
        var sslConnectionFactory = new SslConnectionFactory("HTTP/1.1");
        var sslContextFactory = sslConnectionFactory.getSslContextFactory();

        sslContextFactory.setCertAlias("localhost-ssl");
        sslContextFactory.setKeyStorePath(KeystoreUtils.getKeystoreFilePath());
        sslContextFactory.setKeyStorePassword(KeystoreUtils.getKeyStorePassword());
        sslContextFactory.setKeyManagerPassword(KeystoreUtils.getKeyStorePassword());
        LOG.info("Starting TLS with keystore path {} ", KeystoreUtils.getKeystoreFilePath());


        // truststore avgjør hva vi stoler på av sertifikater når vi gjør utadgående TLS kall
        System.setProperty(TRUSTSTORE_PATH_PROP, KeystoreUtils.getTruststoreFilePath());
        System.setProperty(TRUSTSTORE_PASSW_PROP, KeystoreUtils.getTruststorePassword());

        // keystore genererer sertifikat og TLS for innkommende kall. Bruker standard prop hvis definert, ellers faller tilbake på modig props
        var keystoreProp = System.getProperty("javax.net.ssl.keyStore") != null ? "javax.net.ssl.keyStore" : KEYSTORE_PATH_PROP;
        var keystorePasswProp =
                System.getProperty("javax.net.ssl.keyStorePassword") != null ? "javax.net.ssl.keyStorePassword" : KEYSTORE_PASSW_PROP;
        System.setProperty(keystoreProp, KeystoreUtils.getKeystoreFilePath());
        System.setProperty(keystorePasswProp, KeystoreUtils.getKeyStorePassword());

        @SuppressWarnings("resource") var sslConnector = new ServerConnector(server, sslConnectionFactory,
                new HttpConnectionFactory(https));
        sslConnector.setPort(getSslPort());
        connectors.add(sslConnector);
        server.setConnectors(connectors.toArray(new Connector[0]));
    }

    private Integer getSslPort() {
        return Integer.valueOf(System.getProperty("server.https.port", "" + (port + 3)));
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

}
