package no.nav.tjeneste.virksomhet.arbeidsforhold.v3;

import java.time.LocalDate;
import java.util.List;

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

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidsgiverForMangeForekomster;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidsgiverSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidsgiverUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.HentArbeidsforholdHistorikkArbeidsforholdIkkeFunnet;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.HentArbeidsforholdHistorikkSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidstakerePrArbeidsgiverSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidstakerePrArbeidsgiverUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.ArbeidsforholdV3;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforhold;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidsgiverRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidsgiverResponse;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerResponse;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.HentArbeidsforholdHistorikkRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.HentArbeidsforholdHistorikkResponse;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidstakerePrArbeidsgiverRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidstakerePrArbeidsgiverResponse;


@Addressing
@WebService(name = "Arbeidsforhold_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3")
@HandlerChain(file="Handler-chain.xml")
public class ArbeidsforholdMockImpl implements ArbeidsforholdV3 {

    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsforholdMockImpl.class);
//    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("arbeidsforhold").createEntityManager();

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/finnArbeidsforholdPrArbeidsgiverRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnArbeidsforholdPrArbeidsgiver", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidsgiver")
    @ResponseWrapper(localName = "finnArbeidsforholdPrArbeidsgiverResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidsgiverResponse")
    public FinnArbeidsforholdPrArbeidsgiverResponse finnArbeidsforholdPrArbeidsgiver(@WebParam(name = "request",targetNamespace = "") FinnArbeidsforholdPrArbeidsgiverRequest finnArbeidsforholdPrArbeidsgiverRequest) throws FinnArbeidsforholdPrArbeidsgiverForMangeForekomster, FinnArbeidsforholdPrArbeidsgiverSikkerhetsbegrensning, FinnArbeidsforholdPrArbeidsgiverUgyldigInput {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3", className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3", className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/finnArbeidstakerePrArbeidsgiverRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnArbeidstakerePrArbeidsgiver", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidstakerePrArbeidsgiver")
    @ResponseWrapper(localName = "finnArbeidstakerePrArbeidsgiverResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidstakerePrArbeidsgiverResponse")
    public FinnArbeidstakerePrArbeidsgiverResponse finnArbeidstakerePrArbeidsgiver(@WebParam(name = "request",targetNamespace = "") FinnArbeidstakerePrArbeidsgiverRequest finnArbeidstakerePrArbeidsgiverRequest) throws FinnArbeidstakerePrArbeidsgiverSikkerhetsbegrensning, FinnArbeidstakerePrArbeidsgiverUgyldigInput {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/finnArbeidsforholdPrArbeidstakerRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnArbeidsforholdPrArbeidstaker", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidstaker")
    @ResponseWrapper(localName = "finnArbeidsforholdPrArbeidstakerResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidstakerResponse")
    public FinnArbeidsforholdPrArbeidstakerResponse finnArbeidsforholdPrArbeidstaker(@WebParam(name = "request",targetNamespace = "") FinnArbeidsforholdPrArbeidstakerRequest request) throws FinnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning, FinnArbeidsforholdPrArbeidstakerUgyldigInput {

        if (request != null && request.getIdent() != null
                && request.getIdent().getIdent() != null
                && request.getRapportertSomRegelverk() != null) {

            LocalDate fom = null;
            LocalDate tom = null;

            if (request.getArbeidsforholdIPeriode() != null) {
                fom = ConversionUtils.convertToLocalDate(request.getArbeidsforholdIPeriode().getFom());
                tom = ConversionUtils.convertToLocalDate(request.getArbeidsforholdIPeriode().getTom());
            }

            FinnArbeidsforholdPrArbeidstakerResponse response = new FinnArbeidsforholdPrArbeidstakerResponse();

            NorskIdent ident = request.getIdent();

            List<Arbeidsforhold> arbeidsforhold = new ArbeidsforholdGenerator().hentArbeidsforhold(ident, fom, tom);

            response.getArbeidsforhold().addAll(arbeidsforhold);

            return response;
        }
        return null;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/hentArbeidsforholdHistorikkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentArbeidsforholdHistorikk", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.HentArbeidsforholdHistorikk")
    @ResponseWrapper(localName = "hentArbeidsforholdHistorikkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.HentArbeidsforholdHistorikkResponse")
    public HentArbeidsforholdHistorikkResponse hentArbeidsforholdHistorikk(@WebParam(name = "request",targetNamespace = "") HentArbeidsforholdHistorikkRequest hentArbeidsforholdHistorikkRequest) throws HentArbeidsforholdHistorikkArbeidsforholdIkkeFunnet, HentArbeidsforholdHistorikkSikkerhetsbegrensning{
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
