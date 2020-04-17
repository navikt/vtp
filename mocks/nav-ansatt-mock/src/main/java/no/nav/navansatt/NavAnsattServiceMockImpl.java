package no.nav.navansatt;

import no.nav.inf.psak.navansatt.*;
import no.nav.lib.pen.psakpselv.asbo.ASBOPenFagomradeListe;
import no.nav.lib.pen.psakpselv.asbo.navansatt.ASBOPenNAVAnsatt;
import no.nav.lib.pen.psakpselv.asbo.navansatt.ASBOPenNAVAnsattListe;
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet;
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhetListe;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

@Addressing
@WebService(name = "PSAKNAVAnsatt", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf")
@HandlerChain(file = "Handler-chain.xml")
public class NavAnsattServiceMockImpl implements PSAKNAVAnsatt {

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattEnhetListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListe")
    @ResponseWrapper(localName = "hentNAVAnsattEnhetListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListeResponse")
    @WebResult(name = "hentNAVAnsattEnhetListeResponse", targetNamespace = "")
    @Override
    public ASBOPenNAVEnhetListe hentNAVAnsattEnhetListe(@WebParam(name = "hentNAVAnsattEnhetListeRequest",targetNamespace = "") ASBOPenNAVAnsatt asboPenNAVAnsatt) throws HentNAVAnsattEnhetListeFaultPenGeneriskMsg, HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattFagomradeListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattFagomradeListe")
    @ResponseWrapper(localName = "hentNAVAnsattFagomradeListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattFagomradeListeResponse")
    @WebResult(name = "hentNAVAnsattFagomradeListeResponse", targetNamespace = "")
    @Override
    public ASBOPenFagomradeListe hentNAVAnsattFagomradeListe(@WebParam(name = "hentNAVAnsattFagomradeListeRequest",targetNamespace = "") ASBOPenNAVAnsatt asboPenNAVAnsatt) throws HentNAVAnsattFagomradeListeFaultPenGenerisksMsg, HentNAVAnsattFagomradeListeFaultPenNAVAnsattIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattListe")
    @ResponseWrapper(localName = "hentNAVAnsattListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattListeResponse"
    )
    @WebResult(name = "hentNAVAnsattListeResponse", targetNamespace = "")
    @Override
    public ASBOPenNAVAnsattListe hentNAVAnsattListe(@WebParam(name = "hentNAVAnsattListeRequest",targetNamespace = "") ASBOPenNAVEnhet asboPenNAVEnhet) throws HentNAVAnsattListeFaultPenNAVEnhetIkkeFunnetMsg, HentNAVAnsattListeFaultPenGenerisksMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsatt", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsatt")
    @ResponseWrapper(localName = "hentNAVAnsattResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattResponse")
    @WebResult(name = "hentNAVAnsattResponse", targetNamespace = "")
    @Override
    public ASBOPenNAVAnsatt hentNAVAnsatt(@WebParam(name = "hentNAVAnsattRequest",targetNamespace = "") ASBOPenNAVAnsatt asboPenNAVAnsatt) throws HentNAVAnsattFaultPenGeneriskMsg, HentNAVAnsattFaultPenNAVAnsattIkkeFunnetMsg {
        asboPenNAVAnsatt.setAnsattNavn("Saksbehandler Saksbehandlersen");
        asboPenNAVAnsatt.setFornavn("Saksbehandler");
        asboPenNAVAnsatt.setEtternavn("Saksbehandlersen");
        return asboPenNAVAnsatt;
    }
}
