package no.nav.tjeneste.virksomhet.arbeidsfordeling.v1;

import java.util.Arrays;
import java.util.List;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.informasjon.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.ArbeidsfordelingV1;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.FinnAlleBehandlendeEnheterListeUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.FinnBehandlendeEnhetListeUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnAlleBehandlendeEnheterListeRequest;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnAlleBehandlendeEnheterListeResponse;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnBehandlendeEnhetListeRequest;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnBehandlendeEnhetListeResponse;

@Addressing
@WebService(name = "Arbeidsfordeling_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/")
@HandlerChain(file = "Handler-chain.xml")
public class ArbeidsfordelingMockImpl implements ArbeidsfordelingV1 {

    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsfordelingMockImpl.class);
    private TestscenarioBuilderRepository repo;

    public ArbeidsfordelingMockImpl(TestscenarioBuilderRepository repo) {
        this.repo = repo;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnBehandlendeEnhetListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnBehandlendeEnhetListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListe")
    @ResponseWrapper(localName = "finnBehandlendeEnhetListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListeResponse")
    @Override
    public FinnBehandlendeEnhetListeResponse finnBehandlendeEnhetListe(
                                                                       @WebParam(name = "request", targetNamespace = "") FinnBehandlendeEnhetListeRequest request)
            throws FinnBehandlendeEnhetListeUgyldigInput {
        LOG.info("finnBehandlendeEnhetListe. Diskresjonskode: {}, tema: {}", getKodeverdi(request.getArbeidsfordelingKriterier().getDiskresjonskode()),
                getKodeverdi(request.getArbeidsfordelingKriterier().getTema()));
        Diskresjonskoder diskrKode = request.getArbeidsfordelingKriterier().getDiskresjonskode();
        String diskrKodeStr = (diskrKode != null ? diskrKode.getValue() : null);
        Organisasjonsenhet enhet = lagOrganisasjonsenhet(diskrKodeStr);

        FinnBehandlendeEnhetListeResponse response = new FinnBehandlendeEnhetListeResponse();
        response.getBehandlendeEnhetListe().add(enhet);

        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnAlleBehandlendeEnheterListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnAlleBehandlendeEnheterListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListe")
    @ResponseWrapper(localName = "finnAlleBehandlendeEnheterListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListeResponse")
    @Override
    public FinnAlleBehandlendeEnheterListeResponse finnAlleBehandlendeEnheterListe(
                                                                                   @WebParam(name = "request", targetNamespace = "") FinnAlleBehandlendeEnheterListeRequest request)
            throws FinnAlleBehandlendeEnheterListeUgyldigInput {

        LOG.info("finnAlleBehandlendeEnheterListe. Diskresjonskode: {}, tema: {}", getKodeverdi(request.getArbeidsfordelingKriterier().getDiskresjonskode()),
                getKodeverdi(request.getArbeidsfordelingKriterier().getTema()));
        FinnAlleBehandlendeEnheterListeResponse response = new FinnAlleBehandlendeEnheterListeResponse();
        repo.getEnheterIndeks().getAlleEnheter().forEach(e -> response.getBehandlendeEnhetListe().add(lagEnhet(e)));
        return response;
    }

    private Organisasjonsenhet lagOrganisasjonsenhet(String diskrKode) {

        List<String> spesielleDiskrKoder = Arrays.asList("UFB", "SPSF", "SPFO");

        Norg2Modell modell;
        if (diskrKode != null && spesielleDiskrKoder.contains(diskrKode)) {
            modell = repo.getEnheterIndeks().finnByDiskresjonskode(diskrKode);
        } else {
            modell = repo.getEnheterIndeks().finnByDiskresjonskode("NORMAL");
        }

        return lagEnhet(modell);
    }

    private Organisasjonsenhet lagEnhet(Norg2Modell modell) {
        Organisasjonsenhet enhet = new Organisasjonsenhet();
        enhet.setEnhetId(modell.getEnhetId());
        enhet.setEnhetNavn(modell.getNavn());
        enhet.setOrganisasjonsnummer(modell.getOrganisasjonsnummer());

        String status = modell.getStatus();
        if (status != null) {
            enhet.setStatus(Enhetsstatus.fromValue(status));
        }

        String typeStr = modell.getType();
        if (typeStr != null) {
            Enhetstyper type = new Enhetstyper();
            type.setValue(typeStr);
            enhet.setType(type);
        }

        return enhet;
    }

    private String getKodeverdi(Kodeverdi kodeverdi){
        if(kodeverdi == null){
            return "null";
        } else {
            return kodeverdi.getValue();
        }
    }

}
