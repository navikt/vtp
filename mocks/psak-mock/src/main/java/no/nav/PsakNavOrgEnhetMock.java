package no.nav;

import no.nav.inf.psak.navorgenhet.ObjectFactory;
import no.nav.inf.psak.navorgenhet.*;
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.*;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf", name = "PSAKNAVOrgEnhet")
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.navorgenhet.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.navorgenhet.ObjectFactory.class, ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class PsakNavOrgEnhetMock implements PSAKNAVOrgEnhet {
    @Override
    public ASBOPenNAVEnhetListe hentSpesialenhetTilPerson(ASBOPenHentSpesialEnhetTilPersonRequest hentSpesialenhetTilPersonRequest) throws HentSpesialenhetTilPersonFaultPenNAVEnhetIkkeFunnetMsg, HentSpesialenhetTilPersonFaultPenPersonIkkeFunnetMsg, HentSpesialenhetTilPersonFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentNAVEnhet", targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf", className = "no.nav.inf.psak.navorgenhet.HentNAVEnhet")
    @ResponseWrapper(localName = "hentNAVEnhetResponse", targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf", className = "no.nav.inf.psak.navorgenhet.HentNAVEnhetResponse")
    @WebResult(name = "hentNAVEnhetResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet hentNAVEnhet(
            @WebParam(name = "hentNAVEnhetRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet hentNAVEnhetRequest
    ) throws HentNAVEnhetFaultPenNAVEnhetIkkeFunnetMsg, HentNAVEnhetFaultPenGeneriskMsg {
        ASBOPenNAVEnhet enhet = new ASBOPenNAVEnhet();
        enhet.setEnhetsId("1235");
        enhet.setEnhetsNavn("Kutoppen");
        return enhet;
    }

    @Override
    public ASBOPenNAVEnhetListe hentNAVEnhetGruppeListe(ASBOPenNAVEnhet hentNAVEnhetGruppeListeRequest) throws HentNAVEnhetGruppeListeFaultPenNAVEnhetIkkeFunnetMsg, HentNAVEnhetGruppeListeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenNAVEnhetListe finnNAVEnhet(ASBOPenFinnNAVEnhetRequest finnNAVEnhetRequest) throws FinnNAVEnhetFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenNAVEnhetListe hentNAVEnhetListe(ASBOPenHentNAVEnhetListeRequest hentNAVEnhetListeRequest) throws HentNAVEnhetListeFaultPenNAVEnhetIkkeFunnetMsg, HentNAVEnhetListeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenNAVEnhetListe hentForvaltningsenhetTilPersonListe(ASBOPenHentForvaltningsenhetTilPersonListeRequest hentForvaltningsenhetTilPersonListeRequest) throws HentForvaltningsenhetTilPersonListeFaultPenNAVEnhetIkkeFunnetMsg, HentForvaltningsenhetTilPersonListeFaultPenPersonManglerEnhetMsg, HentForvaltningsenhetTilPersonListeFaultPenPersonIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenNAVEnhetListe finnArenaNAVEnhetListe(ASBOPenFinnArenaNAVEnhetListeRequest finnArenaNAVEnhetListeRequest) throws FinnArenaNAVEnhetListeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
