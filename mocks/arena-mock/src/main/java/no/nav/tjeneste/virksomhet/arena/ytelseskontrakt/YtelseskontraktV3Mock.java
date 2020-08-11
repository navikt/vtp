package no.nav.tjeneste.virksomhet.arena.ytelseskontrakt;

import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.HentYtelseskontraktListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.ObjectFactory;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.YtelseskontraktV3;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.WSBruker;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.WSRettighetsgruppe;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.WSYtelseskontrakt;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.WSHentYtelseskontraktListeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", name = "Ytelseskontrakt_v3")
@XmlSeeAlso({no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.ObjectFactory.class, no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.metadata.ObjectFactory.class, no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.feil.ObjectFactory.class, ObjectFactory.class, no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class YtelseskontraktV3Mock implements YtelseskontraktV3 {
    private static final Logger LOG = LoggerFactory.getLogger(YtelseskontraktV3Mock.class);

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/Ytelseskontrakt_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.PingResponse")
    public void ping() {
        LOG.info("YtelseskontraktV3Mock:ping kalt");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/Ytelseskontrakt_v3/hentYtelseskontraktListeRequest")
    @RequestWrapper(localName = "hentYtelseskontraktListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.HentYtelseskontraktListe")
    @ResponseWrapper(localName = "hentYtelseskontraktListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.HentYtelseskontraktListeResponse")
    @WebResult(name = "response", targetNamespace = "")
    public no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.WSHentYtelseskontraktListeResponse hentYtelseskontraktListe(
            @WebParam(name = "request", targetNamespace = "")
                    no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.WSHentYtelseskontraktListeRequest request
    ) throws HentYtelseskontraktListeSikkerhetsbegrensning {
        WSYtelseskontrakt ytelseskontrakt = new WSYtelseskontrakt();
        WSHentYtelseskontraktListeResponse response = new WSHentYtelseskontraktListeResponse();
        response.getYtelseskontraktListe().add(ytelseskontrakt);

        WSRettighetsgruppe rettighetsgruppe = new WSRettighetsgruppe();
        rettighetsgruppe.setRettighetsGruppe("Gruppe");
        WSBruker bruker = new WSBruker();
        bruker.setRettighetsgruppe(rettighetsgruppe);
        response.setBruker(bruker);

        return response;
    }
}
