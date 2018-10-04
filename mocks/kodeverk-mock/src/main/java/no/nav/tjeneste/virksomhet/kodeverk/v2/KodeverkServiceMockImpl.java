package no.nav.tjeneste.virksomhet.kodeverk.v2;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.FinnKodeverkListeRequest;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.FinnKodeverkListeResponse;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkRequest;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkPortType")
@HandlerChain(file = "Handler-chain.xml")
public class KodeverkServiceMockImpl implements KodeverkPortType {

    private static final Logger LOG = LoggerFactory.getLogger(KodeverkServiceMockImpl.class);

    private TestscenarioBuilderRepository scenarioRepository;

    public KodeverkServiceMockImpl() {}

    public KodeverkServiceMockImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @Override
    public void ping() {
        LOG.info("Ping motatt og besvart");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/KodeverkPortType/hentKodeverkRequest")
    @WebResult(name="response", targetNamespace = "")
    @RequestWrapper(localName = "hentKodeverk", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverk")
    @ResponseWrapper(localName = "hentKodeverkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverkResponse")
    public HentKodeverkResponse hentKodeverk(@WebParam(name="respons", targetNamespace = "") HentKodeverkRequest request) {

        LOG.info("hentKodeverk. Blir kalt, kaster exception");

        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/KodeverkPortType/finnKodeverkListeRequest")
    @WebResult(name="response", targetNamespace = "")
    @RequestWrapper(localName = "finnKodeverkListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.FinnKodeverkListe")
    @ResponseWrapper(localName = "finnKodeverkListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.FinnKodeverkListeResponse")
    public FinnKodeverkListeResponse finnKodeverkListe(@WebParam(name="respons", targetNamespace = "") FinnKodeverkListeRequest request) {

        LOG.info("finnKodeverkListe. Blir kalt, kaster exception.");

        throw new UnsupportedOperationException("Ikke implementert");
    }
}
