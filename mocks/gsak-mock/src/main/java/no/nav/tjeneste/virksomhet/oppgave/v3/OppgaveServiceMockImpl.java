package no.nav.tjeneste.virksomhet.oppgave.v3;


import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import no.nav.tjeneste.virksomhet.oppgave.v3.binding.HentOppgaveOppgaveIkkeFunnet;
import no.nav.tjeneste.virksomhet.oppgave.v3.binding.OppgaveV3;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Fagomrade;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Oppgave;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.*;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnFeilregistrertOppgaveListeResponse;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnFerdigstiltOppgaveListeResponse;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnMappeListeResponse;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnOppgaveListeResponse;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.HentOppgaveResponse;


@Addressing
@WebService(name = "Oppgave_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3")
@HandlerChain(file="Handler-chain.xml")
public class OppgaveServiceMockImpl implements OppgaveV3 {

    private static final Logger LOG = LoggerFactory.getLogger(OppgaveServiceMockImpl.class);

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/hentOppgave")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.HentOppgave")
    @ResponseWrapper(localName = "hentOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.HentOppgaveResponse")
    public HentOppgaveResponse hentOppgave(@WebParam(name = "request", targetNamespace = "") HentOppgaveRequest hentOppgaveRequest) throws HentOppgaveOppgaveIkkeFunnet {
        LOG.info("hentOppgave. OppgaveId: {}", hentOppgaveRequest.getOppgaveId());
        HentOppgaveResponse hentOppgaveResponse = new HentOppgaveResponse();
        Oppgave oppgave = new Oppgave();
        oppgave.setBeskrivelse("Liksomoppgave");
        Fagomrade fagomrade = new Fagomrade();
        fagomrade.setKode("FOR");
        oppgave.setFagomrade(fagomrade);
        hentOppgaveResponse.setOppgave(oppgave);
        return hentOppgaveResponse;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnOppgaveListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnOppgaveListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnOppgaveListe")
    @ResponseWrapper(localName = "finnOppgaveListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnOppgaveListeResponse")
    public FinnOppgaveListeResponse finnOppgaveListe(@WebParam(name = "request", targetNamespace = "") FinnOppgaveListeRequest finnOppgaveListeRequest) {
        LOG.info("finnOppgaveListe. Søk: ansvarligEnhetId: {}, brukerId: {}, sakId: {}, søknadsId: {}", finnOppgaveListeRequest.getSok().getAnsvarligEnhetId(), finnOppgaveListeRequest.getSok().getBrukerId(),
            finnOppgaveListeRequest.getSok().getSakId(), finnOppgaveListeRequest.getSok().getSoknadsId());
        FinnOppgaveListeResponse finnOppgaveListeResponse = new FinnOppgaveListeResponse();
        finnOppgaveListeResponse.setTotaltAntallTreff(0);
        return finnOppgaveListeResponse;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnFerdigstiltOppgaveListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnFerdigstiltOppgaveListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFerdigstiltOppgaveListe")
    @ResponseWrapper(localName = "finnFerdigstiltOppgaveListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFerdigstiltOppgaveListeResponse")
    public FinnFerdigstiltOppgaveListeResponse finnFerdigstiltOppgaveListe(@WebParam(name = "request", targetNamespace = "") FinnFerdigstiltOppgaveListeRequest finnFerdigstiltOppgaveListeRequest) {
        LOG.info("finnFerdigstiltOppgaveListe. Søk: ansvarligEnhetId: {}, brukerId: {}, sakId: {}, søknadsId: {}", finnFerdigstiltOppgaveListeRequest.getSok().getAnsvarligEnhetId(), finnFerdigstiltOppgaveListeRequest.getSok().getBrukerId(),
                finnFerdigstiltOppgaveListeRequest.getSok().getSakId(), finnFerdigstiltOppgaveListeRequest.getSok().getSoknadsId());
        FinnFerdigstiltOppgaveListeResponse  finnFerdigstiltOppgaveListeResponse = new FinnFerdigstiltOppgaveListeResponse();
        return finnFerdigstiltOppgaveListeResponse;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnFeilregistrertOppgaveListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnFeilregistrertOppgaveListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFeilregistrertOppgaveListe")
    @ResponseWrapper(localName = "finnFeilregistrertOppgaveListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFeilregistrertOppgaveListeResponse")
    public FinnFeilregistrertOppgaveListeResponse finnFeilregistrertOppgaveListe(@WebParam(name = "request", targetNamespace = "") FinnFeilregistrertOppgaveListeRequest finnFeilregistrertOppgaveListeRequest) {
        LOG.info("finnFeilregistrertOppgaveListe. Søk: ansvarligEnhetId: {}, brukerId: {}, sakId: {}, aøknadsId{}", finnFeilregistrertOppgaveListeRequest.getSok().getAnsvarligEnhetId(), finnFeilregistrertOppgaveListeRequest.getSok().getBrukerId(),
                finnFeilregistrertOppgaveListeRequest.getSok().getSakId(), finnFeilregistrertOppgaveListeRequest.getSok().getSoknadsId());
        FinnFeilregistrertOppgaveListeResponse finnFeilregistrertOppgaveListeResponse = new FinnFeilregistrertOppgaveListeResponse();
        return finnFeilregistrertOppgaveListeResponse;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnMappeListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnMappeListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnMappeListe")
    @ResponseWrapper(localName = "finnMappeListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnMappeListeResponse")
    public FinnMappeListeResponse finnMappeListe(@WebParam(name = "request", targetNamespace = "") FinnMappeListeRequest finnMappeListeRequest) {
        LOG.info("finnMappeListe. EnhetId: {}", finnMappeListeRequest.getEnhetId());
        return new FinnMappeListeResponse();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }
}
