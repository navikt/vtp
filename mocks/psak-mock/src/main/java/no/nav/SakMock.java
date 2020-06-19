package no.nav;

import no.nav.virksomhet.tjenester.sak.meldinger.v1.FinnGenerellSakListeResponse;
import no.nav.virksomhet.tjenester.sak.v1.ObjectFactory;
import no.nav.virksomhet.tjenester.sak.v1.Sak;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/sak/v1", name = "Sak")
@XmlSeeAlso({ObjectFactory.class, no.nav.virksomhet.tjenester.sak.meldinger.v1.ObjectFactory.class, no.nav.virksomhet.gjennomforing.sak.v1.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class SakMock implements Sak {

    @Override
    @WebMethod(action = "http://nav.no/virksomhet/tjenester/sak/v1/Sak/finnGenerellSakListeRequest")
    @RequestWrapper(localName = "finnGenerellSakListe", targetNamespace = "http://nav.no/virksomhet/tjenester/sak/v1", className = "no.nav.virksomhet.tjenester.sak.v1.FinnGenerellSakListe")
    @ResponseWrapper(localName = "finnGenerellSakListeResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/sak/v1", className = "no.nav.virksomhet.tjenester.sak.v1.FinnGenerellSakListeResponse")
    @WebResult(name = "response", targetNamespace = "")
    public no.nav.virksomhet.tjenester.sak.meldinger.v1.FinnGenerellSakListeResponse finnGenerellSakListe(
            @WebParam(name = "request", targetNamespace = "")
                    no.nav.virksomhet.tjenester.sak.meldinger.v1.FinnGenerellSakListeRequest request
    ) {
        return new FinnGenerellSakListeResponse();
    }
}
