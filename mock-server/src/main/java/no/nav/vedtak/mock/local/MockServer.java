package no.nav.vedtak.mock.local;

import java.io.File;
import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;

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
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.sun.net.httpserver.HttpContext;

import no.nav.abac.pdp.PdpMock;
import no.nav.modig.testcertificates.TestCertificates;
import no.nav.sigrun.SigrunMock;
import no.nav.tjeneste.virksomhet.aktoer.v2.AktoerServiceMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.ArbeidsfordelingMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.BehandleInngaaendeJournalServiceMockImpl;
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.BehandleOppgaveServiceMockImpl;
import no.nav.tjeneste.virksomhet.behandlesak.v1.BehandleSakServiceMockImpl;
import no.nav.tjeneste.virksomhet.behandlesak.v2.BehandleSak2ServiceMockImpl;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.FinnGrunnlagListeMockImpl;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.FinnSakListeMockImpl;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.InngaaendeJournalServiceMockImpl;
import no.nav.tjeneste.virksomhet.inntekt.v3.InntektMockImpl;
import no.nav.tjeneste.virksomhet.journal.v2.JournalServiceMockImpl;
import no.nav.tjeneste.virksomhet.medlemskap.v2.MedlemServiceMockImpl;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.MeldekortUtbetalingsgrunnlagMockImpl;
import no.nav.tjeneste.virksomhet.oppgave.v3.OppgaveServiceMockImpl;
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OppgavebehandlingServiceMockImpl;
import no.nav.tjeneste.virksomhet.organisasjon.v4.OrganisasjonMockImpl;
import no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl;
import no.nav.tjeneste.virksomhet.sak.v1.SakServiceMockImpl;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.HentYtelseskontraktListeMockImpl;
import no.nav.vedtak.mock.local.checks.IsAliveImpl;
import no.nav.vedtak.mock.local.checks.IsReadyImpl;

@SuppressWarnings("restriction")
public class MockServer {

    private static final int HTTP_PORT = 8080;
    private static final int HTTPS_PORT = 8088;
    private static final String HTTP_HOST = "0.0.0.0";
    private static Server server;

    public static void main(String[] args) throws Exception {
        File logDir =new File("logs");
        logDir.mkdirs();
        System.setProperty("APP_LOG_HOME", logDir.getPath());
        
        server = new Server();
        setConnectors();
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();

        server.setHandler(contextHandlerCollection);
        server.start();

        publishServices();
    }

    private static void publishServices() {
        // TODO NB! disse "access wsdl on..." er tvilsomme, da de de returnerer WSDL/XSD *generert* fra JAXB-klassene, ikke originaldokumentene
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
        publishService(BehandleOppgaveServiceMockImpl.class, "/behandleoppgave");
        // access wsdl on http://localhost:7999/behandleoppgave?wsdl
        publishService(BehandleSakServiceMockImpl.class, "/behandlesak");
        // access wsdl on http://localhost:7999/behandlesak?wsdl
        publishService(BehandleSak2ServiceMockImpl.class, "/behandlesakV2");
        // access wsdl on http://localhost:7999/behandlesakV2?wsdl
        publishService(PdpMock.class, "/asm-pdp/authorize");
        publishService(FinnSakListeMockImpl.class, "/infotrygdsak");
        publishService(FinnGrunnlagListeMockImpl.class, "/infotrygdberegningsgrunnlag");
        // access wsdl on http://localhost:7999/infotrygdsak?wsdl
        publishService(HentYtelseskontraktListeMockImpl.class, "/ytelseskontrakt");
        // access wsdl on http://localhost:7999/ytelseskontrakt?wsdl
        publishService(MeldekortUtbetalingsgrunnlagMockImpl.class, "/meldekortutbetalingsgrunnlag");
        publishService(MedlemServiceMockImpl.class, "/medlem");
        publishService(ArbeidsfordelingMockImpl.class, "/arbeidsfordeling");
        publishService(InntektMockImpl.class, "/inntekt");
        publishService(OppgaveServiceMockImpl.class, "/oppgave");
        publishService(ArbeidsforholdMockImpl.class, "/arbeidsforhold");
        publishService(OrganisasjonMockImpl.class, "/organisasjon");
        publishService(SigrunMock.class, "/api/beregnetskatt");

        publishService(IsAliveImpl.class, "/isAlive");
        publishService(IsReadyImpl.class, "/isReady");
    }

    private static void setConnectors() {
        try (ServerConnector connector = new ServerConnector(server)) {
            connector.setPort(HTTP_PORT);
            connector.setHost(HTTP_HOST);
            HttpConfiguration https = new HttpConfiguration();

            TestCertificates.setupKeyAndTrustStore();

            https.addCustomizer(new SecureRequestCustomizer());

            boolean useModigCerts = "true".equalsIgnoreCase(System.getenv("modigcerts"));
            String keystorePath;
            SslContextFactory sslContextFactory;

            if (true) {
                keystorePath = MockServer.class.getClassLoader().getResource("no/nav/modig/testcertificates/keystore.jks").toExternalForm();
            } else {
                keystorePath = System.getenv("mock_keystore");
            }
            sslContextFactory = new SslContextFactory(keystorePath);
            sslContextFactory.setTrustAll(true);
            sslContextFactory.setKeyStorePassword("devillokeystore1234");
            sslContextFactory.setKeyManagerPassword("devillokeystore1234");

            try (ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https))) {
                sslConnector.setPort(HTTPS_PORT);
                sslConnector.setHost(HTTP_HOST);
                server.setConnectors(new Connector[] { connector, sslConnector });
            }
        }
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
    }
}
