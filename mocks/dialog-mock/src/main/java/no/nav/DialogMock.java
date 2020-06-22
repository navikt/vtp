package no.nav;

import no.nav.tjeneste.virksomhet.dialog.v1.DialogV1;
import no.nav.tjeneste.virksomhet.dialog.v1.HentDialogerPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.dialog.v1.ObjectFactory;
import no.nav.tjeneste.virksomhet.dialog.v1.meldinger.WSHentDialogerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1", name = "Dialog_v1")
@XmlSeeAlso({no.nav.tjeneste.virksomhet.dialog.v1.meldinger.ObjectFactory.class, no.nav.tjeneste.virksomhet.dialog.v1.feil.ObjectFactory.class, ObjectFactory.class, no.nav.tjeneste.virksomhet.dialog.v1.informasjon.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class DialogMock implements DialogV1 {
    private static final Logger LOG = LoggerFactory.getLogger(DialogMock.class);

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/dialog/v1/Dialog_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1", className = "no.nav.tjeneste.virksomhet.dialog.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1", className = "no.nav.tjeneste.virksomhet.dialog.v1.PingResponse")
    public void ping() {
        LOG.info("Utførte ping-kall til DialogV1");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/dialog/v1/Dialog_v1/hentDialogerRequest")
    @RequestWrapper(localName = "hentDialoger", targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1", className = "no.nav.tjeneste.virksomhet.dialog.v1.HentDialoger")
    @ResponseWrapper(localName = "hentDialogerResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1", className = "no.nav.tjeneste.virksomhet.dialog.v1.HentDialogerResponse")
    @WebResult(name = "response", targetNamespace = "")
    public no.nav.tjeneste.virksomhet.dialog.v1.meldinger.WSHentDialogerResponse hentDialoger(
            @WebParam(name = "request", targetNamespace = "")
                    no.nav.tjeneste.virksomhet.dialog.v1.meldinger.WSHentDialogerRequest request
    ) throws HentDialogerPersonIkkeFunnet {
        WSHentDialogerResponse response = new WSHentDialogerResponse();
        LOG.warn("Uførte kall til DialogMock");
        return response;
    }
}
