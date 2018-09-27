package no.nav.tjeneste.virksomhet.organisasjon.v4;

import java.util.Optional;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
// import javax.persistence.EntityManager;
// import javax.persistence.Persistence;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.FinnOrganisasjonForMangeForekomster;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.FinnOrganisasjonUgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.HentOrganisasjonOrganisasjonIkkeFunnet;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.HentOrganisasjonUgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.HentNoekkelinfoOrganisasjonOrganisasjonIkkeFunnet;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.HentNoekkelinfoOrganisasjonUgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.ValiderOrganisasjonOrganisasjonIkkeFunnet;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.ValiderOrganisasjonUgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.FinnOrganisasjonsendringerListeUgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.OrganisasjonV4;
import no.nav.tjeneste.virksomhet.organisasjon.v4.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Organisasjon;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.FinnOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.FinnOrganisasjonResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentOrganisasjonsnavnBolkRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentOrganisasjonsnavnBolkResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentOrganisasjonResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentNoekkelinfoOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentNoekkelinfoOrganisasjonResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.ValiderOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.ValiderOrganisasjonResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.FinnOrganisasjonsendringerListeRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.FinnOrganisasjonsendringerListeResponse;


@Addressing
@WebService(name = "Organisasjon_v4", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4")
@HandlerChain(file="Handler-chain.xml")
public class OrganisasjonMockImpl implements OrganisasjonV4 {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisasjonMockImpl.class);

    private TestscenarioBuilderRepository scenarioRepository;

    public OrganisasjonMockImpl() {}

    public OrganisasjonMockImpl(TestscenarioBuilderRepository scenarioRepository) {this.scenarioRepository = scenarioRepository;}

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/finnOrganisasjonRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnOrganisasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjon")
    @ResponseWrapper(localName = "finnOrganisasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjonResponse")
    public FinnOrganisasjonResponse finnOrganisasjon(@WebParam(name = "request",targetNamespace = "") FinnOrganisasjonRequest finnOrganisasjonRequest) throws FinnOrganisasjonForMangeForekomster, FinnOrganisasjonUgyldigInput {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4", className = "no.nav.tjeneste.virksomhet.organisasjon.v4.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4", className = "no.nav.tjeneste.virksomhet.organisasjon.v4.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentOrganisasjonsnavnBolkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentOrganisasjonsnavnBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjonsnavnBolk")
    @ResponseWrapper(localName = "hentOrganisasjonsnavnBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjonsnavnBolkResponse")
    public HentOrganisasjonsnavnBolkResponse hentOrganisasjonsnavnBolk(@WebParam(name = "request",targetNamespace = "") HentOrganisasjonsnavnBolkRequest hentOrganisasjonsnavnBolkRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentOrganisasjonRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentOrganisasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjon")
    @ResponseWrapper(localName = "hentOrganisasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjonResponse")
    public HentOrganisasjonResponse hentOrganisasjon(@WebParam(name = "request",targetNamespace = "") HentOrganisasjonRequest request) throws HentOrganisasjonOrganisasjonIkkeFunnet, HentOrganisasjonUgyldigInput{

        LOG.info("hentOrganisasjon. Orgnummer: {}", request.getOrgnummer());
        if (request != null && request.getOrgnummer() != null) {
            HentOrganisasjonResponse response = new HentOrganisasjonResponse();
            OrganisasjonGenerator orggen = new OrganisasjonGenerator();
            //response.setOrganisasjon(orggen.lagOrganisasjon(request.getOrgnummer()));
            Optional<OrganisasjonModell> organisasjonModell = scenarioRepository.getOrganisasjon(request.getOrgnummer());
            if (organisasjonModell.isPresent()) {
                OrganisasjonModell modell = organisasjonModell.get();
                Organisasjon organisasjon = OrganisasjonsMapper.mapOrganisasjonFraModell(modell);
                response.setOrganisasjon(organisasjon);
            }
            return response;
        } else {
            throw new HentOrganisasjonUgyldigInput("Orgnummer ikke angitt", new UgyldigInput());
        }
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentNoekkelinfoOrganisasjonRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentNoekkelinfoOrganisasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentNoekkelinfoOrganisasjon")
    @ResponseWrapper(localName = "hentNoekkelinfoOrganisasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentNoekkelinfoOrganisasjonResponse")
    public HentNoekkelinfoOrganisasjonResponse hentNoekkelinfoOrganisasjon(@WebParam(name = "request",targetNamespace = "") HentNoekkelinfoOrganisasjonRequest request) throws HentNoekkelinfoOrganisasjonOrganisasjonIkkeFunnet, HentNoekkelinfoOrganisasjonUgyldigInput{

        LOG.info("hentNoekkelinfoOrganisasjon. Orgnummer: {}", request.getOrgnummer());
        if (request != null && request.getOrgnummer() != null) {

            HentNoekkelinfoOrganisasjonResponse response = new HentNoekkelinfoOrganisasjonResponse();
            OrganisasjonGenerator orggen = new OrganisasjonGenerator();
            Organisasjon org = orggen.lagOrganisasjon(request.getOrgnummer());

            response.setOrgnummer(request.getOrgnummer());
            response.setNavn(org.getNavn());
            response.setEnhetstype(orggen.lagEnhetstype(request.getOrgnummer()));
            return response;
        } else {
            throw new HentNoekkelinfoOrganisasjonUgyldigInput("Orgnummer ikke angitt", new UgyldigInput());
        }
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/validerOrganisasjonRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "validerOrganisasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.ValiderOrganisasjon")
    @ResponseWrapper(localName = "validerOrganisasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.ValiderOrganisasjonResponse")
    public ValiderOrganisasjonResponse validerOrganisasjon(@WebParam(name = "request",targetNamespace = "") ValiderOrganisasjonRequest request) throws ValiderOrganisasjonOrganisasjonIkkeFunnet, ValiderOrganisasjonUgyldigInput{
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentVirksomhetsOrgnrForJuridiskOrgnrBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentVirksomhetsOrgnrForJuridiskOrgnrBolk")
    @ResponseWrapper(localName = "hentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse")
    public HentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse hentVirksomhetsOrgnrForJuridiskOrgnrBolk(@WebParam(name = "request",targetNamespace = "") HentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest hentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest)  {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/finnOrganisasjonsendringerListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnOrganisasjonsendringerListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjonsendringerListe")
    @ResponseWrapper(localName = "finnOrganisasjonsendringerListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjonsendringerListeResponse")
    public FinnOrganisasjonsendringerListeResponse finnOrganisasjonsendringerListe(@WebParam(name = "request",targetNamespace = "") FinnOrganisasjonsendringerListeRequest finnOrganisasjonsendringerListeRequest) throws FinnOrganisasjonsendringerListeUgyldigInput {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
