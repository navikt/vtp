package no.nav.foreldrepenger.vtp.server;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.kafka.clients.admin.AdminClient;
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
import no.nav.foreldrepenger.vtp.felles.KeystoreUtils;
import no.nav.foreldrepenger.vtp.felles.PropertiesUtils;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaServer;
import no.nav.foreldrepenger.vtp.ldap.LdapServer;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioBuilderRepository;
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
        int kafkaBrokerPort = Integer.parseInt(System.getProperty("kafkaBrokerPort", "9092"));
        int zookeeperPort = Integer.parseInt(System.getProperty("zookeeper.port", "2181"));
        kafkaServer = new LocalKafkaServer(zookeeperPort, kafkaBrokerPort, getBootstrapTopics());

    }

    public static void main(String[] args) throws Exception {
        PropertiesUtils.initProperties();
        MockServer mockServer = new MockServer();
        mockServer.start();
    }

    public void start() throws Exception {

        if(!tjenesteDisabled(VTPTjeneste.LDAP)){
            startLdapServer();
        }
        if(!tjenesteDisabled(VTPTjeneste.KAFKA)){
            startKafkaServer();
        }
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
        final List<String> topicsOldMethod = List.of("privat-foreldrepenger-mottatBehandling-fpsak",
                "privat-foreldrepenger-aksjonspunkthendelse-fpsak",
                "privat-foreldrepenger-fprisk-utfor-t4",
                "privat-foreldrepenger-tilkjentytelse-v1-local",
                "privat-foreldrepenger-dokumenthendelse-vtp",
                "privat-foreldrepenger-historikkinnslag-vtp");

        final List<String> topics = getEnvValueList("CREATE_TOPICS");

        final Field[] fields = TopicManifest.class.getFields();
        final HashSet<String> topicSet = Stream.of(fields)
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
        return Arrays.asList((null != System.getenv(envName) ? System.getenv(envName) : "").split(","))
                .stream()
                .map(s -> s.trim())
                .collect(Collectors.toList());
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
        HandlerContainer handler = (HandlerContainer) server.getHandler();

        DelegatingTestscenarioRepository testScenarioRepository = new DelegatingTestscenarioRepository(
                TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance()));
        GsakRepo gsakRepo = new GsakRepo();
        JournalRepository journalRepository = JournalRepositoryImpl.getInstance();

        addRestServices(handler,
                testScenarioRepository,
                gsakRepo,
                kafkaServer.getLocalProducer(),
                kafkaServer.getKafkaAdminClient(),
                journalRepository);


        addWebResources(handler);

        startServer();

        // kjør soap oppsett etter jetty har startet
        if(!tjenesteDisabled(VTPTjeneste.SOAP)) {
            addSoapServices(testScenarioRepository, journalRepository, gsakRepo);
        }
    }

    private void startLdapServer() {
        Thread ldapThread = new Thread(ldapServer::start, "LdapServer");
        ldapThread.setDaemon(true);
        ldapThread.start();
    }

    protected void startServer() throws Exception {
        server.start();
        jettyHttpServer = new JettyHttpServer(server, true);
    }

    protected void addSoapServices(TestscenarioBuilderRepository testScenarioRepository,
                                   JournalRepository journalRepository,
                                   GsakRepo gsakRepo) {
        new SoapWebServiceConfig(jettyHttpServer).setup(testScenarioRepository, journalRepository, gsakRepo);
    }

    protected void addRestServices(HandlerContainer handler, DelegatingTestscenarioBuilderRepository testScenarioRepository,
                                   GsakRepo gsakRepo, LocalKafkaProducer localKafkaProducer, AdminClient kafkaAdminClient,
                                   JournalRepository journalRepository) {
        new RestConfig(handler).setup(testScenarioRepository, gsakRepo, localKafkaProducer, kafkaAdminClient,journalRepository);
    }

    protected void addWebResources(HandlerContainer handlerContainer) {
        @SuppressWarnings("resource")
        WebAppContext ctx = new WebAppContext(handlerContainer, Resource.newClassPathResource("/swagger"), "/swagger");


        ctx.setThrowUnavailableOnStartupException(true);
        ctx.setLogUrlOnStart(true);

        DefaultServlet defaultServlet = new DefaultServlet();

        ServletHolder servletHolder = new ServletHolder(defaultServlet);
        servletHolder.setInitParameter("dirAllowed", "false");

        ctx.addServlet(servletHolder, "/swagger");

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
        SslContextFactory sslContextFactory = new SslContextFactory.Server();

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
        server.setConnectors(connectors.toArray(new Connector[0]));
    }

    private boolean tjenesteDisabled(VTPTjeneste tjeneste){
        String miljøVariabel = tjeneste.getNavn().concat("_DISABLE");
        if(System.getenv(miljøVariabel) != null && System.getenv(miljøVariabel).equalsIgnoreCase("true")){
            LOG.info("Tjeneste er disabled: {}", tjeneste.getNavn());
            return true;
        }
        return false;
    }

    private enum VTPTjeneste {
        SOAP("VTP_SOAP"),
        REST("VTP_REST"),
        KAFKA("VTP_KAFKA"),
        LDAP("VTP_LDAP");

        private String navn;

        String getNavn(){
            return navn;
        }

        VTPTjeneste(String navn){
            this.navn = navn;
        }
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
