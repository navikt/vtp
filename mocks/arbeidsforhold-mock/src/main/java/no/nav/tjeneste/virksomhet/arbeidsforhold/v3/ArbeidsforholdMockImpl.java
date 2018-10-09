package no.nav.tjeneste.virksomhet.arbeidsforhold.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.ArbeidsforholdV3;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidsgiverForMangeForekomster;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidsgiverSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidsgiverUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidstakerePrArbeidsgiverSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidstakerePrArbeidsgiverUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.HentArbeidsforholdHistorikkArbeidsforholdIkkeFunnet;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.HentArbeidsforholdHistorikkSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.feil.ArbeidsforholdIkkeFunnet;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforhold;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidsgiverRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidsgiverResponse;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerResponse;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidstakerePrArbeidsgiverRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidstakerePrArbeidsgiverResponse;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.HentArbeidsforholdHistorikkRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.HentArbeidsforholdHistorikkResponse;


@Addressing
@WebService(name = "Arbeidsforhold_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3")
@HandlerChain(file="Handler-chain.xml")
public class ArbeidsforholdMockImpl implements ArbeidsforholdV3 {

    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsforholdMockImpl.class);
//    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("arbeidsforhold").createEntityManager();

    private TestscenarioBuilderRepository scenarioRepository;

    public ArbeidsforholdMockImpl(){

    }

    public ArbeidsforholdMockImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/finnArbeidsforholdPrArbeidsgiverRequest")
    @WebResult(name = "parameters", targetNamespace = "")
    @RequestWrapper(localName = "finnArbeidsforholdPrArbeidsgiver", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidsgiver")
    @ResponseWrapper(localName = "FinnArbeidsforholdPrArbeidsgiverResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidsgiverResponse")
    public FinnArbeidsforholdPrArbeidsgiverResponse finnArbeidsforholdPrArbeidsgiver(@WebParam(name = "parameters",targetNamespace = "") FinnArbeidsforholdPrArbeidsgiverRequest finnArbeidsforholdPrArbeidsgiverRequest) throws FinnArbeidsforholdPrArbeidsgiverForMangeForekomster, FinnArbeidsforholdPrArbeidsgiverSikkerhetsbegrensning, FinnArbeidsforholdPrArbeidsgiverUgyldigInput {
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
    @WebResult(name = "parameters", targetNamespace = "")
    @RequestWrapper(localName = "finnArbeidstakerePrArbeidsgiver", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidstakerePrArbeidsgiverRequest")
    @ResponseWrapper(localName = "finnArbeidstakerePrArbeidsgiverResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidstakerePrArbeidsgiverResponse")
    public FinnArbeidstakerePrArbeidsgiverResponse finnArbeidstakerePrArbeidsgiver(@WebParam(name = "parameters",targetNamespace = "") FinnArbeidstakerePrArbeidsgiverRequest finnArbeidstakerePrArbeidsgiverRequest) throws FinnArbeidstakerePrArbeidsgiverSikkerhetsbegrensning, FinnArbeidstakerePrArbeidsgiverUgyldigInput {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/finnArbeidsforholdPrArbeidstakerRequest")
    @WebResult(name = "parameters", targetNamespace = "")
    @RequestWrapper(localName = "finnArbeidsforholdPrArbeidstaker", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidstaker")
    @ResponseWrapper(localName = "finnArbeidsforholdPrArbeidstakerResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidstakerResponse")
    public FinnArbeidsforholdPrArbeidstakerResponse finnArbeidsforholdPrArbeidstaker(@WebParam(name = "parameters",targetNamespace = "") FinnArbeidsforholdPrArbeidstakerRequest request) throws FinnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning, FinnArbeidsforholdPrArbeidstakerUgyldigInput {
        LOG.info("finnArbeidsforholdPrArbeidstaker. Ident: " + request.getIdent().getIdent() + ". Regelverk: " + request.getRapportertSomRegelverk().getKodeverksRef());
        if (request != null && request.getIdent() != null
                && request.getIdent().getIdent() != null
                && request.getRapportertSomRegelverk() != null) {

            ArbeidsforholdAdapter arbeidsforholdAdapter = new ArbeidsforholdAdapter();

            String fnr = request.getIdent().getIdent();
            Optional<InntektYtelseModell> inntektYtelseModell = scenarioRepository.getInntektYtelseModell(fnr);

            if(inntektYtelseModell.isPresent() && inntektYtelseModell.get().getArbeidsforholdModell() != null){
                ArbeidsforholdModell arbeidsforholdModell = inntektYtelseModell.get().getArbeidsforholdModell();
                List<Arbeidsforhold> responseArbeidsforhold = new ArrayList<>();

                for(no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold arbeidsforhold : arbeidsforholdModell.getArbeidsforhold()){
                    responseArbeidsforhold.add(arbeidsforholdAdapter.fra(fnr, arbeidsforhold));
                }

                FinnArbeidsforholdPrArbeidstakerResponse response = new FinnArbeidsforholdPrArbeidstakerResponse();
                response.getArbeidsforhold().addAll(responseArbeidsforhold);

                return response;

            }
            LOG.warn("finnArbeidsforholdPrArbeidstaker kunne ikke finne etterspurt bruker");

        }
        LOG.warn("finnArbeidsforholdPrArbeidstaker ugyldig forespørsel");
        throw new FinnArbeidsforholdPrArbeidstakerUgyldigInput("Ikke gyldig input", new UgyldigInput());
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/hentArbeidsforholdHistorikkRequest")
    @WebResult(name = "parameters", targetNamespace = "")
    @RequestWrapper(localName = "hentArbeidsforholdHistorikk", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.HentArbeidsforholdHistorikkRequest")
    @ResponseWrapper(localName = "hentArbeidsforholdHistorikkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
            className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.HentArbeidsforholdHistorikkResponse")
    public HentArbeidsforholdHistorikkResponse hentArbeidsforholdHistorikk(@WebParam(name = "parameters",targetNamespace = "") HentArbeidsforholdHistorikkRequest request) throws HentArbeidsforholdHistorikkArbeidsforholdIkkeFunnet, HentArbeidsforholdHistorikkSikkerhetsbegrensning{
        LOG.info("Kall til HentArbeidsforholdHistorikk. Ber om historikk for arbeidsforholdsId: {}", request.getArbeidsforholdId());

        ArbeidsforholdAdapter adapter = new ArbeidsforholdAdapter();
        //TODO: Bruker arbeidsforholdsIdNav for videre oppslag.
        Long arbeidsforholdId = request.getArbeidsforholdId();

        HentArbeidsforholdHistorikkResponse response = new HentArbeidsforholdHistorikkResponse();
        List<String> identer = scenarioRepository.getPersonIndeks().getAlleSøkere()
                .stream()
                .map(t-> t.getSøker().getIdent())
                .collect(Collectors.toList());

        for (String fnr : identer){
            ArbeidsforholdModell arbeidsforholdModell = scenarioRepository.getInntektYtelseModell(fnr).get().getArbeidsforholdModell();
            if(arbeidsforholdModell != null){
                Optional<no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold> first = arbeidsforholdModell.getArbeidsforhold().stream().filter(t -> t.getArbeidsforholdIdnav().equals(arbeidsforholdId)).findFirst();
                if(first.isPresent()){
                    Arbeidsforhold responseArbeidsforhold = adapter.fra(fnr, first.get());
                    response.setArbeidsforhold(responseArbeidsforhold);
                    return  response;
                }
            }
        }
        throw new HentArbeidsforholdHistorikkArbeidsforholdIkkeFunnet("Kunne ikke finne arbeidsforholdHistorikk med Id " + arbeidsforholdId, new ArbeidsforholdIkkeFunnet());
    }
}
