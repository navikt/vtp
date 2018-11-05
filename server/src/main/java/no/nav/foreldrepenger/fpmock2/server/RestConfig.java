package no.nav.foreldrepenger.fpmock2.server;

import java.util.Map;

import javax.servlet.ServletContextEvent;

import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioBuilderRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.DelegatingTestscenarioTemplateRepository;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;

public class RestConfig {

    private final HandlerContainer handler;
    private DelegatingTestscenarioTemplateRepository templateRepository;

    public RestConfig(HandlerContainer handler, DelegatingTestscenarioTemplateRepository templateRepository) {
        this.handler = handler;
        this.templateRepository = templateRepository;
    }

    public void setup(DelegatingTestscenarioBuilderRepository testScenarioRepository, GsakRepo gsakRepo) {
        // Setup RESTEasy's HttpServletDispatcher at "/api/*".
        final ServletContextHandler context = new ServletContextHandler(handler, "/");
        
        // tilgjengeligjør disse for direkte injeksjon i REST tjenester uten CDI via @Context parameter
        // ref https://stackoverflow.com/questions/21126812/bootstrapping-jax-rs-resteasy
        context.addEventListener(new ResteasyBootstrap(){
            @Override
            public void contextInitialized(ServletContextEvent event) {
                super.contextInitialized(event);
                @SuppressWarnings("rawtypes")
                Map<Class, Object> defaultContextObjects = deployment.getDispatcher().getDefaultContextObjects();
                defaultContextObjects.put(TestscenarioRepository.class, testScenarioRepository);
                defaultContextObjects.put(TestscenarioTemplateRepository.class, templateRepository);
                defaultContextObjects.put(GsakRepo.class,gsakRepo);
            }
        });

        HttpServletDispatcher servlet = new HttpServletDispatcher();
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.setInitParameter("resteasy.servlet.mapping.prefix", "/");
        servletHolder.setInitParameter("javax.ws.rs.Application", ApplicationConfig.class.getName());
        context.addServlet(servletHolder, "/*");
    }

}
