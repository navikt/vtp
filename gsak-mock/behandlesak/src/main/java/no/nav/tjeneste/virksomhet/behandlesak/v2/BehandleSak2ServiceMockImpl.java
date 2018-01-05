package no.nav.tjeneste.virksomhet.behandlesak.v2;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

@Addressing
@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", name = "BehandleSakV2")
@HandlerChain(file = "Handler-chain.xml")
public class BehandleSak2ServiceMockImpl implements BehandleSakV2 {

	private static final Logger LOG = LoggerFactory.getLogger(BehandleSak2ServiceMockImpl.class);

	@WebResult(name = "response", targetNamespace = "")
	@RequestWrapper(localName = "opprettSak", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", className = "no.nav.tjeneste.virksomhet.behandlesak.v2.OpprettSak")
	@WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandlesak/v2/BehandleSak_v2/opprettSakRequest")
	@ResponseWrapper(localName = "opprettSakResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", className = "no.nav.tjeneste.virksomhet.behandlesak.v2.OpprettSakResponse")
	public no.nav.tjeneste.virksomhet.behandlesak.v2.WSOpprettSakResponse opprettSak(
			@WebParam(name = "opprettSakRequest", targetNamespace = "") no.nav.tjeneste.virksomhet.behandlesak.v2.WSOpprettSakRequest opprettSakRequest)
			throws WSSikkerhetsbegrensningException, WSSakEksistererAlleredeException, WSUgyldigInputException {
		LOG.info("Oppretter Sak_V2: {}", opprettSakRequest);
		WSOpprettSakResponse response = new WSOpprettSakResponse();
		response.setSakId(lagSakId());
		return response;
	}

	@RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", className = "no.nav.tjeneste.virksomhet.behandlesak.v2.Ping")
	@WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandlesak/v2/BehandleSak_v2/pingRequest")
	@ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", className = "no.nav.tjeneste.virksomhet.behandlesak.v2.PingResponse")
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
