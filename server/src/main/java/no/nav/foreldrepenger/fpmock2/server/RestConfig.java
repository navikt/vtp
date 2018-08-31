package no.nav.foreldrepenger.fpmock2.server;

import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

public class RestConfig {

    private final HandlerContainer handler;

    public RestConfig(HandlerContainer handler) {
        this.handler = handler;
    }

    public void setup() {
        // Setup RESTEasy's HttpServletDispatcher at "/api/*".
        final ServletContextHandler context = new ServletContextHandler(handler, "/api");
        final ServletHolder restEasyServlet = new ServletHolder(new HttpServletDispatcher());
        restEasyServlet.setInitParameter("resteasy.servlet.mapping.prefix", "/");
        restEasyServlet.setInitParameter("javax.ws.rs.Application", ApplicationConfig.class.getName());
        context.addServlet(restEasyServlet, "/*");
    }

}
