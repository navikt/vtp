package no.nav.foreldrepenger.fpmock2.server;

import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

public class MockServer {
    
    private static final String HTTP_HOST = "0.0.0.0";
    private Server server;
    private JettyHttpServer jettyHttpServer;
    private String host = HTTP_HOST;

    private final int port;

    public static void main(String[] args) throws Exception {
        PropertiesUtils.lagPropertiesFilFraTemplate();
        PropertiesUtils.initProperties();

        MockServer mockServer = new MockServer(Integer.getInteger("server.port"));
        mockServer.start();

    }

    public MockServer(int port) {
        this.port=port;
        server = new Server();
        setConnectors(server);

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

    }

    public void start() throws Exception {
        HandlerContainer handler = (HandlerContainer)server.getHandler();
        addRestServices(handler);
        addWebResources(handler);
        
        startServer();
        
        // kjør soap oppsett etter jetty har startet
        addSoapServices();

    }

    protected void startServer() throws Exception {
        server.start();
        jettyHttpServer = new JettyHttpServer(server, true);
    }

    protected void addSoapServices() {
        new SoapWebServiceConfig(jettyHttpServer).setup();
    }

    protected void addRestServices(HandlerContainer handler) {
        new RestConfig(handler).setup();
    }

    protected void addWebResources(HandlerContainer handlerContainer) {
        
        WebAppContext ctx = new WebAppContext(handlerContainer, Resource.newClassPathResource("/swagger"), "/swagger");
        ctx.setThrowUnavailableOnStartupException(true);
        ctx.setLogUrlOnStart(true);
        
        DefaultServlet defaultServlet = new DefaultServlet();
        ServletHolder servletHolder = new ServletHolder(defaultServlet);
        servletHolder.setInitParameter("dirAllowed", "false");
        
        ctx.addServlet(servletHolder, "/");
    }

    protected void setConnectors(Server server) {
        try (ServerConnector connector = new ServerConnector(server)) {
            connector.setPort(port);
            connector.setHost(host);
            server.setConnectors(new Connector[] { connector });
        }
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
    
    

}
