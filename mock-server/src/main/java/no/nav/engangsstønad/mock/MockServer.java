package no.nav.engangsstønad.mock;

import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;

import no.nav.abac.pdp.PdpMock;
import no.nav.modig.testcertificates.TestCertificates;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.BehandleInngaaendeJournalServiceMockImpl;
import no.nav.tjeneste.virksomhet.behandlesak.v1.BehandleSakServiceMockImpl;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.FinnSakListeMockImpl;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.InngaaendeJournalServiceMockImpl;
import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.sun.net.httpserver.HttpContext;

import no.nav.tjeneste.virksomhet.aktoer.v2.AktoerServiceMockImpl;
import no.nav.tjeneste.virksomhet.journal.v2.JournalServiceMockImpl;
import no.nav.tjeneste.virksomhet.person.v2.PersonServiceMockImpl;
import no.nav.tjeneste.virksomhet.sak.v1.SakServiceMockImpl;
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OppgavebehandlingServiceMockImpl;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class MockServer {

    private static final int HTTP_PORT = 7999;
    private static final int HTTPS_PORT = 8443;
    private static Server server;

    public static void main(String[] args) throws Exception {

        server = new Server();
        setConnectors();
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();

        server.setHandler(contextHandlerCollection);
        server.start();
        //TODO NB! disse "access wsdl on..." er tvilsomme, da de de returnerer WSDL/XSD *generert* fra JAXB-klassene, ikke originaldokumentene
        publishService(AktoerServiceMockImpl.class, "/aktoer");
        // access wsdl on http://localhost:7999/aktoer?wsdl
        publishService(SakServiceMockImpl.class, "/sak");
        // access wsdl on http://localhost:7999/sak?wsdl
        publishService(PersonServiceMockImpl.class, "/person");
        // access wsdl on http://localhost:7999/person?wsdl
        publishService(JournalServiceMockImpl.class, "/journal");
        // access wsdl on http://localhost:7999/journal?wsdl
        publishService(InngaaendeJournalServiceMockImpl.class, "/inngaaendejournal");
        publishService(BehandleInngaaendeJournalServiceMockImpl.class, "/behandleinngaaendejournal");
        publishService(OppgavebehandlingServiceMockImpl.class, "/oppgavebehandling");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl
        publishService(BehandleSakServiceMockImpl.class, "/behandlesak");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl
        publishService(PdpMock.class,"/asm-pdp/authorize");
        publishService(FinnSakListeMockImpl.class, "/infotrygdsak");


    }

    private static void setConnectors() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(HTTP_PORT);
        HttpConfiguration https = new HttpConfiguration();

        TestCertificates.setupKeyAndTrustStore();

        https.addCustomizer(new SecureRequestCustomizer());

        boolean useModigCerts = "true".equalsIgnoreCase(System.getenv("modigcerts"));
        String keystorePath;
        SslContextFactory sslContextFactory;
        if (useModigCerts) {
            keystorePath = MockServer.class.getClassLoader().getResource("no/nav/modig/testcertificates/keystore.jks").toExternalForm();
        } else {
            keystorePath = System.getenv("mock_keystore");
        }
        sslContextFactory = new SslContextFactory(keystorePath);

        sslContextFactory.setKeyStorePassword("devillokeystore1234");
        sslContextFactory.setKeyManagerPassword("devillokeystore1234");
        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
        sslConnector.setPort(HTTPS_PORT);
        server.setConnectors(new Connector[] { connector, sslConnector });
    }

    private static void publishService(Class<?> clazz, String path) {
        HttpContext context = buildHttpContext(server, path);
        try {
            Object ws = clazz.newInstance();
            Endpoint endpoint = Endpoint.create(ws);
            endpoint.publish(context);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static HttpContext buildHttpContext(Server server, String contextString) {
        JettyHttpServer jettyHttpServer = new JettyHttpServer(server, true);
        JettyHttpContext ctx = (JettyHttpContext) jettyHttpServer.createContext(contextString);
        try {
            Method method = JettyHttpContext.class.getDeclaredMethod("getJettyContextHandler");
            method.setAccessible(true);
            HttpSpiContextHandler contextHandler = (HttpSpiContextHandler) method.invoke(ctx);
            contextHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ctx;
    }}
