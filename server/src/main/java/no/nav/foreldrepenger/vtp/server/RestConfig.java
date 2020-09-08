package no.nav.foreldrepenger.vtp.server;

import java.util.Map;

import javax.servlet.ServletContextEvent;

import org.apache.kafka.clients.admin.AdminClient;
import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;

import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.DelegatingTestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;

public class RestConfig {

    private final HandlerContainer handler;

    public RestConfig(HandlerContainer handler) {
        this.handler = handler;
    }

    public void setup(DelegatingTestscenarioBuilderRepository testScenarioRepository,
                      GsakRepo gsakRepo,
                      LocalKafkaProducer localKafkaProducer,
                      AdminClient kafkaAdminClient,
                      JournalRepository journalRepository) {
        // Setup RESTEasy's HttpServletDispatcher at "/api/*".
        final ServletContextHandler context = new ServletContextHandler(handler, "/rest");

        // tilgjengeligjør disse for direkte injeksjon i REST tjenester uten CDI via @Context parameter
        // ref https://stackoverflow.com/questions/21126812/bootstrapping-jax-rs-resteasy
        context.addEventListener(new ResteasyBootstrap(){
            @Override
            public void contextInitialized(ServletContextEvent event) {
                super.contextInitialized(event);
                @SuppressWarnings("rawtypes")
                Map<Class, Object> defaultContextObjects = deployment.getDispatcher().getDefaultContextObjects();
                defaultContextObjects.put(TestscenarioRepository.class, testScenarioRepository);
                defaultContextObjects.put(TestscenarioBuilderRepository.class, testScenarioRepository);
                defaultContextObjects.put(JournalRepository.class, journalRepository);
                defaultContextObjects.put(GsakRepo.class,gsakRepo);
                defaultContextObjects.put(LocalKafkaProducer.class, localKafkaProducer);
                defaultContextObjects.put(AdminClient.class, kafkaAdminClient);
            }
        });

        HttpServletDispatcher servlet = new HttpServletDispatcher();
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.setInitParameter("resteasy.servlet.mapping.prefix", "/");
        servletHolder.setInitParameter("javax.ws.rs.Application", ApplicationConfig.class.getName());
        context.addServlet(servletHolder, "/*");
    }

}
