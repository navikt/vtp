package no.nav.tjeneste.virksomhet.kodeverk.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jws.HandlerChain;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;
import jakarta.xml.ws.soap.Addressing;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.FinnKodeverkListeRequest;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.FinnKodeverkListeResponse;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkRequest;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkResponse;

/** @deprecated brukes ikke?. */
@Deprecated(forRemoval = true)
@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkPortType")
@HandlerChain(file = "Handler-chain.xml")
public class KodeverkServiceMockImpl implements KodeverkPortType {

    private static final Logger LOG = LoggerFactory.getLogger(KodeverkServiceMockImpl.class);

    private TestscenarioBuilderRepository scenarioRepository;

    public KodeverkServiceMockImpl() {
    }

    public KodeverkServiceMockImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @Override
    public void ping() {
        LOG.info("Ping motatt og besvart");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/KodeverkPortType/hentKodeverkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentKodeverk", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverk")
    @ResponseWrapper(localName = "hentKodeverkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverkResponse")
    public HentKodeverkResponse hentKodeverk(@WebParam(name = "respons", targetNamespace = "") HentKodeverkRequest request) {

        LOG.info("hentKodeverk. Blir kalt, kaster exception");

        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/KodeverkPortType/finnKodeverkListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnKodeverkListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.FinnKodeverkListe")
    @ResponseWrapper(localName = "finnKodeverkListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.FinnKodeverkListeResponse")
    public FinnKodeverkListeResponse finnKodeverkListe(@WebParam(name = "respons", targetNamespace = "") FinnKodeverkListeRequest request) {

        LOG.info("finnKodeverkListe. Blir kalt, kaster exception.");

        throw new UnsupportedOperationException("Ikke implementert");
    }
}
