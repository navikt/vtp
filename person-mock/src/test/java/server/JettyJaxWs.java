package server;

import com.sun.net.httpserver.HttpContext;
import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.http.spi.JettyHttpServerProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class JettyJaxWs {

    public static void main(String[] args) throws Exception {
        Server server = new Server(7777);
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);
        server.start();
        HttpContext context = buildNew(server, "/ws");
        MyWebService ws = new MyWebService();
        Endpoint endpoint = Endpoint.create(ws);
        endpoint.publish(context);
        // access wsdl on http://localhost:7777/ws/MyWebService?wsdl
    }

    @WebService
    public static class MyWebService {

        public String hello(String s) {
            return "hi " + s;
        }
    }

    public static HttpContext buildOrig(Server server, String contextString) throws Exception {
        JettyHttpServerProvider.setServer(server);
        return new JettyHttpServerProvider().createHttpServer(new InetSocketAddress(7777), 5).createContext(contextString);
    }

    public static HttpContext buildNew(Server server, String contextString) {
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
    }
}
