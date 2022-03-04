package no.nav.foreldrepenger.vtp.server;

import static no.nav.foreldrepenger.vtp.server.ApplicationConfigJersey.API_URI;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import io.swagger.jaxrs.config.BeanConfig;
import no.nav.familie.topic.Topic;
import no.nav.familie.topic.TopicManifest;
import no.nav.foreldrepenger.util.KeystoreUtils;
import no.nav.foreldrepenger.util.PropertiesUtils;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaServer;
import no.nav.foreldrepenger.vtp.ldap.LdapServer;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;


public class MockServer {

    private static final String HTTP_HOST = "0.0.0.0";
    private static final String SERVER_PORT = "8060";
    private static final Logger LOG = LoggerFactory.getLogger(MockServer.class);

    private static final String TRUSTSTORE_PASSW_PROP = "javax.net.ssl.trustStorePassword";
    private static final String TRUSTSTORE_PATH_PROP = "javax.net.ssl.trustStore";
    private static final String KEYSTORE_PASSW_PROP = "no.nav.modig.security.appcert.password";
    private static final String KEYSTORE_PATH_PROP = "no.nav.modig.security.appcert.keystore";

    private final int port;
    private final LdapServer ldapServer;
    private final LocalKafkaServer kafkaServer;
    private Server server;
    private JettyHttpServer jettyHttpServer;
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

        var contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

        ldapServer = new LdapServer(new File(KeystoreUtils.getKeystoreFilePath()), KeystoreUtils.getKeyStorePassword().toCharArray());
        var kafkaBrokerPort = Integer.parseInt(System.getProperty("kafkaBrokerPort", "9092"));
        var zookeeperPort = Integer.parseInt(System.getProperty("zookeeper.port", "2181"));
        kafkaServer = new LocalKafkaServer(zookeeperPort, kafkaBrokerPort, getBootstrapTopics());

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
        /*
         * Unngå å legge til flere topics i denne listen: Benytt heller miljøvariablen "CREATE_TOPICS".
         *
         * Listen under kan fjernes hvis alle som starter VTP og trenger topic-ene under kjører med:
         * CREATE_TOPICS=topicManTrenger1, topicManTrenger2
         */
        final var topicsOldMethod = List.of("privat-foreldrepenger-mottatBehandling-fpsak",
                "privat-foreldrepenger-aksjonspunkthendelse-fpsak",
                "privat-foreldrepenger-dokumenthendelse-vtp",
                "privat-foreldrepenger-historikkinnslag-vtp");

        final var topics = getEnvValueList("CREATE_TOPICS");

        final var fields = TopicManifest.class.getFields();
        final var topicSet = Stream.of(fields)
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> f.getType().equals(Topic.class))
                .map(this::getTopic)
                .filter(Objects::nonNull)
                .map(Topic.class::cast)
                .map(Topic::getTopic)
                .collect(Collectors.toCollection(HashSet::new));
        topicSet.addAll(topicsOldMethod);
        topicSet.addAll(topics);
        return topicSet;
    }

    private static List<String> getEnvValueList(String envName) {
        return Arrays.stream((null != System.getenv(envName) ? System.getenv(envName) : "").split(","))
                .map(String::trim)
                .toList();
    }

    private Object getTopic(Field f) {
        try {
            return f.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @SuppressWarnings("resource")
    private void startWebServer() throws Exception {
        var instance = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        var testScenarioRepository = new DelegatingTestscenarioRepository(instance);
        var gsakRepo = new GsakRepo();
        var journalRepository = JournalRepositoryImpl.getInstance();

        var handler = (HandlerContainer) server.getHandler();

        addRestServices(testScenarioRepository, instance, gsakRepo, journalRepository, handler);

        addWebResources(handler);

        startServer();

        // kjør soap oppsett etter jetty har startet
        addSoapServices(testScenarioRepository);
    }

    private void addRestServices(DelegatingTestscenarioRepository testScenarioRepository, TestscenarioRepositoryImpl instance, GsakRepo gsakRepo, JournalRepositoryImpl journalRepository, HandlerContainer handler) {
        var config = new ApplicationConfigJersey()
                .setup(testScenarioRepository,
                        instance,
                        gsakRepo,
                        kafkaServer.getLocalProducer(),
                        kafkaServer.getKafkaAdminClient(),
                        journalRepository);

        var context = new ServletContextHandler(handler, "/rest");
        var jerseyServlet = new ServletHolder(new ServletContainer(config));
        jerseyServlet.setInitOrder(1);
        context.addServlet(jerseyServlet, "/*");
    }

    private void startLdapServer() {
        var ldapThread = new Thread(ldapServer::start, "LdapServer");
        ldapThread.setDaemon(true);
        ldapThread.start();
    }

    protected void startServer() throws Exception {
        server.start();
        jettyHttpServer = new JettyHttpServer(server, true);
    }

    protected void addSoapServices(TestscenarioBuilderRepository testScenarioRepository) {
        new SoapWebServiceConfig(jettyHttpServer).setup(testScenarioRepository);
    }


    protected void addWebResources(HandlerContainer handler) {
        // Swagger
        var beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[] { "http", "https" });
        beanConfig.setBasePath(API_URI);
        beanConfig.setTitle("VLMock2 - Virtualiserte Tjenester");
        beanConfig.setResourcePackage("no.nav");
        beanConfig.setDescription("REST grensesnitt for VTP.");
        beanConfig.setScan(true);

        var swaggerPath = "/swagger";
        var ctx = new WebAppContext(handler, Resource.newClassPathResource(swaggerPath), swaggerPath);
        ctx.setThrowUnavailableOnStartupException(true);
        ctx.setLogUrlOnStart(true);

        var defaultServlet = new DefaultServlet();
        var servletHolder = new ServletHolder(defaultServlet);
        servletHolder.setInitParameter("dirAllowed", "false");

        ctx.addServlet(servletHolder, swaggerPath);

    }

    protected void setConnectors(Server server) {

        var connectors = new ArrayList<>();

        @SuppressWarnings("resource")
        var httpConnector = new ServerConnector(server);
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
        var keystorePasswProp = System.getProperty("javax.net.ssl.keyStorePassword") != null ? "javax.net.ssl.keyStorePassword" : KEYSTORE_PASSW_PROP;
        System.setProperty(keystoreProp, KeystoreUtils.getKeystoreFilePath());
        System.setProperty(keystorePasswProp, KeystoreUtils.getKeyStorePassword());

        @SuppressWarnings("resource")
        var sslConnector = new ServerConnector(server,
                sslConnectionFactory,
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
