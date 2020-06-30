package no.nav.tjeneste.organisasjonenhet.v2;

import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.binding.FinnNAVKontorUgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.binding.HentOverordnetEnhetListeEnhetIkkeFunnet;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.binding.OrganisasjonEnhetV2;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.informasjon.Organisasjonsenhet;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.meldinger.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "OrganisasjonEnhet_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/")
@XmlSeeAlso({
        no.nav.tjeneste.virksomhet.organisasjonenhet.v2.ObjectFactory.class,
        no.nav.tjeneste.virksomhet.organisasjonenhet.v2.feil.ObjectFactory.class,
        no.nav.tjeneste.virksomhet.organisasjonenhet.v2.informasjon.ObjectFactory.class,
        no.nav.tjeneste.virksomhet.organisasjonenhet.v2.meldinger.ObjectFactory.class
})
public class OrganisasjonEnhetMock implements OrganisasjonEnhetV2 {
    private static final Logger LOG = LoggerFactory.getLogger(OrganisasjonEnhetMock.class);

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/OrganisasjonEnhet_v2/finnNAVKontorRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnNAVKontor", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/", className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.FinnNAVKontor")
    @ResponseWrapper(localName = "finnNAVKontorResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/", className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.FinnNAVKontorResponse")
    public FinnNAVKontorResponse finnNAVKontor(
            @WebParam(name = "request", targetNamespace = "") FinnNAVKontorRequest request) throws FinnNAVKontorUgyldigInput {
        FinnNAVKontorResponse response = new FinnNAVKontorResponse();
        Organisasjonsenhet orgEnhet = new Organisasjonsenhet();
        orgEnhet.setEnhetId("4407");
        orgEnhet.setEnhetNavn("NAV Arbeid og ytelser Tønsberg");
        response.setNAVKontor(orgEnhet);
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/OrganisasjonEnhet_v2/hentEnhetBolkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentEnhetBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/", className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentEnhetBolk")
    @ResponseWrapper(localName = "hentEnhetBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/", className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentEnhetBolkResponse")
    public HentEnhetBolkResponse hentEnhetBolk(
            @WebParam(name = "request", targetNamespace = "")
                    HentEnhetBolkRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/OrganisasjonEnhet_v2/hentFullstendigEnhetListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentFullstendigEnhetListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/", className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentFullstendigEnhetListe")
    @ResponseWrapper(localName = "hentFullstendigEnhetListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/", className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentFullstendigEnhetListeResponse")
    public HentFullstendigEnhetListeResponse hentFullstendigEnhetListe(
            @WebParam(name = "request", targetNamespace = "")
                    HentFullstendigEnhetListeRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public void ping() {
        LOG.info("OrganisasjonEnhet_v2:ping kalt");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/OrganisasjonEnhet_v2/hentOverordnetEnhetListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentOverordnetEnhetListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/", className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentOverordnetEnhetListe")
    @ResponseWrapper(localName = "hentOverordnetEnhetListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/", className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentOverordnetEnhetListeResponse")
    public HentOverordnetEnhetListeResponse hentOverordnetEnhetListe(
            @WebParam(name = "request", targetNamespace = "")
                    HentOverordnetEnhetListeRequest request)
            throws HentOverordnetEnhetListeEnhetIkkeFunnet {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
