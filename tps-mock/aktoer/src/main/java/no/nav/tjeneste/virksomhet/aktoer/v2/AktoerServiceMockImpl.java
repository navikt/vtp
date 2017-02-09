package no.nav.tjeneste.virksomhet.aktoer.v2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.tjeneste.virksomhet.aktoer.v2.binding.AktoerV2;
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentAktoerIdForIdentPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentIdentForAktoerIdPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.AktoerIder;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentListeRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentListeResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdListeRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdListeResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.IdentDetaljer;

@WebService(name = "Aktoer_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2")
public class AktoerServiceMockImpl implements AktoerV2 {

    private static final Logger LOG = LoggerFactory.getLogger(AktoerServiceMockImpl.class);

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentIdentForAktoerIdRequest")
    @WebResult(name = "hentIdentForAktoerIdResponse", targetNamespace = "")
    @RequestWrapper(localName = "hentIdentForAktoerId", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerId")
    @ResponseWrapper(localName = "hentIdentForAktoerIdResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerIdResponse")
    public HentIdentForAktoerIdResponse hentIdentForAktoerId(
            @WebParam(name = "hentIdentForAktoerIdRequest", targetNamespace = "")
            HentIdentForAktoerIdRequest hentIdentForAktoerIdRequest)
            throws HentIdentForAktoerIdPersonIkkeFunnet {
        LOG.info("hentIdentForAktoerId: " + hentIdentForAktoerIdRequest.getAktoerId());
        HentIdentForAktoerIdResponse response = new HentIdentForAktoerIdResponse();
        response.setIdent("123");
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentAktoerIdForIdentRequest")
    @WebResult(name = "hentAktoerIdForIdentResponse", targetNamespace = "")
    @RequestWrapper(localName = "hentAktoerIdForIdent", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdent")
    @ResponseWrapper(localName = "hentAktoerIdForIdentResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdentResponse")
    public HentAktoerIdForIdentResponse hentAktoerIdForIdent(
        @WebParam(name = "hentAktoerIdForIdentRequest", targetNamespace = "")
        HentAktoerIdForIdentRequest hentAktoerIdForIdentRequest)
        throws HentAktoerIdForIdentPersonIkkeFunnet {
        LOG.info("hentIdentForAktoerId: " + hentAktoerIdForIdentRequest.getIdent());
        HentAktoerIdForIdentResponse response = new HentAktoerIdForIdentResponse();
        response.setAktoerId("456");
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentAktoerIdForIdentListeRequest")
    @WebResult(name = "hentAktoerIdForIdentListeResponse", targetNamespace = "")
    @RequestWrapper(localName = "hentAktoerIdForIdentListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdentListe")
    @ResponseWrapper(localName = "hentAktoerIdForIdentListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdentListeResponse")
    public HentAktoerIdForIdentListeResponse hentAktoerIdForIdentListe(
        @WebParam(name = "hentAktoerIdForIdentListeRequest", targetNamespace = "")
        HentAktoerIdForIdentListeRequest hentAktoerIdForIdentListeRequest) {
        LOG.info("hentIdentForAktoerId");
        HentAktoerIdForIdentListeResponse response = new HentAktoerIdForIdentListeResponse();
        AktoerIder aktoerIder = new AktoerIder();
        aktoerIder.setAktoerId("789");
        IdentDetaljer identDetaljer = new IdentDetaljer();
        aktoerIder.setGjeldendeIdent(identDetaljer);
        response.getAktoerListe().add(aktoerIder);
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentIdentForAktoerIdListeRequest")
    @WebResult(name = "hentIdentForAktoerIdListeResponse", targetNamespace = "")
    @RequestWrapper(localName = "hentIdentForAktoerIdListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerIdListe")
    @ResponseWrapper(localName = "hentIdentForAktoerIdListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerIdListeResponse")
    public HentIdentForAktoerIdListeResponse hentIdentForAktoerIdListe(
        @WebParam(name = "hentIdentForAktoerIdListeRequest", targetNamespace = "")
        HentIdentForAktoerIdListeRequest hentIdentForAktoerIdListeRequest) {
        LOG.info("hentIdentForAktoerId");
        HentIdentForAktoerIdListeResponse response = new HentIdentForAktoerIdListeResponse();
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }
}
