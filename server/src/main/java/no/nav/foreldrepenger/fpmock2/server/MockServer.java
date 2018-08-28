package no.nav.foreldrepenger.fpmock2.server;

import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;

import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.http.spi.JettyHttpServerProvider;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import no.nav.abac.pdp.PdpMock;
import no.nav.foreldrepenger.fpmock2.server.checks.IsAliveImpl;
import no.nav.foreldrepenger.fpmock2.server.checks.IsReadyImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.Repository;
import no.nav.foreldrepenger.fpmock2.testmodell.Scenarios;
import no.nav.sigrun.SigrunMock;
import no.nav.tjeneste.virksomhet.aktoer.v2.AktoerServiceMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.ArbeidsfordelingMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.tjeneste.virksomhet.arena.meldekort.MeldekortUtbetalingsgrunnlagMockImpl;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.BehandleInngaaendeJournalServiceMockImpl;
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.BehandleOppgaveServiceMockImpl;
import no.nav.tjeneste.virksomhet.behandlesak.v1.BehandleSakServiceMockImpl;
import no.nav.tjeneste.virksomhet.behandlesak.v2.BehandleSak2ServiceMockImpl;
import no.nav.tjeneste.virksomhet.infotrygd.infotrygdberegningsgrunnlag.v1.FinnGrunnlagListeMockImpl;
import no.nav.tjeneste.virksomhet.infotrygd.infotrygdsak.v1.FinnSakListeMockImpl;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.InngaaendeJournalServiceMockImpl;
import no.nav.tjeneste.virksomhet.inntekt.v3.InntektMockImpl;
import no.nav.tjeneste.virksomhet.journal.v2.JournalServiceMockImpl;
import no.nav.tjeneste.virksomhet.medlemskap.v2.MedlemServiceMockImpl;
import no.nav.tjeneste.virksomhet.oppgave.v3.OppgaveServiceMockImpl;
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OppgavebehandlingServiceMockImpl;
import no.nav.tjeneste.virksomhet.organisasjon.v4.OrganisasjonMockImpl;
import no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;
import no.nav.tjeneste.virksomhet.sak.v1.SakServiceMockImpl;

public class MockServer {

    private static final String HTTP_HOST = "0.0.0.0";
    private Server server;
    private JettyHttpServer jettyHttpServer;
    private int port;
    private String host = HTTP_HOST;

    public static void main(String[] args) throws Exception {
        System.setProperty("com.sun.net.httpserver.HttpServerProvider", JettyHttpServerProvider.class.getName());
        MockServer mockServer = new MockServer(8080);
        mockServer.start();
    }

    public MockServer(int port) {
        this.port = port;
    }

    protected void start() throws Exception {
        server = new Server();
        setConnectors(server);
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);
        server.start();
        jettyHttpServer = new JettyHttpServer(server, true);
        publishServices();
    }

    protected void publishServices() {
        Repository repo = Scenarios.getRepository();
        GsakRepo gsakRepo = new GsakRepo();
        
        // TODO NB! disse "access wsdl on..." er tvilsomme, da de de returnerer WSDL/XSD *generert* fra JAXB-klassene, ikke originaldokumentene
        publishService(new AktoerServiceMockImpl(repo), "/aktoer");
        // access wsdl on http://localhost:7999/aktoer?wsdl
        publishService(new SakServiceMockImpl(gsakRepo), "/sak");
        // access wsdl on http://localhost:7999/sak?wsdl
        publishService(new PersonServiceMockImpl(repo), "/person");
        // access wsdl on http://localhost:7999/person?wsdl
        publishService(new JournalServiceMockImpl(repo), "/journal");
        // access wsdl on http://localhost:7999/journal?wsdl
        publishService(new InngaaendeJournalServiceMockImpl(repo), "/inngaaendejournal");
        publishService(new BehandleInngaaendeJournalServiceMockImpl(repo), "/behandleinngaaendejournal");
        publishService(new OppgavebehandlingServiceMockImpl(gsakRepo), "/oppgavebehandling");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl
        publishService(new BehandleOppgaveServiceMockImpl(gsakRepo), "/behandleoppgave");
        // access wsdl on http://localhost:7999/behandleoppgave?wsdl
        publishService(new BehandleSakServiceMockImpl(gsakRepo, repo), "/behandlesak");
        // access wsdl on http://localhost:7999/behandlesak?wsdl
        publishService(new BehandleSak2ServiceMockImpl(gsakRepo, repo), "/behandlesakV2");
        // access wsdl on http://localhost:7999/behandlesakV2?wsdl
        publishService(new PdpMock(), "/asm-pdp/authorize");
        publishService(new FinnSakListeMockImpl(repo), "/infotrygdsak");
        publishService(new FinnGrunnlagListeMockImpl(repo), "/infotrygdberegningsgrunnlag");
        // access wsdl on http://localhost:7999/infotrygdsak?wsdl
        publishService(new MeldekortUtbetalingsgrunnlagMockImpl(repo), "/meldekortutbetalingsgrunnlag");
        publishService(new MedlemServiceMockImpl(repo), "/medlem");
        publishService(new ArbeidsfordelingMockImpl(repo), "/arbeidsfordeling");
        publishService(new InntektMockImpl(), "/inntekt");
        publishService(new OppgaveServiceMockImpl(), "/oppgave");
        publishService(new ArbeidsforholdMockImpl(), "/arbeidsforhold");
        publishService(new OrganisasjonMockImpl(), "/organisasjon");
        publishService(new SigrunMock(), "/api/beregnetskatt");

        publishService(new IsAliveImpl(), "/isAlive");
        publishService(new IsReadyImpl(), "/isReady");
    }

    protected void setConnectors(Server server) {
        try (ServerConnector connector = new ServerConnector(server)) {
            connector.setPort(port);
            connector.setHost(host);
            server.setConnectors(new Connector[] { connector });
        }
    }

    private void publishService(Object ws, String path) {
        JettyHttpContext context = buildHttpContext(path);
        Endpoint endpoint = Endpoint.create(ws);
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
            e.printStackTrace();
        }
        return ctx;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

}
