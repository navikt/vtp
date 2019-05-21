package no.nav.tjeneste.virksomhet.behandlejournal.v3;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.ArkiverUstrukturertKravSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.BehandleJournalV3;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.FerdigstillDokumentopplastingFerdigstillDokumentopplastingjournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.FerdigstillDokumentopplastingSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.JournalfoerInngaaendeHenvendelseSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.JournalfoerNotatSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.JournalfoerUtgaaendeHenvendelseSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.LagreVedleggPaaJournalpostLagreVedleggPaaJournalpostjournalpostIkkeFunnet;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.LagreVedleggPaaJournalpostSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.ArkiverUstrukturertKravRequest;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.ArkiverUstrukturertKravResponse;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.FerdigstillDokumentopplastingRequest;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.JournalfoerInngaaendeHenvendelseRequest;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.JournalfoerInngaaendeHenvendelseResponse;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.JournalfoerNotatRequest;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.JournalfoerNotatResponse;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.JournalfoerUtgaaendeHenvendelseRequest;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.JournalfoerUtgaaendeHenvendelseResponse;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.LagreVedleggPaaJournalpostRequest;
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.LagreVedleggPaaJournalpostResponse;


@Addressing
@HandlerChain(file="Handler-chain.xml")
@WebService(
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        name = "behandleJournal_v3"
)
public class BehandleJournalV3ServiceMockImpl implements BehandleJournalV3 {
    Logger LOG = LoggerFactory.getLogger(BehandleJournalV3ServiceMockImpl.class);



    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/ferdigstillDokumentopplastingRequest"
    )
    @RequestWrapper(
            localName = "ferdigstillDokumentopplasting",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.FerdigstillDokumentopplasting"
    )
    @ResponseWrapper(
            localName = "ferdigstillDokumentopplastingResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.FerdigstillDokumentopplastingResponse"
    )
    @Override
    public void ferdigstillDokumentopplasting(@WebParam(name = "ferdigstillDokumentopplastingRequest", targetNamespace = "") FerdigstillDokumentopplastingRequest request) throws FerdigstillDokumentopplastingSikkerhetsbegrensning, FerdigstillDokumentopplastingFerdigstillDokumentopplastingjournalpostIkkeFunnet {
        LOG.info("invoke: ferdigstillDokumentopplasting");

    }



    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/journalfoerNotatRequest"
    )
    @RequestWrapper(
            localName = "journalfoerNotat",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerNotat"
    )
    @ResponseWrapper(
            localName = "journalfoerNotatResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerNotatResponse"
    )
    @WebResult(
            name = "journalfoerNotatResponse",
            targetNamespace = ""
    )
    @Override
    public JournalfoerNotatResponse journalfoerNotat(@WebParam(name = "journalfoerNotatRequest", targetNamespace = "") JournalfoerNotatRequest request) throws JournalfoerNotatSikkerhetsbegrensning {
        LOG.info("invoke: journalfoerNotat");
        throw new UnsupportedOperationException("Not implemented");

    }

    @Override
    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/arkiverUstrukturertKravRequest"
    )
    @RequestWrapper(
            localName = "arkiverUstrukturertKrav",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.ArkiverUstrukturertKrav"
    )
    @ResponseWrapper(
            localName = "arkiverUstrukturertKravResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.ArkiverUstrukturertKravResponse"
    )
    @WebResult(
            name = "arkiverUstrukturertKravResponse",
            targetNamespace = ""
    )
    public ArkiverUstrukturertKravResponse arkiverUstrukturertKrav(@WebParam(name = "arkiverUstrukturertKravRequest", targetNamespace = "") ArkiverUstrukturertKravRequest request) throws ArkiverUstrukturertKravSikkerhetsbegrensning {
        LOG.info("invoke: arkiverUstrukturertKrav");
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/journalfoerUtgaaendeHenvendelseRequest"
    )
    @RequestWrapper(
            localName = "journalfoerUtgaaendeHenvendelse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerUtgaaendeHenvendelse"
    )
    @ResponseWrapper(
            localName = "journalfoerUtgaaendeHenvendelseResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerUtgaaendeHenvendelseResponse"
    )
    @WebResult(
            name = "journalfoerUtgaaendeHenvendelseResponse",
            targetNamespace = ""
    )
    public JournalfoerUtgaaendeHenvendelseResponse journalfoerUtgaaendeHenvendelse(@WebParam(name = "journalfoerUtgaaendeHenvendelseRequest", targetNamespace = "") JournalfoerUtgaaendeHenvendelseRequest request) throws JournalfoerUtgaaendeHenvendelseSikkerhetsbegrensning {
        LOG.info("invoke: JournalfoerUtgaaendeHenvendelseSikkerhetsbegrensning");
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/lagreVedleggPaaJournalpostRequest"
    )
    @RequestWrapper(
            localName = "lagreVedleggPaaJournalpost",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.LagreVedleggPaaJournalpost"
    )
    @ResponseWrapper(
            localName = "lagreVedleggPaaJournalpostResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.LagreVedleggPaaJournalpostResponse"
    )
    @WebResult(
            name = "lagreVedleggPaaJournalpostResponse",
            targetNamespace = ""
    )
    public LagreVedleggPaaJournalpostResponse lagreVedleggPaaJournalpost(@WebParam(name = "lagreVedleggPaaJournalpostRequest", targetNamespace = "") LagreVedleggPaaJournalpostRequest request) throws LagreVedleggPaaJournalpostLagreVedleggPaaJournalpostjournalpostIkkeFunnet, LagreVedleggPaaJournalpostSikkerhetsbegrensning {
        LOG.info("invoke: lagreVedleggPaaJournalpost");
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/pingRequest"
    )
    @RequestWrapper(
            localName = "ping",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.Ping"
    )
    @ResponseWrapper(
            localName = "pingResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.PingResponse"
    )
    public void ping() {
        LOG.info("Ping response");
    }

    @Override
    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/journalfoerInngaaendeHenvendelseRequest"
    )
    @RequestWrapper(
            localName = "journalfoerInngaaendeHenvendelse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerInngaaendeHenvendelse"
    )
    @ResponseWrapper(
            localName = "journalfoerInngaaendeHenvendelseResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
            className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerInngaaendeHenvendelseResponse"
    )
    @WebResult(
            name = "journalfoerInngaaendeHenvendelseResponse",
            targetNamespace = ""
    )
    public JournalfoerInngaaendeHenvendelseResponse journalfoerInngaaendeHenvendelse(@WebParam(name = "journalfoerInngaaendeHenvendelseRequest", targetNamespace = "") JournalfoerInngaaendeHenvendelseRequest request) throws JournalfoerInngaaendeHenvendelseSikkerhetsbegrensning {
        LOG.info("invoke: journalfoerInngaaendeHenvendelse");
        throw new UnsupportedOperationException("Not implemented");
    }
}
