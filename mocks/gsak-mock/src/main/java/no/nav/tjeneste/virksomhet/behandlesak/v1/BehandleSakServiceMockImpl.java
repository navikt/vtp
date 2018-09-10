package no.nav.tjeneste.virksomhet.behandlesak.v1;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.behandlesak.v1.binding.BehandleSakV1;
import no.nav.tjeneste.virksomhet.behandlesak.v1.binding.OpprettSakSakEksistererAllerede;
import no.nav.tjeneste.virksomhet.behandlesak.v1.binding.OpprettSakUgyldigInput;
import no.nav.tjeneste.virksomhet.behandlesak.v1.meldinger.OpprettSakRequest;
import no.nav.tjeneste.virksomhet.behandlesak.v1.meldinger.OpprettSakResponse;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;

@Addressing
@WebService(name = "BehandleSak_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1")
@HandlerChain(file = "Handler-chain.xml")
public class BehandleSakServiceMockImpl implements BehandleSakV1 {

    private static final Logger LOG = LoggerFactory.getLogger(BehandleSakServiceMockImpl.class);
    private GsakRepo gsakRepo;
    private TestscenarioBuilderRepository repository;

    public BehandleSakServiceMockImpl(GsakRepo repo, TestscenarioBuilderRepository repository) {
        this.gsakRepo = repo;
        this.repository = repository;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleSak/v1/BehandleSak_v1/opprettSakRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "opprettSak", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1", className = "no.nav.tjeneste.virksomhet.behandlesak.v1.OpprettSak")
    @ResponseWrapper(localName = "opprettSakResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1", className = "no.nav.tjeneste.virksomhet.behandlesak.v1.OpprettSakResponse")
    public OpprettSakResponse opprettSak(@WebParam(name = "request", targetNamespace = "") OpprettSakRequest request)
            throws OpprettSakSakEksistererAllerede, OpprettSakUgyldigInput {
        LOG.info("Oppretter Sak: {}", request);

        OpprettSakResponse response = new OpprettSakResponse();
        Set<String> identer = request.getSak().getGjelderBrukerListe().stream().map(a -> a.getIdent()).collect(Collectors.toSet());

        List<PersonModell> personer = identer.stream().map(i -> (PersonModell) repository.getPersonIndeks().finnByIdent(i)).collect(Collectors.toList());
        no.nav.tjeneste.virksomhet.sak.v1.informasjon.Sak sak = gsakRepo.leggTilSak(personer);
        response.setSakId(sak.getSakId());
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleSak/v1/BehandleSak_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1", className = "no.nav.tjeneste.virksomhet.behandlesak.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1", className = "no.nav.tjeneste.virksomhet.behandlesak.v1.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

}
