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

    private static Server server;

    public static void main(String[] args) throws Exception {
        server = new Server(7779);
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);
        server.start();
        publishService("no.nav.tjeneste.virksomhet.aktoer.v2.AktoerServiceMockImpl", "/aktoer");
        // access wsdl on http://localhost:7779/ws/Aktoer_v2?wsdl
        publishService("no.nav.tjeneste.virksomhet.sak.v1.SakServiceMockImpl", "/sak");

        publishService("no.nav.tjeneste.virksomhet.person.v2.PersonServiceMockImpl", "/person");
        // access wsdl on http://localhost:7779/ws/Person_v2?wsdl

    }

    public static void publishService(String classname, String path) {
        HttpContext context = buildHttpContext(server, path);
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
