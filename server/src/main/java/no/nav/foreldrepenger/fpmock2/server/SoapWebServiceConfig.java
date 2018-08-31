package no.nav.foreldrepenger.fpmock2.server;

import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;

import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;

import no.nav.abac.pdp.PdpMock;
import no.nav.foreldrepenger.fpmock2.testmodell.Repository;
import no.nav.foreldrepenger.fpmock2.testmodell.Scenarios;
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

public class SoapWebServiceConfig {
    private JettyHttpServer jettyHttpServer;

    public SoapWebServiceConfig(JettyHttpServer jettyHttpServer) {
        this.jettyHttpServer = jettyHttpServer;
    }

    public void setup() {
        Repository repo = Scenarios.getRepository();
        GsakRepo gsakRepo = new GsakRepo();

        // TODO NB! disse "access wsdl on..." er tvilsomme, da de de returnerer WSDL/XSD *generert* fra JAXB-klassene, ikke originaldokumentene
        publishWebService(new AktoerServiceMockImpl(repo), "/aktoer");
        // access wsdl on http://localhost:7999/aktoer?wsdl
        publishWebService(new SakServiceMockImpl(gsakRepo), "/sak");
        // access wsdl on http://localhost:7999/sak?wsdl
        publishWebService(new PersonServiceMockImpl(repo), "/person");
        // access wsdl on http://localhost:7999/person?wsdl
        publishWebService(new JournalServiceMockImpl(repo), "/journal");
        // access wsdl on http://localhost:7999/journal?wsdl
        publishWebService(new InngaaendeJournalServiceMockImpl(repo), "/inngaaendejournal");
        publishWebService(new BehandleInngaaendeJournalServiceMockImpl(repo), "/behandleinngaaendejournal");
        publishWebService(new OppgavebehandlingServiceMockImpl(gsakRepo), "/oppgavebehandling");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl
        publishWebService(new BehandleOppgaveServiceMockImpl(gsakRepo), "/behandleoppgave");
        // access wsdl on http://localhost:7999/behandleoppgave?wsdl
        publishWebService(new BehandleSakServiceMockImpl(gsakRepo, repo), "/behandlesak");
        // access wsdl on http://localhost:7999/behandlesak?wsdl
        publishWebService(new BehandleSak2ServiceMockImpl(gsakRepo, repo), "/behandlesakV2");
        // access wsdl on http://localhost:7999/behandlesakV2?wsdl
        publishWebService(new PdpMock(), "/asm-pdp/authorize");
        publishWebService(new FinnSakListeMockImpl(repo), "/infotrygdsak");
        publishWebService(new FinnGrunnlagListeMockImpl(repo), "/infotrygdberegningsgrunnlag");
        // access wsdl on http://localhost:7999/infotrygdsak?wsdl
        publishWebService(new MeldekortUtbetalingsgrunnlagMockImpl(repo), "/meldekortutbetalingsgrunnlag");
        publishWebService(new MedlemServiceMockImpl(repo), "/medlem");
        publishWebService(new ArbeidsfordelingMockImpl(repo), "/arbeidsfordeling");
        publishWebService(new InntektMockImpl(), "/inntekt");
        publishWebService(new OppgaveServiceMockImpl(), "/oppgave");
        publishWebService(new ArbeidsforholdMockImpl(), "/arbeidsforhold");
        publishWebService(new OrganisasjonMockImpl(), "/organisasjon");

    }

    private void publishWebService(Object ws, String path) {
        // binder sammen jetty, tjeneste til JAX-WS Endpoint
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
            throw new IllegalStateException("Kunne ikke starte server", e);
        }
        return ctx;
    }
}
