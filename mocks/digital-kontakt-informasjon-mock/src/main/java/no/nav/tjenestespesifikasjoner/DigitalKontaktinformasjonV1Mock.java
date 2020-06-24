package no.nav.tjenestespesifikasjoner;

import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.ObjectFactory;
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.*;
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.informasjon.WSEpostadresse;
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.informasjon.WSKontaktinformasjon;
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.informasjon.WSMobiltelefonnummer;
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.*;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/digitalKontaktinformasjon/v1", name = "DigitalKontaktinformasjon_v1")
@XmlSeeAlso({ObjectFactory.class, no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.informasjon.ObjectFactory.class, no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.feil.ObjectFactory.class, no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class DigitalKontaktinformasjonV1Mock implements DigitalKontaktinformasjonV1 {
    @Override
    public WSHentSikkerDigitalPostadresseBolkResponse hentSikkerDigitalPostadresseBolk(WSHentSikkerDigitalPostadresseBolkRequest wsHentSikkerDigitalPostadresseBolkRequest) throws HentSikkerDigitalPostadresseBolkSikkerhetsbegrensing, HentSikkerDigitalPostadresseBolkForMangeForespoersler {
        return null;
    }

    @Override
    public WSHentPrintsertifikatResponse hentPrintsertifikat(WSHentPrintsertifikatRequest wsHentPrintsertifikatRequest) {
        return null;
    }

    @Override
    @WebMethod(operationName = "HentDigitalKontaktinformasjon", action = "http://nav.no/tjeneste/virksomhet/digitalKontaktinformasjon/v1/DigitalKontaktinformasjon_v1/HentDigitalKontaktinformasjonRequest")
    @RequestWrapper(localName = "HentDigitalKontaktinformasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/digitalKontaktinformasjon/v1", className = "no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.HentDigitalKontaktinformasjon")
    @ResponseWrapper(localName = "HentDigitalKontaktinformasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/digitalKontaktinformasjon/v1", className = "no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.HentDigitalKontaktinformasjonResponse")
    @WebResult(name = "response", targetNamespace = "")
    public no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentDigitalKontaktinformasjonResponse hentDigitalKontaktinformasjon(
            @WebParam(name = "request", targetNamespace = "") no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentDigitalKontaktinformasjonRequest request
    ) throws HentDigitalKontaktinformasjonKontaktinformasjonIkkeFunnet, HentDigitalKontaktinformasjonSikkerhetsbegrensing, HentDigitalKontaktinformasjonPersonIkkeFunnet{
        WSHentDigitalKontaktinformasjonResponse response = new WSHentDigitalKontaktinformasjonResponse();
        WSKontaktinformasjon wsKontaktinformasjon = new WSKontaktinformasjon();
        wsKontaktinformasjon.setEpostadresse(new WSEpostadresse().withValue("example@com"));
        wsKontaktinformasjon.setMobiltelefonnummer(new WSMobiltelefonnummer().withValue("11223211"));
        response.setDigitalKontaktinformasjon(wsKontaktinformasjon);

        return response;
    }

    @Override
    public void ping() {

    }

    @Override
    public WSHentDigitalKontaktinformasjonBolkResponse hentDigitalKontaktinformasjonBolk(WSHentDigitalKontaktinformasjonBolkRequest wsHentDigitalKontaktinformasjonBolkRequest) throws HentDigitalKontaktinformasjonBolkSikkerhetsbegrensing, HentDigitalKontaktinformasjonBolkForMangeForespoersler {
        return null;
    }

    @Override
    public WSHentSikkerDigitalPostadresseResponse hentSikkerDigitalPostadresse(WSHentSikkerDigitalPostadresseRequest wsHentSikkerDigitalPostadresseRequest) throws HentSikkerDigitalPostadresseKontaktinformasjonIkkeFunnet, HentSikkerDigitalPostadresseSikkerhetsbegrensing, HentSikkerDigitalPostadressePersonIkkeFunnet {
        return null;
    }
}
