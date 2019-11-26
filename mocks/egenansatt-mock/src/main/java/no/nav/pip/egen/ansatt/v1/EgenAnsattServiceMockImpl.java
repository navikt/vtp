package no.nav.pip.egen.ansatt.v1;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.tjeneste.pip.egen.ansatt.v1.EgenAnsattV1;
import no.nav.tjeneste.pip.egen.ansatt.v1.WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.egen.ansatt.v1.WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse;

public class EgenAnsattServiceMockImpl implements EgenAnsattV1 {
    private static final Logger LOG = LoggerFactory.getLogger(EgenAnsattServiceMockImpl.class);

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/pip/egenAnsatt/v1/EgenAnsatt_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/pip/egenAnsatt/v1/", className = "no.nav.tjeneste.pip.egen.ansatt.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/pip/egenAnsatt/v1/", className = "no.nav.tjeneste.pip.egen.ansatt.v1.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/pip/egenAnsatt/v1/EgenAnsatt_v1/hentErEgenAnsattEllerIFamilieMedEgenAnsattRequest")
    @RequestWrapper(localName = "hentErEgenAnsattEllerIFamilieMedEgenAnsatt", targetNamespace = "http://nav.no/tjeneste/pip/egenAnsatt/v1/", className = "no.nav.tjeneste.pip.egen.ansatt.v1.HentErEgenAnsattEllerIFamilieMedEgenAnsatt")
    @ResponseWrapper(localName = "hentErEgenAnsattEllerIFamilieMedEgenAnsattResponse", targetNamespace = "http://nav.no/tjeneste/pip/egenAnsatt/v1/", className = "no.nav.tjeneste.pip.egen.ansatt.v1.HentErEgenAnsattEllerIFamilieMedEgenAnsattResponse")
    @WebResult(name = "response", targetNamespace = "")
    public WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse hentErEgenAnsattEllerIFamilieMedEgenAnsatt(WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest wsHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest) {
        WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse response = new WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse();
        response.setEgenAnsatt(false);
        return response;
    }
}
