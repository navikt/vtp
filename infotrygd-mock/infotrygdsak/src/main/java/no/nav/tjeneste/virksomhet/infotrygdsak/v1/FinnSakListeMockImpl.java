package no.nav.tjeneste.virksomhet.infotrygdsak.v1;


import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeUgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.InfotrygdSakV1;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.informasjon.Periode;
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
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.List;

@WebService(name = "InfotrygdSak_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1")
public class FinnSakListeMockImpl implements InfotrygdSakV1 {

    private static final Logger LOG = LoggerFactory.getLogger(FinnSakListeMockImpl.class);
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("infotrygd").createEntityManager();

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1/infotrygdSak_v1/FinnSakListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnSakListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygdsak.v1.finnSakListe")
    @ResponseWrapper(localName = "finnSakListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/infotrygdSak/v1", className = "no.nav.tjeneste.virksomhet.infotrygdSak.v1.FinnSakListeResponse")
    public no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse finnSakListe(@WebParam(name = "request", targetNamespace = "") FinnSakListeRequest finnSakListeRequest) throws FinnSakListePersonIkkeFunnet, FinnSakListeSikkerhetsbegrensning, FinnSakListeUgyldigInput {

        no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse response = new FinnSakListeResponse();
        String ident = finnSakListeRequest.getPersonident();

        if (ident == null){
            throw new FinnSakListeUgyldigInput("Ident må være satt", new UgyldigInput());
        }

        InfotrygdDbLeser infotrygdDbLeser = new InfotrygdDbLeser(entityManager);
        List<InfotrygdYtelse> infotrygdYtelseListe = infotrygdDbLeser.finnInfotrygdYtelseMedFnr(ident);

        if (infotrygdYtelseListe != null) {
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
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

}
