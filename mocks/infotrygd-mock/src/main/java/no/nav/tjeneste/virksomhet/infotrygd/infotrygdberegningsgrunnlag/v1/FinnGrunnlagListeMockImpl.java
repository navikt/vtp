package no.nav.tjeneste.virksomhet.infotrygd.infotrygdberegningsgrunnlag.v1;

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
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdBeregningsgrunnlag;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.infotrygd.infotrygdberegningsgrunnlag.v1.modell.InfotrygdGrunnlagMapper;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.FinnGrunnlagListePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.FinnGrunnlagListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.FinnGrunnlagListeUgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.InfotrygdBeregningsgrunnlagV1;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.ForretningsmessigUnntak;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.Sikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.FinnGrunnlagListeRequest;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.FinnGrunnlagListeResponse;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.ObjectFactory;

@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.InfotrygdBeregningsgrunnlagV1")
@HandlerChain(file = "Handler-chain.xml")
public class FinnGrunnlagListeMockImpl implements InfotrygdBeregningsgrunnlagV1 {

    private static final Logger LOG = LoggerFactory.getLogger(FinnGrunnlagListeMockImpl.class);

    private static final String FAULTINFO_FEILAARSAK = "Feilaarsak";
    private static final String FAULTINFO_FEILKILDE = "Mock infotrygdBeregningsgrunnlag";

    private static ObjectFactory of = new ObjectFactory();
    private static InfotrygdGrunnlagMapper itmapper = new InfotrygdGrunnlagMapper();

    private TestscenarioBuilderRepository scenarioRepository;

    public FinnGrunnlagListeMockImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1/InfotrygdBeregningsgrunnlag_v1/FinnGrunnlagListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnGrunnlagListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.infotrygd.infotrygdberegningsgrunnlag.v1.finnGrunnlagListe")
    @ResponseWrapper(localName = "finnGrunnlagListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.infotrygd.infotrygdBeregningsgrunnlag.v1.FinnGrunnlagListeResponse")
    @Override
    public FinnGrunnlagListeResponse finnGrunnlagListe(@WebParam(name = "request", targetNamespace = "") FinnGrunnlagListeRequest finnGrunnlagListeRequest)
            throws FinnGrunnlagListePersonIkkeFunnet, FinnGrunnlagListeSikkerhetsbegrensning, FinnGrunnlagListeUgyldigInput {

        FinnGrunnlagListeResponse response = of.createFinnGrunnlagListeResponse();
        String ident = finnGrunnlagListeRequest.getPersonident();
        LOG.info("FinnGrunnlagListeRequest, ident={}", ident);

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

        List<InfotrygdBeregningsgrunnlag> infotrygdGrunnlagList = infotrygdModell.getGrunnlag();
        LOG.info("infotrygdGrunnlagListe {}", infotrygdGrunnlagList);
        if (infotrygdGrunnlagList != null) {
            LOG.info("infotrygdGrunnlagListestørrelse {}", infotrygdGrunnlagList.size());

            response = itmapper.mapInfotrygdGrunnlag(response, infotrygdGrunnlagList);

            return response;
        }

        return null;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1/InfotrygdBeregningsgrunnlag_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.infotrygd.infotrygdBeregningsgrunnlag.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.infotrygd.infotrygdBeregningsgrunnlag.v1.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    private void håndterExceptions(String kode, String ident)
            throws FinnGrunnlagListeUgyldigInput, FinnGrunnlagListePersonIkkeFunnet, FinnGrunnlagListeSikkerhetsbegrensning {

        switch (kode) {
            case FeilKodeKonstanter.UGYLDIG_INPUT: {
                UgyldigInput faultInfo = lagUgyldigInput(ident);
                throw new FinnGrunnlagListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
            }
            case FeilKodeKonstanter.PERSON_IKKE_FUNNET: {
                PersonIkkeFunnet faultInfo = lagPersonIkkeFunnet();
                throw new FinnGrunnlagListePersonIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
            }
            case FeilKodeKonstanter.SIKKERHET_BEGRENSNING: {
                Sikkerhetsbegrensning faultInfo = lagSikkerhetsbegrensning(ident);
                throw new FinnGrunnlagListeSikkerhetsbegrensning(faultInfo.getFeilmelding(), faultInfo);
            }
            default:
                // ikke noe
                break;
        }
    }

    private UgyldigInput lagUgyldigInput(String ident) {
        no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.ObjectFactory();
        UgyldigInput faultInfo = ofl.createUgyldigInput();
        faultInfo.setFeilmelding("Ident \"" + ident + "\" er ugyldig input");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private Sikkerhetsbegrensning lagSikkerhetsbegrensning(String ident) {
        no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.ObjectFactory();
        Sikkerhetsbegrensning faultInfo = ofl.createSikkerhetsbegrensning();
        faultInfo.setFeilmelding("PersonModell med ident \"" + ident + "\" har sikkerhetsbegrensning");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private PersonIkkeFunnet lagPersonIkkeFunnet() {
        no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.ObjectFactory();
        PersonIkkeFunnet faultInfo = ofl.createPersonIkkeFunnet();
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
