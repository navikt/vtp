package no.nav.engangsstønad.mock;

import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;

import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.sun.net.httpserver.HttpContext;

public class MockServer {

    private static HttpContext context;

    public static void main(String[] args) throws Exception {
        Server server = new Server(7779);
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);
        server.start();
        context = buildHttpContext(server, "/ws");
        publishService("no.nav.tjeneste.virksomhet.aktoer.v2.AktoerServiceMockImpl");
        // access wsdl on http://localhost:7779/ws/Aktoer_v2?wsdl

        // Hack for å få til 2 endepunkter. BTS, hjelp meg gjerne her.
        server = new Server(7780);
        contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);
        server.start();
        context = buildHttpContext(server, "/ws");
        publishService("no.nav.tjeneste.virksomhet.person.v2.PersonServiceMockImpl");
        // access wsdl on http://localhost:7779/ws/Person_v2?wsdl

    }

    public static void publishService(String classname) {
        try {
            Class<?> cl = Class.forName(classname);
            Object ws = cl.newInstance();
            Endpoint endpoint = Endpoint.create(ws);
            endpoint.publish(context);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
