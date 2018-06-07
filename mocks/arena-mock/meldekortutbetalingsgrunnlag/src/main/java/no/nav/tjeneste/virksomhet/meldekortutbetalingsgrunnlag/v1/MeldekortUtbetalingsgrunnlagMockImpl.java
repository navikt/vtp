package no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1;

import static no.nav.tjeneste.virksomhet.arenafelles.v1.modell.FeilKodeKonstanter.PERSON_IKKE_FUNNET;
import static no.nav.tjeneste.virksomhet.arenafelles.v1.modell.FeilKodeKonstanter.SIKKERHET_BEGRENSNING;
import static no.nav.tjeneste.virksomhet.arenafelles.v1.modell.FeilKodeKonstanter.UGYLDIG_INPUT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.MeldekortUtbetalingsgrunnlagV1;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.AktoerIkkeFunnet;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ForretningsmessigUnntak;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.Sikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Tema;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeRequest;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeResponse;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.ObjectFactory;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.modell.ArenaMUAktør;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.modell.ArenaMUDLeser;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.modell.ArenaMUMapper;

@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.MeldekortUtbetalingsgrunnlagV1")
@HandlerChain(file="Handler-chain.xml")
public class MeldekortUtbetalingsgrunnlagMockImpl implements MeldekortUtbetalingsgrunnlagV1 {

    private static final Logger LOG = LoggerFactory.getLogger(MeldekortUtbetalingsgrunnlagMockImpl.class);
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("arena").createEntityManager();

    private static final String FAULTINFO_FEILAARSAK = "Feilaarsak";
    private static final String FAULTINFO_FEILKILDE = "Mock MeldekortUtbetalingsgrunnlag";

    private static ObjectFactory of = new ObjectFactory();
    private static ArenaMUMapper arenaMapper = new ArenaMUMapper();


    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1/meldekortutbetalingsgrunnlag_v1/FinnMeldekortUtbetalingsgrunnlagListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnMeldekortUtbetalingsgrunnlagListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1",
            className = "no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.finnMeldekortUtbetalingsgrunnlagListe")
    @ResponseWrapper(localName = "finnMeldekortUtbetalingsgrunnlagListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v1",
            className = "no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.finnMeldekortUtbetalingsgrunnlagListeResponse")
    @Override
    public FinnMeldekortUtbetalingsgrunnlagListeResponse finnMeldekortUtbetalingsgrunnlagListe(@WebParam(name = "request",targetNamespace = "")FinnMeldekortUtbetalingsgrunnlagListeRequest finnMeldekortUtbetalingsgrunnlagListeRequest)
            throws FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet, FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning, FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput {

        FinnMeldekortUtbetalingsgrunnlagListeResponse response = of.createFinnMeldekortUtbetalingsgrunnlagListeResponse();
        AktoerId aktoerId = (AktoerId)finnMeldekortUtbetalingsgrunnlagListeRequest.getIdent();
        String ident = aktoerId.getAktoerId();
        if (finnMeldekortUtbetalingsgrunnlagListeRequest.getPeriode() != null) {
            XMLGregorianCalendar fom = finnMeldekortUtbetalingsgrunnlagListeRequest.getPeriode().getFom();
            XMLGregorianCalendar tom = finnMeldekortUtbetalingsgrunnlagListeRequest.getPeriode().getTom();
        }
        List<Tema> temaListe = finnMeldekortUtbetalingsgrunnlagListeRequest.getTemaListe();

        ArenaMUDLeser arenaMUDLeser = new ArenaMUDLeser(entityManager);
        if (ident == null) {
            UgyldigInput faultInfo = lagUgyldigInput(ident);
            throw new FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }
        ArenaMUAktør arenaAktør = arenaMUDLeser.finnArenaAktørId(ident);
        if (arenaAktør == null) {
            return response;
        }
        Optional<String> feilkode = arenaMUDLeser.finnArenaAktørFeilkode(ident);
        try {
            haandterExceptions(feilkode.orElse("--"), ident);
        } catch (Exception e) {
            LOG.error("Error ", e);
            throw e;
        }

        return arenaMapper.mapArenaSaker(response, arenaAktør.getArenaSaker());

    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1/meldekortutbetalingsgrunnlag_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.PingResponse")
    @Override
    public void ping() {
        LOG.info("Returned ping");
    }

    private void haandterExceptions(String kode, String ident)
            throws FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput, FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet, FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning {

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
        faultInfo.setFeilmelding("Person med aktørId \"" + ident + "\" har sikkerhetsbegrensning");
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
