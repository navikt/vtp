package no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1;



import static no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.FeilKodeKonstanter.PERSON_IKKE_FUNNET;
import static no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.FeilKodeKonstanter.SIKKERHET_BEGRENSNING;
import static no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.FeilKodeKonstanter.UGYLDIG_INPUT;

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

import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.ObjectFactory;
import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
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

import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.modell.InfotrygdGrunnlagMapper;
import no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.InfotrygdDbLeser;
import no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.InfotrygdGrunnlag;

@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.InfotrygdBeregningsgrunnlagV1")
@HandlerChain(file="Handler-chain.xml")
public class FinnGrunnlagListeMockImpl implements InfotrygdBeregningsgrunnlagV1 {

    private static final Logger LOG = LoggerFactory.getLogger(FinnGrunnlagListeMockImpl.class);
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("infotrygd").createEntityManager();

    private static final String FAULTINFO_FEILAARSAK = "Feilaarsak";
    private static final String FAULTINFO_FEILKILDE = "Mock infotrygdBeregningsgrunnlag";

    private static ObjectFactory of = new ObjectFactory();
    private static InfotrygdGrunnlagMapper itmapper = new InfotrygdGrunnlagMapper();

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1/InfotrygdBeregningsgrunnlag_v1/FinnGrunnlagListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnGrunnlagListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1",
            className = "no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.finnGrunnlagListe")
    @ResponseWrapper(localName = "finnGrunnlagListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1",
            className = "no.nav.tjeneste.virksomhet.infotrygdBeregningsgrunnlag.v1.FinnGrunnlagListeResponse")
    @Override
    public FinnGrunnlagListeResponse finnGrunnlagListe(@WebParam(name = "request", targetNamespace = "") FinnGrunnlagListeRequest finnGrunnlagListeRequest)
            throws FinnGrunnlagListePersonIkkeFunnet, FinnGrunnlagListeSikkerhetsbegrensning, FinnGrunnlagListeUgyldigInput {

        LOG.info("Starter finnGrunnlagListe");
        FinnGrunnlagListeResponse response = of.createFinnGrunnlagListeResponse();
        LOG.info("FinnGrunnlagListeRequest ", finnGrunnlagListeRequest);
        String ident = finnGrunnlagListeRequest.getPersonident();
        LOG.info("Identen er", finnGrunnlagListeRequest.getPersonident());

        InfotrygdDbLeser infotrygdDbLeser = new InfotrygdDbLeser(entityManager);
        LOG.info("Se på tjeneste ", finnGrunnlagListeRequest.getPersonident());

        Optional<String> feilkode = infotrygdDbLeser.finnInfotrygdSvarMedFnrOpt(ident);
        try {
            haandterExceptions(feilkode.orElse("--"), ident);
        } catch (Exception e) {
            LOG.error("Error ", e);
            throw e;
        }

        List<InfotrygdGrunnlag> infotrygdGrunnlagList = infotrygdDbLeser.finnInfotrygdGrunnlagMedFnr(ident);
        LOG.info("infotrygdGrunnlagListe ", infotrygdGrunnlagList);
        if (infotrygdGrunnlagList != null) {
            LOG.info("infotrygdGrunnlagListestørrelse ", infotrygdGrunnlagList.size());

            response = itmapper.mapInfotrygdGrunnlag(response, infotrygdGrunnlagList);

            return response;
        }

        return null;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1/InfotrygdBeregningsgrunnlag_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1",
            className = "no.nav.tjeneste.virksomhet.infotrygdBeregningsgrunnlag.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdBeregningsgrunnlag/v1",
            className = "no.nav.tjeneste.virksomhet.infotrygdBeregningsgrunnlag.v1.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    private void haandterExceptions(String kode, String ident) throws FinnGrunnlagListeUgyldigInput, FinnGrunnlagListePersonIkkeFunnet, FinnGrunnlagListeSikkerhetsbegrensning {

        switch (kode) {
            case UGYLDIG_INPUT: {
                UgyldigInput faultInfo = lagUgyldigInput(ident);
                throw new FinnGrunnlagListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
            }
            case PERSON_IKKE_FUNNET: {
                PersonIkkeFunnet faultInfo = lagPersonIkkeFunnet();
                throw new FinnGrunnlagListePersonIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
            }
            case SIKKERHET_BEGRENSNING: {
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
        faultInfo.setFeilmelding("Person med ident \"" + ident + "\" har sikkerhetsbegrensning");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private PersonIkkeFunnet lagPersonIkkeFunnet() {
        no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.feil.ObjectFactory();
        PersonIkkeFunnet faultInfo = ofl.createPersonIkkeFunnet();
        faultInfo.setFeilmelding("Person er ikke funnet");
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