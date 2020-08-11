package no.nav;

import no.nav.inf.psak.ppen015.ObjectFactory;
import no.nav.inf.psak.ppen015.PSAKPPEN015;
import no.nav.lib.pen.psakpselv.asbo.ppen015.ASBOPSAKPPEN015Response;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav-cons-pen-psak-ppen015/no/nav/inf/PSAKPPEN015", name = "PSAKPPEN015")
@XmlSeeAlso({ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ppen015.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class PSAKPPEN015Mock implements PSAKPPEN015 {

    @Override
    @WebMethod
    @RequestWrapper(localName = "iverksettVedtak", targetNamespace = "http://nav-cons-pen-psak-ppen015/no/nav/inf/PSAKPPEN015", className = "no.nav.inf.psak.ppen015.IverksettVedtak")
    @ResponseWrapper(localName = "iverksettVedtakResponse", targetNamespace = "http://nav-cons-pen-psak-ppen015/no/nav/inf/PSAKPPEN015", className = "no.nav.inf.psak.ppen015.IverksettVedtakResponse")
    @WebResult(name = "iverksettVedtakResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.ppen015.ASBOPSAKPPEN015Response iverksettVedtak(
            @WebParam(name = "iverksettVedtakRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.ppen015.ASBOPSAKPPEN015Request iverksettVedtakRequest
    ) {
        ASBOPSAKPPEN015Response response = new ASBOPSAKPPEN015Response();
        response.setStartetOk(true);
        return response;
    }
}
