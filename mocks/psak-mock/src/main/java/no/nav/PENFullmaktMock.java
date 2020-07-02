package no.nav;

import no.nav.inf.pen.fullmakt.FinnFullmaktmottagereFaultPenGeneriskMsg;
import no.nav.inf.pen.fullmakt.ObjectFactory;
import no.nav.inf.pen.fullmakt.PENFullmakt;
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFullmaktListe;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


@WebService(targetNamespace = "http://nav-cons-pen-pen-fullmakt/no/nav/inf", name = "PENFullmakt")
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.fullmakt.ObjectFactory.class, ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class PENFullmaktMock implements PENFullmakt {

    @Override
    @WebMethod
    @RequestWrapper(localName = "finnFullmaktmottagere", targetNamespace = "http://nav-cons-pen-pen-fullmakt/no/nav/inf", className = "no.nav.inf.pen.fullmakt.FinnFullmaktmottagere")
    @ResponseWrapper(localName = "finnFullmaktmottagereResponse", targetNamespace = "http://nav-cons-pen-pen-fullmakt/no/nav/inf", className = "no.nav.inf.pen.fullmakt.FinnFullmaktmottagereResponse")
    @WebResult(name = "finnFullmaktmottagereResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFullmaktListe finnFullmaktmottagere(
            @WebParam(name = "finnFullmaktmottagereRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFinnFullmaktmottagereRequest finnFullmaktmottagereRequest
    ) throws FinnFullmaktmottagereFaultPenGeneriskMsg {
        return new ASBOPenFullmaktListe();
    }
}
