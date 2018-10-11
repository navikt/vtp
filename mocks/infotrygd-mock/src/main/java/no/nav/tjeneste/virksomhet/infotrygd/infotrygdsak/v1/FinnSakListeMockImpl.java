package no.nav.tjeneste.virksomhet.infotrygd.infotrygdsak.v1;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.FeilKodeKonstanter;
import no.nav.foreldrepenger.fpmock2.testmodell.Feilkode;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.infotrygd.infotrygdsak.v1.modell.InfotrygdSakBygger;
import no.nav.tjeneste.virksomhet.infotrygd.infotrygdsak.v1.modell.InfotrygdVedtakBygger;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeUgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.InfotrygdSakV1;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.ForretningsmessigUnntak;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.ObjectFactory;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.Sikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeRequest;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse;

@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.InfotrygdSakV1")
@HandlerChain(file = "Handler-chain.xml")
public class FinnSakListeMockImpl implements InfotrygdSakV1 {

    private static final Logger LOG = LoggerFactory.getLogger(FinnSakListeMockImpl.class);

    private static final String FAULTINFO_FEILAARSAK = "Feilaarsak";
    private static final String FAULTINFO_FEILKILDE = "Mock infotrygdSak";

    private TestscenarioBuilderRepository scenarioRepository;

    public FinnSakListeMockImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1/infotrygdSak_v1/FinnSakListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnSakListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygd.infotrygdsak.v1.finnSakListe")
    @ResponseWrapper(localName = "finnSakListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygd.infotrygdSak.v1.FinnSakListeResponse")
    @Override
    public no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse finnSakListe(@WebParam(name = "request", targetNamespace = "") FinnSakListeRequest finnSakListeRequest)
            throws FinnSakListePersonIkkeFunnet, FinnSakListeSikkerhetsbegrensning, FinnSakListeUgyldigInput {

        no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse response = new FinnSakListeResponse();
        String ident = finnSakListeRequest.getPersonident();
        LOG.info("FinnSakListeRequest, ident={}", ident);

        Optional<InntektYtelseModell> iyIndeksOpt = scenarioRepository.getInntektYtelseModell(ident);
        if (!iyIndeksOpt.isPresent()) {
            return response;
        }
        InntektYtelseModell inntektYtelseModell = iyIndeksOpt.get();
        InfotrygdModell infotrygdModell = inntektYtelseModell.getInfotrygdModell();

        Feilkode feilkode = infotrygdModell.getFeilkode();
        if (feilkode != null) {
            try {
                håndterExceptions(feilkode.getKode(), ident);
            } catch (Exception e) {
                LOG.error("Error ", e);
                throw e;
            }
        }

        List<InfotrygdYtelse> infotrygdYtelseListe = infotrygdModell.getYtelser();
        LOG.info("infotrygdYtelseListe {}", infotrygdYtelseListe);
        if (infotrygdYtelseListe != null) {
            LOG.info("infotrygdYtelseListestørrelse {}", infotrygdYtelseListe.size());

            for (InfotrygdYtelse ytelse : infotrygdYtelseListe) {
                if (null == ytelse.getOpphørFom()) {
                    InfotrygdSakBygger ib = new InfotrygdSakBygger(ytelse);
                    response.getSakListe().add(ib.byggInfotrygdSak());
                } else {
                    InfotrygdVedtakBygger ib = new InfotrygdVedtakBygger(ytelse);
                    response.getVedtakListe().add(ib.byggInfotrygdVedtak());
                }
            }

            return response;
        }

        return null;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1/InfotrygdSak_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygd.infotrygdSak.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygd.infotrygdSak.v1.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    private void håndterExceptions(String kode, String ident)
            throws FinnSakListeUgyldigInput, FinnSakListePersonIkkeFunnet, FinnSakListeSikkerhetsbegrensning {

        switch (kode) {
            case FeilKodeKonstanter.UGYLDIG_INPUT: {
                UgyldigInput faultInfo = lagUgyldigInput(ident);
                throw new FinnSakListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
            }
            case FeilKodeKonstanter.PERSON_IKKE_FUNNET: {
                PersonIkkeFunnet faultInfo = lagPersonIkkeFunnet();
                throw new FinnSakListePersonIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
            }
            case FeilKodeKonstanter.SIKKERHET_BEGRENSNING: {
                Sikkerhetsbegrensning faultInfo = lagSikkerhetsbegrensning(ident);
                throw new FinnSakListeSikkerhetsbegrensning(faultInfo.getFeilmelding(), faultInfo);
            }
            default:
                // ikke noe
                break;
        }
    }

    private UgyldigInput lagUgyldigInput(String ident) {
        no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.ObjectFactory of = new ObjectFactory();
        UgyldigInput faultInfo = of.createUgyldigInput();
        faultInfo.setFeilmelding("Ident \"" + ident + "\" er ugyldig input");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private Sikkerhetsbegrensning lagSikkerhetsbegrensning(String ident) {
        no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.ObjectFactory of = new ObjectFactory();
        Sikkerhetsbegrensning faultInfo = of.createSikkerhetsbegrensning();
        faultInfo.setFeilmelding("PersonModell med ident \"" + ident + "\" har sikkerhetsbegrensning");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private PersonIkkeFunnet lagPersonIkkeFunnet() {
        no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.ObjectFactory of = new ObjectFactory();
        PersonIkkeFunnet faultInfo = of.createPersonIkkeFunnet();
        faultInfo.setFeilmelding("PersonModell er ikke funnet");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private void populerMedStandardVerdier(ForretningsmessigUnntak faultInfo) {
        faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
        faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
        faultInfo.setTidspunkt(now());
    }

    private XMLGregorianCalendar now() {
        return ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now());
    }
}
