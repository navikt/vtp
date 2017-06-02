package no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1;

import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.*;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.meldinger.FerdigstillJournalfoeringRequest;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.meldinger.OppdaterJournalpostRequest;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(
        name = "BehandleInngaaendeJournal_v1",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1"
)
public class BehandleInngaaendeJournalServiceMockImpl implements BehandleInngaaendeJournalV1 {

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1/BehandleInngaaendeJournal_v1/pingRequest"
    )
    @RequestWrapper(
            localName = "ping",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.Ping"
    )
    @ResponseWrapper(
            localName = "pingResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.PingResponse"
    )
    @Override
    public void ping() {
        // ikke noe
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1/BehandleInngaaendeJournal_v1/ferdigstillJournalfoeringRequest"
    )
    @RequestWrapper(
            localName = "ferdigstillJournalfoering",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.FerdigstillJournalfoering"
    )
    @ResponseWrapper(
            localName = "ferdigstillJournalfoeringResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.FerdigstillJournalfoeringResponse"
    )
    @Override
    public void ferdigstillJournalfoering(@WebParam(name = "request",targetNamespace = "") FerdigstillJournalfoeringRequest request)
            throws FerdigstillJournalfoeringFerdigstillingIkkeMulig, FerdigstillJournalfoeringJournalpostIkkeInngaaende,
                FerdigstillJournalfoeringObjektIkkeFunnet, FerdigstillJournalfoeringSikkerhetsbegrensning, FerdigstillJournalfoeringUgyldigInput {

        //TODO (rune) ...
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1/BehandleInngaaendeJournal_v1/oppdaterJournalpostRequest"
    )
    @RequestWrapper(
            localName = "oppdaterJournalpost",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.OppdaterJournalpost"
    )
    @ResponseWrapper(
            localName = "oppdaterJournalpostResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.OppdaterJournalpostResponse"
    )
    @Override
    public void oppdaterJournalpost(@WebParam(name = "request",targetNamespace = "") OppdaterJournalpostRequest request)
            throws OppdaterJournalpostJournalpostIkkeInngaaende, OppdaterJournalpostObjektIkkeFunnet,
                OppdaterJournalpostOppdateringIkkeMulig, OppdaterJournalpostSikkerhetsbegrensning, OppdaterJournalpostUgyldigInput {

        //TODO (rune) ...
    }
}

