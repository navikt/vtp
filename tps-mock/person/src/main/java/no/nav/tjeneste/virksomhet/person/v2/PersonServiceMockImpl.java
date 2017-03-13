package no.nav.tjeneste.virksomhet.person.v2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.tjeneste.virksomhet.person.v2.binding.HentKjerneinformasjonPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v2.binding.HentKjerneinformasjonSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v2.binding.HentSikkerhetstiltakPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v2.binding.PersonV2;
import no.nav.tjeneste.virksomhet.person.v2.feil.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Person;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentKjerneinformasjonRequest;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentKjerneinformasjonResponse;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentPersonnavnBolkRequest;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentPersonnavnBolkResponse;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentSikkerhetstiltakRequest;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentSikkerhetstiltakResponse;
import no.nav.tjeneste.virksomhet.person.v2.modell.TpsRepo;

@WebService(name = "Person_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2")
public class PersonServiceMockImpl implements PersonV2 {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceMockImpl.class);
    private static final TpsRepo TPS_REPO = TpsRepo.init();

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v2/Person_v2/hentKjerneinformasjonRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentKjerneinformasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.HentKjerneinformasjon")
    @ResponseWrapper(localName = "hentKjerneinformasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.HentKjerneinformasjonResponse")
    @Override
    public no.nav.tjeneste.virksomhet.person.v2.meldinger.HentKjerneinformasjonResponse hentKjerneinformasjon(@WebParam(name = "request",targetNamespace = "") HentKjerneinformasjonRequest request) throws HentKjerneinformasjonPersonIkkeFunnet, HentKjerneinformasjonSikkerhetsbegrensning {
        LOG.info("hentIdentForAktoerId: " + request.getIdent());
        Person person = TPS_REPO.finnPerson(request.getIdent());
        if(person == null) {
            throw new HentKjerneinformasjonPersonIkkeFunnet("Fant ingen bruker for ident: " + request.getIdent(), new PersonIkkeFunnet());
        }
        HentKjerneinformasjonResponse response = new HentKjerneinformasjonResponse();
        response.setPerson(person);
        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v2/Person_v2/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v2/Person_v2/hentPersonnavnBolkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentPersonnavnBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.HentPersonnavnBolk")
    @ResponseWrapper(localName = "hentPersonnavnBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.HentPersonnavnBolkResponse")
    @Override
    public HentPersonnavnBolkResponse hentPersonnavnBolk(@WebParam(name = "request",targetNamespace = "") HentPersonnavnBolkRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v2/Person_v2/hentSikkerhetstiltakRequest")
    @WebResult(name = "response",targetNamespace = "")
    @RequestWrapper(localName = "hentSikkerhetstiltak", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2",className = "no.nav.tjeneste.virksomhet.person.v2.HentSikkerhetstiltak")
    @ResponseWrapper(localName = "hentSikkerhetstiltakResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2",className = "no.nav.tjeneste.virksomhet.person.v2.HentSikkerhetstiltakResponse")
    @Override
    public HentSikkerhetstiltakResponse hentSikkerhetstiltak(@WebParam(name = "request",targetNamespace = "") HentSikkerhetstiltakRequest var1) throws HentSikkerhetstiltakPersonIkkeFunnet {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
