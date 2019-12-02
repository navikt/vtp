package no.nav.tjeneste.virksomhet.innsynjournal.v2;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.tjeneste.virksomhet.innsynjournal.v2.binding.HentDokumentDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.binding.HentDokumentSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.binding.HentTilgjengeligJournalpostListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.binding.IdentifiserJournalpostJournalpostIkkeInngaaende;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.binding.IdentifiserJournalpostObjektIkkeFunnet;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.binding.IdentifiserJournalpostUgyldigAntallJournalposter;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.binding.IdentifiserJournalpostUgyldingInput;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.binding.InnsynJournalV2;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.meldinger.HentDokumentRequest;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.meldinger.HentDokumentResponse;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.meldinger.HentTilgjengeligJournalpostListeRequest;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.meldinger.HentTilgjengeligJournalpostListeResponse;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.meldinger.IdentifiserJournalpostRequest;
import no.nav.tjeneste.virksomhet.innsynjournal.v2.meldinger.IdentifiserJournalpostResponse;

@Addressing
@WebService(name = "InnsynJournal_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2")
@HandlerChain(file="Handler-chain.xml")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class InnsynJournalServiceMockImpl implements InnsynJournalV2 {
    private static final Logger LOG = LoggerFactory.getLogger(InnsynJournalServiceMockImpl.class);

    private static final String JOURNALPOST_ID_MOCK = "12345678";

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2/InnsynJournal_v2/hentDokumentRequest")
    @RequestWrapper(localName = "hentDokument", targetNamespace = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2", className = "no.nav.tjeneste.virksomhet.innsynjournal.v2.HentDokument")
    @ResponseWrapper(localName = "hentDokumentResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2", className = "no.nav.tjeneste.virksomhet.innsynjournal.v2.HentDokumentResponse")
    @WebResult(name = "Response", targetNamespace = "")
    public HentDokumentResponse hentDokument(HentDokumentRequest hentDokumentRequest) throws HentDokumentDokumentIkkeFunnet, HentDokumentSikkerhetsbegrensning {
        return null;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2/InnsynJournal_v2/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2", className = "no.nav.tjeneste.virksomhet.innsynjournal.v2.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2", className = "no.nav.tjeneste.virksomhet.innsynjournal.v2.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2/InnsynJournal_v2/identifiserJournalpostRequest")
    @RequestWrapper(localName = "identifiserJournalpost", targetNamespace = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2", className = "no.nav.tjeneste.virksomhet.innsynjournal.v2.IdentifiserJournalpost")
    @ResponseWrapper(localName = "identifiserJournalpostResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2", className = "no.nav.tjeneste.virksomhet.innsynjournal.v2.IdentifiserJournalpostResponse")
    @WebResult(name = "Response", targetNamespace = "")
    public IdentifiserJournalpostResponse identifiserJournalpost(IdentifiserJournalpostRequest identifiserJournalpostRequest) throws IdentifiserJournalpostJournalpostIkkeInngaaende, IdentifiserJournalpostUgyldigAntallJournalposter, IdentifiserJournalpostObjektIkkeFunnet, IdentifiserJournalpostUgyldingInput {
        IdentifiserJournalpostResponse response = new IdentifiserJournalpostResponse();
        response.setJournalpostId(JOURNALPOST_ID_MOCK);
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2/InnsynJournal_v2/hentTilgjengeligJournalpostListeRequest")
    @RequestWrapper(localName = "hentTilgjengeligJournalpostListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2", className = "no.nav.tjeneste.virksomhet.innsynjournal.v2.HentTilgjengeligJournalpostListe")
    @ResponseWrapper(localName = "hentTilgjengeligJournalpostListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/innsynJournal/v2", className = "no.nav.tjeneste.virksomhet.innsynjournal.v2.HentTilgjengeligJournalpostListeResponse")
    @WebResult(name = "Response", targetNamespace = "")
    public HentTilgjengeligJournalpostListeResponse hentTilgjengeligJournalpostListe(HentTilgjengeligJournalpostListeRequest hentTilgjengeligJournalpostListeRequest) throws HentTilgjengeligJournalpostListeSikkerhetsbegrensning {
        return null;
    }
}
