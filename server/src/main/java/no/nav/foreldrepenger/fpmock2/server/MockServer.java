package no.nav.foreldrepenger.fpmock2.server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import no.nav.foreldrepenger.fpmock2.kafkaembedded.LocalKafkaProducer;
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

import no.nav.familie.topic.Topic;
import no.nav.familie.topic.TopicManifest;
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
    private static final Logger LOG = LoggerFactory.getLogger(MockServer.class);

    private static final String TRUSTSTORE_PASSW_PROP = "javax.net.ssl.trustStorePassword";
    private static final String TRUSTSTORE_PATH_PROP = "javax.net.ssl.trustStore";
    private static final String KEYSTORE_PASSW_PROP = "no.nav.modig.security.appcert.password";
    private static final String KEYSTORE_PATH_PROP = "no.nav.modig.security.appcert.keystore";

    private final int port;
    private final LdapServer ldapServer;
    private Server server;
    private JettyHttpServer jettyHttpServer;
    private String host = HTTP_HOST;

    public MockServer() throws Exception {
        LOG.info("Dummyprop er satt til: " + System.getenv("DUMMYPROP"));
        this.port = Integer.parseInt(System.getProperty("autotest.vtp.port", SERVER_PORT));

        // Bør denne settes fra ENV_VAR?
        System.setProperty("server.url", "https://localhost:" + getSslPort());

        server = new Server();
        setConnectors(server);

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

        ldapServer = new LdapServer(new File(KeystoreUtils.getKeystoreFilePath()), KeystoreUtils.getKeyStorePassword().toCharArray());

    }

    public static void main(String[] args) throws Exception {
        PropertiesUtils.initProperties();
        MockServer mockServer = new MockServer();
        mockServer.start();
    }

    public void start() throws Exception {
        startKafkaServer();

        startLdapServer();
        startWebServer();
    }

    private void startKafkaServer() {

        // Properties.setProp(JaasUtils.JAVA_LOGIN_CONFIG_PARAM, "KafkaServerJaas.conf");

        Integer kafkaBrokerPort = Integer.parseInt(System.getProperty("kafkaBrokerPort", "9092"));
        Integer zookeeperPort = Integer.parseInt(System.getProperty("zookeeper.port", "2181"));
        LocalKafkaServer.startKafka(zookeeperPort, kafkaBrokerPort, getBootstrapTopics());
    }

    private Set<String> getBootstrapTopics() {
        Field[] fields = TopicManifest.class.getFields();
        HashSet<String> topicSet = Stream.of(fields)
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> f.getType().equals(Topic.class))
                .map(this::getTopic)
                .filter(Objects::nonNull)
                .map(Topic.class::cast)
                .map(Topic::getTopic)
                .collect(Collectors.toCollection(HashSet::new));
        topicSet.addAll(List.of("privat-foreldrepenger-mottatBehandling-fpsak",
                "privat-foreldrepenger-aksjonspunkthendelse-fpsak",
                "privat-foreldrepenger-fprisk-utfor-t4",
                "privat-foreldrepenger-tilkjentytelse-v1-local"));
        return topicSet;
    }

    private Object getTopic(Field f) {
        try {
            return f.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private void startWebServer() throws IOException, Exception {
        HandlerContainer handler = (HandlerContainer) server.getHandler();

        TestscenarioTemplateRepositoryImpl templateRepositoryImpl = TestscenarioTemplateRepositoryImpl.getInstance();
        templateRepositoryImpl.load();

        DelegatingTestscenarioTemplateRepository templateRepository = new DelegatingTestscenarioTemplateRepository(templateRepositoryImpl);
        DelegatingTestscenarioRepository testScenarioRepository = new DelegatingTestscenarioRepository(TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        GsakRepo gsakRepo = new GsakRepo();
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();

        addRestServices(handler, testScenarioRepository, templateRepository, gsakRepo, LocalKafkaServer.getLocalProducer());


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
                                   GsakRepo gsakRepo, LocalKafkaProducer localKafkaProducer) {
        new RestConfig(handler, templateRepository).setup(testScenarioRepository, gsakRepo, localKafkaProducer);
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
