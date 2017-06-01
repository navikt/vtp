package no.nav.engangsstønad.mock;

import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;

import no.nav.tjeneste.virksomhet.behandlesak.v1.BehandleSakServiceMockImpl;
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

    private static Server server;

    public static void main(String[] args) throws Exception {
        server = new Server();
        setConnectors();
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);
        server.start();
        //TODO! disse "access wsdl on..." er tvilsomme, da de de returnerer WSDL/XSD *generert* fra JAXB-klassene, ikke originaldokumentene
        publishService(AktoerServiceMockImpl.class, "/aktoer");
        // access wsdl on http://localhost:7999/aktoer?wsdl
        publishService(SakServiceMockImpl.class, "/sak");
        // access wsdl on http://localhost:7999/sak?wsdl
        publishService(PersonServiceMockImpl.class, "/person");
        // access wsdl on http://localhost:7999/person?wsdl
        publishService(JournalServiceMockImpl.class, "/journal");
        publishService(InngaaendeJournalServiceMockImpl.class, "/inngaaendejournal");
        // access wsdl on http://localhost:7999/journal?wsdl
        publishService(OppgavebehandlingServiceMockImpl.class, "/oppgavebehandling");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl
        publishService(BehandleSakServiceMockImpl.class, "/behandlesak");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl

    }

    private static void setConnectors() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(7999);
        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        SslContextFactory sslContextFactory = new SslContextFactory("keystore.jks");
        sslContextFactory.setKeyStorePassword("secret99");
        sslContextFactory.setKeyManagerPassword("secret99");
        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
        sslConnector.setPort(7998);
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
