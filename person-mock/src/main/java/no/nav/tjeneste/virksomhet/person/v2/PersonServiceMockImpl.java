package no.nav.tjeneste.virksomhet.person.v2;

import no.nav.tjeneste.virksomhet.person.v2.binding.HentKjerneinformasjonPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v2.binding.HentKjerneinformasjonSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v2.binding.HentSikkerhetstiltakPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v2.binding.PersonV2;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.*;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.*;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentKjerneinformasjonResponse;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentPersonnavnBolkResponse;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentSikkerhetstiltakResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.GregorianCalendar;

@WebService(name = "Person_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2")
public class PersonServiceMockImpl implements PersonV2 {

    private static final Logger log = LoggerFactory.getLogger(PersonServiceMockImpl.class);

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v2/Person_v2/hentKjerneinformasjonRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentKjerneinformasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.HentKjerneinformasjon")
    @ResponseWrapper(localName = "hentKjerneinformasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.HentKjerneinformasjonResponse")
    @Override
    public no.nav.tjeneste.virksomhet.person.v2.meldinger.HentKjerneinformasjonResponse hentKjerneinformasjon(@WebParam(name = "request",targetNamespace = "") HentKjerneinformasjonRequest request) throws HentKjerneinformasjonPersonIkkeFunnet, HentKjerneinformasjonSikkerhetsbegrensning {
        log.info("hentIdentForAktoerId: " + request.getIdent());
        no.nav.tjeneste.virksomhet.person.v2.meldinger.HentKjerneinformasjonResponse response = new HentKjerneinformasjonResponse();
        Person person = new Person();

        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(request.getIdent());
        Personidenter personidenter = new Personidenter();
        personidenter.setValue("fnr");
        norskIdent.setType(personidenter);
        person.setIdent(norskIdent);

        Kjoenn kjonn = new Kjoenn();
        Kjoennstyper kjonnstype = new Kjoennstyper();
        kjonnstype.setValue("M");
        kjonn.setKjoenn(kjonnstype);
        person.setKjoenn(kjonn);

        Foedselsdato fodselsdato = new Foedselsdato();
        XMLGregorianCalendar xcal = null;
        try {
            LocalDate fødselsdato = LocalDate.of(1992, Month.OCTOBER, 13);
            GregorianCalendar gcal = GregorianCalendar.from(fødselsdato.atStartOfDay(ZoneId.systemDefault()));
            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        fodselsdato.setFoedselsdato(xcal);
        person.setFoedselsdato(fodselsdato);

        Personnavn personnavn   = new Personnavn();
        personnavn.setEtternavn("HJARTDAL");
        personnavn.setFornavn("ANNE-BERIT");
        personnavn.setSammensattNavn("HJARTDAL ANNE-BERIT");
        person.setPersonnavn(personnavn);

        response.setPerson(person);
        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v2/Person_v2/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v2", className = "no.nav.tjeneste.virksomhet.person.v2.PingResponse")
    @Override
    public void ping() {
        log.info("Ping mottatt og besvart");
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
