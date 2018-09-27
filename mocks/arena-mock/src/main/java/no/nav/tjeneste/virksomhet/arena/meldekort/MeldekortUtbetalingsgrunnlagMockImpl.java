package no.nav.tjeneste.virksomhet.arena.meldekort;

import static no.nav.foreldrepenger.fpmock2.testmodell.FeilKodeKonstanter.PERSON_IKKE_FUNNET;
import static no.nav.foreldrepenger.fpmock2.testmodell.FeilKodeKonstanter.SIKKERHET_BEGRENSNING;
import static no.nav.foreldrepenger.fpmock2.testmodell.FeilKodeKonstanter.UGYLDIG_INPUT;

import java.time.LocalDateTime;
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
import no.nav.foreldrepenger.fpmock2.testmodell.Feilkode;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena.ArenaModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.arena.meldekort.modell.ArenaMUMapper;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.MeldekortUtbetalingsgrunnlagV1;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.AktoerIkkeFunnet;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ForretningsmessigUnntak;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.Sikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeRequest;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeResponse;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.ObjectFactory;

@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.MeldekortUtbetalingsgrunnlagV1")
@HandlerChain(file = "Handler-chain.xml")
public class MeldekortUtbetalingsgrunnlagMockImpl implements MeldekortUtbetalingsgrunnlagV1 {

    private static final Logger LOG = LoggerFactory.getLogger(MeldekortUtbetalingsgrunnlagMockImpl.class);

    private static final String FAULTINFO_FEILAARSAK = "Feilaarsak";
    private static final String FAULTINFO_FEILKILDE = "Mock MeldekortUtbetalingsgrunnlag";

    private static ObjectFactory of = new ObjectFactory();
    private static ArenaMUMapper arenaMapper = new ArenaMUMapper();
    private TestscenarioBuilderRepository scenarioRepository;

    public MeldekortUtbetalingsgrunnlagMockImpl(TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1/meldekortutbetalingsgrunnlag_v1/FinnMeldekortUtbetalingsgrunnlagListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnMeldekortUtbetalingsgrunnlagListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.arena.meldekort.finnMeldekortUtbetalingsgrunnlagListe")
    @ResponseWrapper(localName = "finnMeldekortUtbetalingsgrunnlagListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v1", className = "no.nav.tjeneste.virksomhet.arena.meldekort.finnMeldekortUtbetalingsgrunnlagListeResponse")
    @Override
    public FinnMeldekortUtbetalingsgrunnlagListeResponse finnMeldekortUtbetalingsgrunnlagListe(@WebParam(name = "request", targetNamespace = "") FinnMeldekortUtbetalingsgrunnlagListeRequest finnMeldekortUtbetalingsgrunnlagListeRequest)
            throws FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet, FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning,
            FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput {

        FinnMeldekortUtbetalingsgrunnlagListeResponse response = of.createFinnMeldekortUtbetalingsgrunnlagListeResponse();
        AktoerId aktoerId = (AktoerId) finnMeldekortUtbetalingsgrunnlagListeRequest.getIdent();
        String ident = aktoerId.getAktoerId();
        LOG.info("finnMeldekortUtbetalingsgrunnlagListe. AktoerIdent: " + ident);

        if (ident == null) {
            UgyldigInput faultInfo = lagUgyldigInput(ident);
            throw new FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }
        Optional<InntektYtelseModell> iyIndeksOpt = scenarioRepository.getInntektYtelseModell(ident);
        if (!iyIndeksOpt.isPresent()) {
            return response;
        }
        InntektYtelseModell inntektYtelseModell = iyIndeksOpt.get();
        ArenaModell arenaModell = inntektYtelseModell.getArenaModell();
        Feilkode feilkode = arenaModell.getFeilkode();
        if (feilkode != null) {
            try {
                haandterExceptions(feilkode.getKode(), ident);
            } catch (Exception e) {
                LOG.error("Error ", e);
                throw e;
            }
        }

        return arenaMapper.mapArenaSaker(finnMeldekortUtbetalingsgrunnlagListeRequest, response, arenaModell.getSaker());

    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1/meldekortutbetalingsgrunnlag_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.arena.meldekort.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.arena.meldekort.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    private void haandterExceptions(String kode, String ident)
            throws FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput, FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet,
            FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning {

        switch (kode) {
            case UGYLDIG_INPUT: {
                UgyldigInput faultInfo = lagUgyldigInput(ident);
                throw new FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
            }
            case PERSON_IKKE_FUNNET: {
                AktoerIkkeFunnet faultInfo = lagAktoerIkkeFunnet();
                throw new FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
            }
            case SIKKERHET_BEGRENSNING: {
                Sikkerhetsbegrensning faultInfo = lagSikkerhetsbegrensning(ident);
                throw new FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning(faultInfo.getFeilmelding(), faultInfo);
            }
            default:
                // ikke noe
                break;
        }
    }

    private UgyldigInput lagUgyldigInput(String ident) {
        no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory();
        UgyldigInput faultInfo = ofl.createUgyldigInput();
        faultInfo.setFeilmelding("Aktør \"" + ident + "\" er ugyldig input");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private Sikkerhetsbegrensning lagSikkerhetsbegrensning(String ident) {
        no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory();
        Sikkerhetsbegrensning faultInfo = ofl.createSikkerhetsbegrensning();
        faultInfo.setFeilmelding("PersonModell med aktørId \"" + ident + "\" har sikkerhetsbegrensning");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private AktoerIkkeFunnet lagAktoerIkkeFunnet() {
        no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory();
        AktoerIkkeFunnet faultInfo = ofl.createAktoerIkkeFunnet();
        faultInfo.setFeilmelding("Aktør er ikke funnet");
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
