package no.nav;

import no.nav.inf.pen.oppdrag.*;
import no.nav.lib.pen.psakpselv.asbo.oppdrag.ASBOPenSimuleringsresultatLinjeListe;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", name = "PENOppdrag")
@XmlSeeAlso({ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.oppdrag.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.oppdrag.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class PENOppdragMock implements PENOppdrag {

    @Override
    @WebMethod
    @RequestWrapper(localName = "sendAsynkronOppdragsavstemmingListe", targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", className = "no.nav.inf.pen.oppdrag.SendAsynkronOppdragsavstemmingListe")
    @ResponseWrapper(localName = "sendAsynkronOppdragsavstemmingListeResponse", targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", className = "no.nav.inf.pen.oppdrag.SendAsynkronOppdragsavstemmingListeResponse")
    public void sendAsynkronOppdragsavstemmingListe(
            @WebParam(name = "sendAsynkronOppdragsavstemmingListeRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.oppdrag.ASBOPenSendAsynkOppdragsavstemmingListeRequest sendAsynkronOppdragsavstemmingListeRequest
    ) throws SendAsynkronOppdragsavstemmingListeFaultPenGeneriskMsg{
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentOppdragSimulering", targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", className = "no.nav.inf.pen.oppdrag.HentOppdragSimulering")
    @ResponseWrapper(localName = "hentOppdragSimuleringResponse", targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", className = "no.nav.inf.pen.oppdrag.HentOppdragSimuleringResponse")
    @WebResult(name = "hentOppdragSimuleringResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.oppdrag.ASBOPenSimuleringsresultatLinjeListe hentOppdragSimulering(
            @WebParam(name = "hentOppdragSimuleringRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.oppdrag.ASBOPenOppdragsmelding hentOppdragSimuleringRequest
    ) throws HentOppdragSimuleringFaultPenGeneriskMsg, HentOppdragSimuleringFaultPenServiceUnavailableMsg, HentOppdragSimuleringFaultPenFeilIOppdragMsg {
        return new ASBOPenSimuleringsresultatLinjeListe();
    }
}
