package no.nav.foreldrepenger.fpmock2.server;

import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;

import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;

import no.nav.abac.pdp.PdpMock;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.aktoer.v2.AktoerServiceMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.ArbeidsfordelingMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.tjeneste.virksomhet.arena.meldekort.MeldekortUtbetalingsgrunnlagMockImpl;
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.BehandleOppgaveServiceMockImpl;
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

    public void setup(TestscenarioBuilderRepository repo) {
        GsakRepo gsakRepo = new GsakRepo();

        // TODO NB! disse "access wsdl on..." er tvilsomme, da de de returnerer WSDL/XSD *generert* fra JAXB-klassene, ikke originaldokumentene
        publishWebService(new AktoerServiceMockImpl(repo), "/aktoerregister/ws/Aktoer/v2");
        // access wsdl on http://localhost:7999/aktoer?wsdl
        publishWebService(new SakServiceMockImpl(gsakRepo), "/nav-gsak-ws/SakV1");
        // access wsdl on http://localhost:7999/sak?wsdl
        publishWebService(new PersonServiceMockImpl(repo), "/tpsws/ws/Person/v3");
        // access wsdl on http://localhost:7999/person?wsdl
        publishWebService(new JournalServiceMockImpl(repo), "/joark/Journal/v2");
        // access wsdl on http://localhost:7999/journal?wsdl
        publishWebService(new InngaaendeJournalServiceMockImpl(repo), "/joark/InngaaendeJournal/v1");
        publishWebService(new OppgavebehandlingServiceMockImpl(gsakRepo), "/nav-gsak-ws/BehandleOppgaveV1");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl
        publishWebService(new BehandleOppgaveServiceMockImpl(gsakRepo), "/nav-gsak-ws/BehandleOppgaveV1");
        // access wsdl on http://localhost:7999/behandleoppgave?wsdl
        publishWebService(new BehandleSak2ServiceMockImpl(gsakRepo, repo), "/nav-gsak-ws/BehandleSakV2");
        // access wsdl on http://localhost:7999/behandlesakV2?wsdl
        publishWebService(new FinnSakListeMockImpl(repo), "/infotrygd-ws/InfotrygdSak/v1");
        publishWebService(new FinnGrunnlagListeMockImpl(repo), "/infotrygd-ws/InfotrygdBeregningsgrunnlag/v1");
        // access wsdl on http://localhost:7999/infotrygdsak?wsdl
        publishWebService(new MeldekortUtbetalingsgrunnlagMockImpl(repo), "/ail_ws/MeldekortUtbetalingsgrunnlag_v1");
        publishWebService(new MedlemServiceMockImpl(repo), "/medl2/ws/Medlemskap/v2");
        publishWebService(new ArbeidsfordelingMockImpl(repo), "/norg2/ws/Arbeidsfordeling/v1");
        publishWebService(new InntektMockImpl(repo), "/inntektskomponenten-ws/inntekt/v3/Inntekt");
        publishWebService(new OppgaveServiceMockImpl(), "/nav-gsak-ws/OppgaveV3");
        publishWebService(new ArbeidsforholdMockImpl(), "/aareg-core/ArbeidsforholdService/v3");
        publishWebService(new OrganisasjonMockImpl(), "/ereg/ws/OrganisasjonService/v4");
        publishWebService(new PdpMock(), "/asm-pdp/authorize");
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
