package no.nav.tjeneste.virksomhet.person.v3;

import no.nav.tjeneste.virksomhet.person.v3.binding.HentEkteskapshistorikkPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentEkteskapshistorikkSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentGeografiskTilknytningPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentGeografiskTilknytningSikkerhetsbegrensing;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonerMedSammeAdresseIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonerMedSammeAdresseSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonhistorikkPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonhistorikkSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentSikkerhetstiltakPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentVergePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentVergeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.PersonV3;
import no.nav.tjeneste.virksomhet.person.v3.data.PersonDbLeser;
import no.nav.tjeneste.virksomhet.person.v3.data.RelasjonDbLeser;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentEkteskapshistorikkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentEkteskapshistorikkResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentGeografiskTilknytningRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentGeografiskTilknytningResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonerMedSammeAdresseRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonerMedSammeAdresseResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonnavnBolkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonnavnBolkResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentSikkerhetstiltakRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentSikkerhetstiltakResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentVergeRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentVergeResponse;
import no.nav.tjeneste.virksomhet.person.v3.modell.RelasjonBygger;
import no.nav.tjeneste.virksomhet.person.v3.modell.TpsRelasjon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;
import java.util.List;

@Addressing
@WebService(name = "Person_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3")
@HandlerChain(file="Handler-chain.xml")
public class PersonServiceMockImpl implements PersonV3 {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceMockImpl.class);
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("tps").createEntityManager();


    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentPerson", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPerson")
    @ResponseWrapper(localName = "hentPersonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonResponse")
    @Override
    public HentPersonResponse hentPerson(@WebParam(name = "request", targetNamespace = "") HentPersonRequest hentPersonRequest) throws HentPersonPersonIkkeFunnet, HentPersonSikkerhetsbegrensning {

        PersonIdent personIdent = (PersonIdent)hentPersonRequest.getAktoer();
        String ident = personIdent.getIdent().getIdent();

        Bruker bruker = new PersonDbLeser(entityManager).finnPerson(ident);
        if (bruker == null) {
            throw new HentPersonPersonIkkeFunnet("Fant ingen bruker for ident: " + ident, new no.nav.tjeneste.virksomhet.person.v3.feil.PersonIkkeFunnet());
        }

        List<TpsRelasjon> relasjoner = new RelasjonDbLeser(entityManager).finnRelasjon(ident);
        for (TpsRelasjon relasjon : relasjoner) {
            new RelasjonBygger(relasjon).byggFor(bruker);
        }

        HentPersonResponse response = new HentPersonResponse();
        response.setPerson(bruker);
        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentGeografiskTilknytningRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentGeografiskTilknytning", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytning")
    @ResponseWrapper(localName = "hentGeografiskTilknytningResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytningResponse")
    @Override
    public HentGeografiskTilknytningResponse hentGeografiskTilknytning(@WebParam(name = "request", targetNamespace = "") HentGeografiskTilknytningRequest hentGeografiskTilknytningRequest) throws HentGeografiskTilknytningPersonIkkeFunnet, HentGeografiskTilknytningSikkerhetsbegrensing {
        PersonIdent personIdent = (PersonIdent)hentGeografiskTilknytningRequest.getAktoer();
        String ident = personIdent.getIdent().getIdent();

        Bruker bruker = new PersonDbLeser(entityManager).finnPerson(ident);
        if (bruker == null) {
            throw new HentGeografiskTilknytningPersonIkkeFunnet("Fant ingen bruker for ident: " + ident, new no.nav.tjeneste.virksomhet.person.v3.feil.PersonIkkeFunnet());
        }

        HentGeografiskTilknytningResponse response = new HentGeografiskTilknytningResponse();
        response.setGeografiskTilknytning(bruker.getGeografiskTilknytning());
        response.setDiskresjonskode(bruker.getDiskresjonskode());
        return response;
    }

    @WebMethod
    @RequestWrapper(localName = "hentVerge", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentVerge")
    @ResponseWrapper(localName = "hentVergeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentVergeResponse")
    @WebResult(name = "response", targetNamespace = "")
    @Override
    public HentVergeResponse hentVerge(HentVergeRequest hentVergeRequest) throws HentVergePersonIkkeFunnet, HentVergeSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(localName = "hentEkteskapshistorikk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikk")
    @ResponseWrapper(localName = "hentEkteskapshistorikkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikkResponse")
    @WebResult(name = "response", targetNamespace = "")
    @Override
    public HentEkteskapshistorikkResponse hentEkteskapshistorikk(HentEkteskapshistorikkRequest hentEkteskapshistorikkRequest) throws HentEkteskapshistorikkPersonIkkeFunnet, HentEkteskapshistorikkSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(localName = "hentPersonerMedSammeAdresse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresse")
    @ResponseWrapper(localName = "hentPersonerMedSammeAdresseResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresseResponse")
    @WebResult(name = "response", targetNamespace = "")
    @Override
    public HentPersonerMedSammeAdresseResponse hentPersonerMedSammeAdresse(HentPersonerMedSammeAdresseRequest hentPersonerMedSammeAdresseRequest) throws HentPersonerMedSammeAdresseIkkeFunnet, HentPersonerMedSammeAdresseSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(localName = "hentPersonhistorikk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikk")
    @ResponseWrapper(localName = "hentPersonhistorikkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikkResponse")
    @WebResult(name = "response", targetNamespace = "")
    @Override
    public HentPersonhistorikkResponse hentPersonhistorikk(HentPersonhistorikkRequest hentPersonhistorikkRequest) throws HentPersonhistorikkPersonIkkeFunnet, HentPersonhistorikkSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert, men skal implementeres i PK-49366 PKTERMITT-121");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonnavnBolkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentPersonnavnBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonnavnBolk")
    @ResponseWrapper(localName = "hentPersonnavnBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonnavnBolkResponse")
    @Override
    public HentPersonnavnBolkResponse hentPersonnavnBolk(@WebParam(name = "request", targetNamespace = "") HentPersonnavnBolkRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentSikkerhetstiltakRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentSikkerhetstiltak", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltak")
    @ResponseWrapper(localName = "hentSikkerhetstiltakResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltakResponse")
    @Override
    public HentSikkerhetstiltakResponse hentSikkerhetstiltak(@WebParam(name = "request", targetNamespace = "") HentSikkerhetstiltakRequest var1) throws HentSikkerhetstiltakPersonIkkeFunnet {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
