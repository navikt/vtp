package no.nav.foreldrepenger.vtp.server;

import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceFeature;

import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;

import no.nav.foreldrepenger.vtp.server.ws.SecurityTokenServiceMockImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingServiceMockImpl;
import no.nav.system.os.eksponering.SimulerFpServiceMockImpl;
import no.nav.tjeneste.virksomhet.aktoer.v2.AktoerServiceMockImpl;
import no.nav.tjeneste.virksomhet.arena.meldekort.MeldekortUtbetalingsgrunnlagMockImpl;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.DokumentproduksjonV2MockImpl;
import no.nav.tjeneste.virksomhet.journal.v3.JournalV3ServiceMockImpl;
import no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkServiceMockImpl;
import no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl;


public class SoapWebServiceConfig {

    private JettyHttpServer jettyHttpServer;

    public SoapWebServiceConfig(JettyHttpServer jettyHttpServer) {
        this.jettyHttpServer = jettyHttpServer;
    }

    public void setup(TestscenarioBuilderRepository repo, JournalRepository journalRepository) {

        System.setProperty("javax.xml.soap.SAAJMetaFactory", "com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl");


        publishWebService(new SecurityTokenServiceMockImpl(), "/soap/SecurityTokenServiceProvider/");

        publishWebService(new AktoerServiceMockImpl(repo), "/soap/aktoerregister/ws/Aktoer/v2");
        publishWebService(new PersonServiceMockImpl(repo), "/soap/tpsws/ws/Person/v3");
        publishWebService(new JournalV3ServiceMockImpl(journalRepository), "/soap/joark/Journal/v3");
        publishWebService(new KodeverkServiceMockImpl(repo), "/soap/kodeverk/ws/Kodeverk/v2");

        publishWebService(new DokumentproduksjonV2MockImpl(journalRepository), "/soap/dokprod/ws/dokumentproduksjon/v2");
        publishWebService(new MeldekortUtbetalingsgrunnlagMockImpl(repo), "/soap/ail_ws/MeldekortUtbetalingsgrunnlag_v1");
        publishWebService(new SimulerFpServiceMockImpl(repo), "/soap/cics/services/oppdragService");
        publishWebService(new TilbakekrevingServiceMockImpl(), "/soap/tilbakekreving/services/tilbakekrevingService");
    }

    private void publishWebService(Object ws, String path, WebServiceFeature... features ) {
        // binder sammen jetty, tjeneste til JAX-WS Endpoint
        JettyHttpContext context = buildHttpContext(path);
        Endpoint endpoint = Endpoint.create(ws, features);
        endpoint.publish(context);
    }

    private JettyHttpContext buildHttpContext(String contextString) {
        JettyHttpContext ctx = (JettyHttpContext) jettyHttpServer.createContext(contextString);
        try {
            Method method = JettyHttpContext.class.getDeclaredMethod("getJettyContextHandler");
            method.setAccessible(true);
            HttpSpiContextHandler contextHandler = (HttpSpiContextHandler) method.invoke(ctx);
            contextHandler.start();
        } catch (Exception e) {
            throw new IllegalStateException("Kunne ikke starte server", e);
        }
        return ctx;
    }
}
