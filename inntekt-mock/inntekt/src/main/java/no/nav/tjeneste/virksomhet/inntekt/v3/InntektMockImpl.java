package no.nav.tjeneste.virksomhet.inntekt.v3;

import java.time.LocalDate;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentAbonnerteInntekterBolkHarIkkeTilgangTilOensketAInntektsfilter;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentAbonnerteInntekterBolkUgyldigInput;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentDetaljerteAbonnerteInntekterHarIkkeTilgangTilOensketAInntektsfilter;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentDetaljerteAbonnerteInntekterManglendeAbonnent;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentDetaljerteAbonnerteInntekterPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentDetaljerteAbonnerteInntekterSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentDetaljerteAbonnerteInntekterUgyldigInput;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentForventetInntektPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentForventetInntektSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentForventetInntektUgyldigInput;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentInntektListeBolkHarIkkeTilgangTilOensketAInntektsfilter;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentInntektListeBolkUgyldigInput;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentInntektListeForOpplysningspliktigHarIkkeTilgangTilOensketAInntektsfilter;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentInntektListeForOpplysningspliktigUgyldigInput;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentInntektListeHarIkkeTilgangTilOensketAInntektsfilter;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentInntektListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentInntektListeUgyldigInput;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.InntektV3;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Aktoer;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.AktoerId;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.PersonIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentAbonnerteInntekterBolkRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentAbonnerteInntekterBolkResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentDetaljerteAbonnerteInntekterRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentDetaljerteAbonnerteInntekterResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentForventetInntektRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentForventetInntektResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeBolkRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeBolkResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeForOpplysningspliktigRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeForOpplysningspliktigResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeResponse;

@Addressing
@WebService(name = "Inntekt_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3")
public class InntektMockImpl implements InntektV3 {

    private static final Logger LOG = LoggerFactory.getLogger(InntektMockImpl.class);
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("inntekt").createEntityManager();


    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentForventetInntektRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentForventetInntekt", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentForventetInntekt")
    @ResponseWrapper(localName = "hentForventetInntektResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentForventetInntektResponse")
    @Override
    public HentForventetInntektResponse hentForventetInntekt(@WebParam(name = "request",targetNamespace = "") HentForventetInntektRequest hentForventetInntektRequest) throws HentForventetInntektPersonIkkeFunnet, HentForventetInntektSikkerhetsbegrensning, HentForventetInntektUgyldigInput {
        return null;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.PingResponse")
    @Override
    public void ping() {
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentInntektListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentInntektListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListe")
    @ResponseWrapper(localName = "hentInntektListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeResponse")
    @Override
    public HentInntektListeResponse hentInntektListe(@WebParam(name = "request",targetNamespace = "") HentInntektListeRequest hentInntektListeRequest) throws HentInntektListeHarIkkeTilgangTilOensketAInntektsfilter, HentInntektListeSikkerhetsbegrensning, HentInntektListeUgyldigInput {
        return null;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentInntektListeBolkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentInntektListeBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeBolk")
    @ResponseWrapper(localName = "hentInntektListeBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeBolkResponse")
    @Override
    public HentInntektListeBolkResponse hentInntektListeBolk(@WebParam(name = "request",targetNamespace = "") HentInntektListeBolkRequest request) throws HentInntektListeBolkHarIkkeTilgangTilOensketAInntektsfilter, HentInntektListeBolkUgyldigInput {

        if (request != null && request.getIdentListe() != null
                && !request.getIdentListe().isEmpty()
                && request.getUttrekksperiode() != null) {

            LocalDate fom = ConversionUtils.convertToLocalDate(request.getUttrekksperiode().getMaanedFom());
            LocalDate tom = ConversionUtils.convertToLocalDate(request.getUttrekksperiode().getMaanedTom());
            HentInntektListeBolkResponse response = new HentInntektListeBolkResponse();

            for (Aktoer aktoer : request.getIdentListe()) {
                // PK-41326: Mock av en response for å teste oppdatering av registeropplysninger i vedtaksløsningen.
                List<ArbeidsInntektIdent> inntekter = new InntektGenerator().hentInntekter(aktoer, fom, tom);

                response.getArbeidsInntektIdentListe().addAll(inntekter);
            }
            return response;
        }
        return null;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentInntektListeForOpplysningspliktigRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentInntektListeForOpplysningspliktig", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeForOpplysningspliktig")
    @ResponseWrapper(localName = "hentInntektListeForOpplysningspliktigResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeForOpplysningspliktigResponse")
    @Override
    public HentInntektListeForOpplysningspliktigResponse hentInntektListeForOpplysningspliktig(@WebParam(name = "request",targetNamespace = "") HentInntektListeForOpplysningspliktigRequest hentInntektListeForOpplysningspliktigRequest) throws HentInntektListeForOpplysningspliktigHarIkkeTilgangTilOensketAInntektsfilter, HentInntektListeForOpplysningspliktigUgyldigInput {
        return null;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentAbonnerteInntekterBolkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentAbonnerteInntekterBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentAbonnerteInntekterBolk")
    @ResponseWrapper(localName = "hentAbonnerteInntekterBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentAbonnerteInntekterBolkResponse")
    @Override
    public HentAbonnerteInntekterBolkResponse hentAbonnerteInntekterBolk(@WebParam(name = "request",targetNamespace = "") HentAbonnerteInntekterBolkRequest hentAbonnerteInntekterBolkRequest) throws HentAbonnerteInntekterBolkHarIkkeTilgangTilOensketAInntektsfilter, HentAbonnerteInntekterBolkUgyldigInput {
        return null;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentDetaljerteAbonnerteInntekterRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentDetaljerteAbonnerteInntekter", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentDetaljerteAbonnerteInntekter")
    @ResponseWrapper(localName = "hentDetaljerteAbonnerteInntekterResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentDetaljerteAbonnerteInntekterResponse")
    @Override
    public HentDetaljerteAbonnerteInntekterResponse hentDetaljerteAbonnerteInntekter(@WebParam(name = "request",targetNamespace = "") HentDetaljerteAbonnerteInntekterRequest hentDetaljerteAbonnerteInntekterRequest) throws HentDetaljerteAbonnerteInntekterHarIkkeTilgangTilOensketAInntektsfilter, HentDetaljerteAbonnerteInntekterManglendeAbonnent, HentDetaljerteAbonnerteInntekterPersonIkkeFunnet, HentDetaljerteAbonnerteInntekterSikkerhetsbegrensning, HentDetaljerteAbonnerteInntekterUgyldigInput {
        return null;
    }
}
