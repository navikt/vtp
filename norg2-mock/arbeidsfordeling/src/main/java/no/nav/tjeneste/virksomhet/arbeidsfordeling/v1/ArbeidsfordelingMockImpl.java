package no.nav.tjeneste.virksomhet.arbeidsfordeling.v1;

import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.ArbeidsfordelingV1;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.FinnAlleBehandlendeEnheterListeUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.FinnBehandlendeEnhetListeUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.*;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnAlleBehandlendeEnheterListeResponse;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnBehandlendeEnhetListeResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

@Addressing
@WebService(
        name = "Arbeidsfordeling_v1",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/"
)
public class ArbeidsfordelingMockImpl implements ArbeidsfordelingV1 {

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnBehandlendeEnhetListeRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "finnBehandlendeEnhetListe",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListe"
    )
    @ResponseWrapper(
            localName = "finnBehandlendeEnhetListeResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListeResponse"
    )
    @Override
    public FinnBehandlendeEnhetListeResponse finnBehandlendeEnhetListe(
            @WebParam(name = "request",targetNamespace = "") FinnBehandlendeEnhetListeRequest request)
            throws FinnBehandlendeEnhetListeUgyldigInput {
        //TODO (rune)
        return null;
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/pingRequest"
    )
    @RequestWrapper(
            localName = "ping",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.Ping"
    )
    @ResponseWrapper(
            localName = "pingResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.PingResponse"
    )
    @Override
    public void ping() {
        //TODO (rune)
        int brkpt = 1;
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnAlleBehandlendeEnheterListeRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "finnAlleBehandlendeEnheterListe",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListe"
    )
    @ResponseWrapper(
            localName = "finnAlleBehandlendeEnheterListeResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListeResponse"
    )
    @Override
    public FinnAlleBehandlendeEnheterListeResponse finnAlleBehandlendeEnheterListe(
            @WebParam(name = "request",targetNamespace = "") FinnAlleBehandlendeEnheterListeRequest request)
            throws FinnAlleBehandlendeEnheterListeUgyldigInput {
        //TODO (rune)
        return null;
    }
}
