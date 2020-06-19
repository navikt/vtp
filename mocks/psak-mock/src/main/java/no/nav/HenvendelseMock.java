package no.nav;

import no.nav.inf.psak.henvendelse.*;
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHentStatistikkRequest;
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelse;
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelseListe;
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenStatistikk;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav-cons-pen-psak-henvendelse/no/nav/inf", name = "PSAKHenvendelse")
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.henvendelse.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.henvendelse.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.oppgave.ObjectFactory.class, ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class HenvendelseMock implements PSAKHenvendelse {
    @Override
    public ASBOPenHenvendelse lagreHenvendelse(ASBOPenHenvendelse lagreHenvendelseRequest) throws LagreHenvendelseFaultPenGeneriskMsg, LagreHenvendelseFaultPenHenvendelseIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenStatistikk hentStatistikkAntall(ASBOPenHentStatistikkRequest hentStatstikkAntallRequest) throws HentStatistikkAntallFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenStatistikk hentStatistikkHenvendelseGjelder(ASBOPenHentStatistikkRequest hentStatistikkHenvendelseGjelderRequest) throws HentStatistikkHenvendelseGjelderFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenStatistikk hentStatistikkPrType(ASBOPenHentStatistikkRequest hentStatistikkPrTypeRequest) throws HentStatistikkPrTypeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public Boolean erTidsbrukAktivert(ASBOPenHenvendelse erTidsbrukAktivertRequest) throws ErTidsbrukAktivertFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenHenvendelse opprettHenvendelse(ASBOPenHenvendelse opprettHenvendelseRequest) throws OpprettHenvendelseFaultPenGeneriskMsg {
        return opprettHenvendelseRequest;
    }

    @Override
    public ASBOPenStatistikk hentStatistikkPrKanal(ASBOPenHentStatistikkRequest hentStatistikkPrKanalRequest) throws HentStatistikkPrKanalFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentHenvendelseListe", targetNamespace = "http://nav-cons-pen-psak-henvendelse/no/nav/inf", className = "no.nav.inf.psak.henvendelse.HentHenvendelseListe")
    @ResponseWrapper(localName = "hentHenvendelseListeResponse", targetNamespace = "http://nav-cons-pen-psak-henvendelse/no/nav/inf", className = "no.nav.inf.psak.henvendelse.HentHenvendelseListeResponse")
    @WebResult(name = "hentHenvendelseListeResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelseListe hentHenvendelseListe(
            @WebParam(name = "hentHenvendelseListeRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelse hentHenvendelseListeRequest
    ) throws HentHenvendelseListeFaultPenGeneriskMsg {
        return new ASBOPenHenvendelseListe();
    }

    @Override
    public ASBOPenHenvendelse hentHenvendelse(ASBOPenHenvendelse hentHenvendelseRequest) throws HentHenvendelseFaultPenGeneriskMsg, HentHenvendelseFaultPenHenvendelseIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
