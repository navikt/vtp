package no.nav.tjeneste.virksomhet.infotrygdsak.v1;


import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeUgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.InfotrygdSakV1;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.*;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.ObjectFactory;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.*;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.modell.InfotrygdDbLeser;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.modell.InfotrygdSakBygger;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.modell.InfotrygdYtelse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.time.LocalDateTime;
import java.util.List;

import static no.nav.tjeneste.virksomhet.infotrygdsak.v1.modell.FeilKodeKonstanter.PERSON_IKKE_FUNNET;
import static no.nav.tjeneste.virksomhet.infotrygdsak.v1.modell.FeilKodeKonstanter.SIKKERHET_BEGRENSNING;
import static no.nav.tjeneste.virksomhet.infotrygdsak.v1.modell.FeilKodeKonstanter.UGYLDIG_INPUT;

@WebService(name = "InfotrygdSak_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1")
public class FinnSakListeMockImpl implements InfotrygdSakV1 {

    private static final Logger LOG = LoggerFactory.getLogger(FinnSakListeMockImpl.class);
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("infotrygd").createEntityManager();

    private static final String FAULTINFO_FEILAARSAK = "Feilaarsak";
    private static final String FAULTINFO_FEILKILDE = "Mock infotrygdSak";

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1/infotrygdSak_v1/FinnSakListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnSakListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygdsak.v1.finnSakListe")
    @ResponseWrapper(localName = "finnSakListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygdSak.v1.FinnSakListeResponse")
    @Override
    public no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse finnSakListe(@WebParam(name = "request", targetNamespace = "") FinnSakListeRequest finnSakListeRequest) throws FinnSakListePersonIkkeFunnet, FinnSakListeSikkerhetsbegrensning, FinnSakListeUgyldigInput {

        LOG.info("Starter finnSakListe");
        no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse response = new FinnSakListeResponse();
        LOG.info("FinnSakListeRequest " + finnSakListeRequest);
        String ident = finnSakListeRequest.getPersonident();
        LOG.info("Identen er" + finnSakListeRequest.getPersonident());

        if (ident == null || ident.isEmpty()) {
            UgyldigInput faultInfo = lagUgyldigInput(ident);
            throw new FinnSakListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }

        InfotrygdDbLeser infotrygdDbLeser = new InfotrygdDbLeser(entityManager);
        LOG.info("Se på tjeneste " + finnSakListeRequest.getPersonident());

        String feilkode = infotrygdDbLeser.finnInfotrygdSvarMedFnr(ident).get(0).getFeilkode();
        if (feilkode != null) {
            haandterExceptions(feilkode, ident);
        }

        List<InfotrygdYtelse> infotrygdYtelseListe = infotrygdDbLeser.finnInfotrygdYtelseMedFnr(ident);
        LOG.info("infotrygdYtelseListe " + infotrygdYtelseListe);
        if (infotrygdYtelseListe != null) {
            LOG.info("infotrygdYtelseListestørrelse " + infotrygdYtelseListe.size());

            for (InfotrygdYtelse ytelse : infotrygdYtelseListe) {
                InfotrygdSakBygger ib = new InfotrygdSakBygger(ytelse);
                response.getSakListe().add(ib.byggInfotrygdSak());
            }

            return response;
        }

        return null;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1/InfotrygdSak_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygdSak.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygdSak.v1.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    private void haandterExceptions(String kode, String ident) throws FinnSakListeUgyldigInput, FinnSakListePersonIkkeFunnet, FinnSakListeSikkerhetsbegrensning {

        switch (kode) {
            case UGYLDIG_INPUT: {
                UgyldigInput faultInfo = lagUgyldigInput(ident);
                throw new FinnSakListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
            }
            case PERSON_IKKE_FUNNET: {
                PersonIkkeFunnet faultInfo = lagPersonIkkeFunnet(ident);
                throw new FinnSakListePersonIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
            }
            case SIKKERHET_BEGRENSNING: {
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
        faultInfo.setFeilmelding("Person med ident \"" + ident + "\" har sikkerhetsbegrensning");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private PersonIkkeFunnet lagPersonIkkeFunnet(String ident) {
        no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.ObjectFactory of = new ObjectFactory();
        PersonIkkeFunnet faultInfo = of.createPersonIkkeFunnet();
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

