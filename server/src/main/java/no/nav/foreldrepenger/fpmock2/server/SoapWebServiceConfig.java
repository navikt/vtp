package no.nav.foreldrepenger.fpmock2.server;

import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceFeature;

import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;

import no.nav.foreldrepenger.fpmock2.server.ws.SecurityTokenServiceMockImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.system.os.eksponering.SimulerFpServiceMockImpl;
import no.nav.tjeneste.virksomhet.aktoer.v2.AktoerServiceMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.ArbeidsfordelingMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.tjeneste.virksomhet.arena.meldekort.MeldekortUtbetalingsgrunnlagMockImpl;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.BehandleJournalV3ServiceMockImpl;
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.BehandleOppgaveServiceMockImpl;
import no.nav.tjeneste.virksomhet.behandlesak.v2.BehandleSak2ServiceMockImpl;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.DokumentproduksjonV2MockImpl;
import no.nav.tjeneste.virksomhet.infotrygd.infotrygdberegningsgrunnlag.v1.FinnGrunnlagListeMockImpl;
import no.nav.tjeneste.virksomhet.infotrygd.infotrygdsak.v1.FinnSakListeMockImpl;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.InngaaendeJournalServiceMockImpl;
import no.nav.tjeneste.virksomhet.inntekt.v3.InntektMockImpl;
import no.nav.tjeneste.virksomhet.journal.v2.JournalV2ServiceMockImpl;
import no.nav.tjeneste.virksomhet.journal.v3.JournalV3ServiceMockImpl;
import no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkServiceMockImpl;
import no.nav.tjeneste.virksomhet.medlemskap.v2.MedlemServiceMockImpl;
import no.nav.tjeneste.virksomhet.oppgave.v3.OppgaveServiceMockImpl;
import no.nav.tjeneste.virksomhet.organisasjon.v4.OrganisasjonMockImpl;
import no.nav.tjeneste.virksomhet.person.v3.PersonServiceMockImpl;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;
import no.nav.tjeneste.virksomhet.sak.v1.SakServiceMockImpl;

public class SoapWebServiceConfig {
    
    private JettyHttpServer jettyHttpServer;

    public SoapWebServiceConfig(JettyHttpServer jettyHttpServer) {
        this.jettyHttpServer = jettyHttpServer;
    }

    public void setup(TestscenarioBuilderRepository repo, JournalRepository journalRepository, GsakRepo gsakRepo) {

        publishWebService(new SecurityTokenServiceMockImpl(), "/soap/SecurityTokenServiceProvider/");
        
        // TODO NB! disse "access wsdl on..." er tvilsomme, da de de returnerer WSDL/XSD *generert* fra JAXB-klassene, ikke originaldokumentene
        publishWebService(new AktoerServiceMockImpl(repo), "/soap/aktoerregister/ws/Aktoer/v2");
        // access wsdl on http://localhost:7999/aktoer?wsdl
        publishWebService(new SakServiceMockImpl(gsakRepo), "/soap/nav-gsak-ws/SakV1");
        // access wsdl on http://localhost:7999/sak?wsdl
        publishWebService(new PersonServiceMockImpl(repo), "/soap/tpsws/ws/Person/v3");
        // access wsdl on http://localhost:7999/person?wsdl
        publishWebService(new JournalV2ServiceMockImpl(journalRepository), "/soap/joark/Journal/v2");
        // access wsdl on http://localhost:7999/journal?wsdl
        publishWebService(new JournalV3ServiceMockImpl(journalRepository), "/soap/joark/Journal/v3");
        publishWebService(new InngaaendeJournalServiceMockImpl(journalRepository), "/soap/joark/InngaaendeJournal/v1");
        // publishWebService(new OppgavebehandlingServiceMockImpl(gsakRepo), "/soap/nav-gsak-ws/BehandleOppgaveV1");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl
        publishWebService(new BehandleOppgaveServiceMockImpl(gsakRepo), "/soap/nav-gsak-ws/BehandleOppgaveV1");
        // access wsdl on http://localhost:7999/behandleoppgave?wsdl
        publishWebService(new BehandleSak2ServiceMockImpl(gsakRepo, repo), "/soap/nav-gsak-ws/BehandleSakV2");
        // access wsdl on http://localhost:7999/behandlesakV2?wsdl
        publishWebService(new FinnSakListeMockImpl(repo), "/soap/infotrygd-ws/InfotrygdSak/v1");
        publishWebService(new FinnGrunnlagListeMockImpl(repo), "/soap/infotrygd-ws/InfotrygdBeregningsgrunnlag/v1");
        // access wsdl on http://localhost:7999/infotrygdsak?wsdl
        publishWebService(new KodeverkServiceMockImpl(repo), "/soap/kodeverk/ws/Kodeverk/v2");

        publishWebService(new DokumentproduksjonV2MockImpl(journalRepository), "/soap/dokprod/ws/dokumentproduksjon/v2");
        publishWebService(new MeldekortUtbetalingsgrunnlagMockImpl(repo), "/soap/ail_ws/MeldekortUtbetalingsgrunnlag_v1");
        publishWebService(new MedlemServiceMockImpl(repo), "/soap/medl2/ws/Medlemskap/v2");
        publishWebService(new ArbeidsfordelingMockImpl(repo), "/soap/norg2/ws/Arbeidsfordeling/v1");
        publishWebService(new InntektMockImpl(repo), "/soap/inntektskomponenten-ws/inntekt/v3/Inntekt");
        publishWebService(new OppgaveServiceMockImpl(), "/soap/nav-gsak-ws/OppgaveV3");
        publishWebService(new ArbeidsforholdMockImpl(repo), "/soap/aareg-core/ArbeidsforholdService/v3");
        publishWebService(new OrganisasjonMockImpl(repo), "/soap/ereg/ws/OrganisasjonService/v4");
        publishWebService(new BehandleJournalV3ServiceMockImpl(),"/soap/services/behandlejournal/v3");
        publishWebService(new SimulerFpServiceMockImpl(), "/soap/cics/services/oppdragService");
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
