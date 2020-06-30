package no.nav;

import no.nav.inf.pen.tjenestepensjon.*;
import no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenTjenestepensjon;
import no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenTjenestepensjonForhold;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav-cons-pen-pen-tjenestepensjon/no/nav/inf", name = "PENTjenestepensjon")
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.tjenestepensjon.ObjectFactory.class, ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class PenTjenestePensjonMock implements PENTjenestepensjon {

    @Override
    @WebMethod
    @RequestWrapper(localName = "finnTjenestepensjonsForhold", targetNamespace = "http://nav-cons-pen-pen-tjenestepensjon/no/nav/inf", className = "no.nav.inf.pen.tjenestepensjon.FinnTjenestepensjonsForhold")
    @ResponseWrapper(localName = "finnTjenestepensjonsForholdResponse", targetNamespace = "http://nav-cons-pen-pen-tjenestepensjon/no/nav/inf", className = "no.nav.inf.pen.tjenestepensjon.FinnTjenestepensjonsForholdResponse")
    @WebResult(name = "finnTjenestepensjonForholdResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenTjenestepensjon finnTjenestepensjonsForhold(
            @WebParam(name = "finnTjenestepensjonForholdRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenFinnTjenestepensjonForholdRequest finnTjenestepensjonForholdRequest
    ) throws FinnTjenestepensjonsForholdFaultElementetFinnesIkkeMsg, FinnTjenestepensjonsForholdFaultPenGeneriskMsg, FinnTjenestepensjonsForholdFaultTomDatoForanFromDatoMsg {
        ASBOPenTjenestepensjon response = new ASBOPenTjenestepensjon();
        response.setFnr(finnTjenestepensjonForholdRequest.getFnr());
        response.setPersonId(finnTjenestepensjonForholdRequest.getFnr());
        response.setTjenestepensjonForholdene(new ASBOPenTjenestepensjonForhold[0]);

        return response;
    }
}
