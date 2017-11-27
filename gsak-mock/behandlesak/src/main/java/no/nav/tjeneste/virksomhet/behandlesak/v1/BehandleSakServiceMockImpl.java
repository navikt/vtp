package no.nav.tjeneste.virksomhet.behandlesak.v1;

import java.text.SimpleDateFormat;
import java.util.Date;

import no.nav.tjeneste.virksomhet.behandlesak.v1.binding.BehandleSakV1;
import no.nav.tjeneste.virksomhet.behandlesak.v1.binding.OpprettSakSakEksistererAllerede;
import no.nav.tjeneste.virksomhet.behandlesak.v1.binding.OpprettSakUgyldigInput;
import no.nav.tjeneste.virksomhet.behandlesak.v1.meldinger.OpprettSakRequest;
import no.nav.tjeneste.virksomhet.behandlesak.v1.meldinger.OpprettSakResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

@Addressing
@WebService(name = "BehandleSak_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1")
public class BehandleSakServiceMockImpl implements BehandleSakV1 {

    private static final Logger LOG = LoggerFactory.getLogger(BehandleSakServiceMockImpl.class);

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleSak/v1/BehandleSak_v1/opprettSakRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "opprettSak", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1", className = "no.nav.tjeneste.virksomhet.behandlesak.v1.OpprettSak")
    @ResponseWrapper(localName = "opprettSakResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1", className = "no.nav.tjeneste.virksomhet.behandlesak.v1.OpprettSakResponse")
    public OpprettSakResponse opprettSak(@WebParam(name = "request", targetNamespace = "") OpprettSakRequest opprettSakRequest) throws OpprettSakSakEksistererAllerede, OpprettSakUgyldigInput {
        LOG.info("Oppretter Sak: {}", opprettSakRequest);
        OpprettSakResponse response = new OpprettSakResponse();
        response.setSakId(lagSakId());
        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleSak/v1/BehandleSak_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1", className = "no.nav.tjeneste.virksomhet.behandlesak.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleSak/v1", className = "no.nav.tjeneste.virksomhet.behandlesak.v1.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    private String lagSakId() {

        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmssSSS");
        Date now = new Date();
        String timeStr = sdf.format(now);

        return "13" + timeStr.substring(0, timeStr.length() - 2);
    }

}
