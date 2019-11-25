package no.nav.tjeneste.virksomhet.arena.ytelseskontrakt;

import no.nav.tjeneste.virksomhet.arena.arbeidsevnevurdering.ArbeidsevnevurderingMockImpl;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v2.binding.HentYtelseskontraktListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v2.binding.YtelseskontraktV2;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v2.informasjon.ytelseskontrakt.Bruker;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v2.informasjon.ytelseskontrakt.Rettighetsgruppe;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v2.informasjon.ytelseskontrakt.Ytelseskontrakt;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v2.meldinger.HentYtelseskontraktListeRequest;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v2.meldinger.HentYtelseskontraktListeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.HandlerChain;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.soap.Addressing;


@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v2.binding.YtelseskontraktV2")
@HandlerChain(file = "Handler-chain.xml")
public class YtelseskontraktV2MockImpl implements YtelseskontraktV2 {
    private static final Logger LOG = LoggerFactory.getLogger(YtelseskontraktV2MockImpl.class);


    @Override
    @WebResult(name = "response", targetNamespace = "")
    public HentYtelseskontraktListeResponse hentYtelseskontraktListe(@WebParam(name = "request", targetNamespace = "") HentYtelseskontraktListeRequest hentYtelseskontraktListeRequest) throws HentYtelseskontraktListeSikkerhetsbegrensning {
        LOG.info("YtelseskontraktV2:hentYtelseskontraktListe kalt");

        String personidentifikator = hentYtelseskontraktListeRequest.getPersonidentifikator();


        Ytelseskontrakt ytelseskontrakt = new Ytelseskontrakt();
        //Fyll ut ytelseskontrakt + map til modell

        HentYtelseskontraktListeResponse response = new HentYtelseskontraktListeResponse();
        response.getYtelseskontraktListe().add(ytelseskontrakt);

        Rettighetsgruppe rettighetsgruppe = new Rettighetsgruppe();
        rettighetsgruppe.setRettighetsGruppe("Gruppe");

        Bruker bruker = new Bruker();
        bruker.setRettighetsgruppe(rettighetsgruppe);

        response.setBruker(bruker);

        return response;
    }

    @Override
    public void ping() {
        LOG.info("YtelseskontraktV2:ping kalt");
    }
}
