package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3;

import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.binding.HentYtelseskontraktListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.binding.YtelseskontraktV3;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.HentYtelseskontraktListeRequest;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.HentYtelseskontraktListeResponse;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell.ArenaDbLeser;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell.ArenaSvar;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell.ArenaVedtak;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell.ArenaYtelseskontrakt;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell.BrukerBygger;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell.YtelseskontraktBygger;
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
import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.List;

@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.binding.YtelseskontraktV3")
public class HentYtelseskontraktListeMockImpl implements YtelseskontraktV3 {

    private static final Logger LOG = LoggerFactory.getLogger(HentYtelseskontraktListeMockImpl.class);
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("arena").createEntityManager();

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/ytelseskontrakt_v3/HentYtelseskontraktListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentYtelseskontraktListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.hentYtelseskontraktListe")
    @ResponseWrapper(localName = "hentYtelseskontraktListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.hentYtelseskontraktListeResponse")
    @Override
    public HentYtelseskontraktListeResponse hentYtelseskontraktListe(@WebParam(name = "request", targetNamespace = "") HentYtelseskontraktListeRequest hentYtelseskontraktListeRequest) throws HentYtelseskontraktListeSikkerhetsbegrensning {

        HentYtelseskontraktListeResponse response = new HentYtelseskontraktListeResponse();

        List<ArenaYtelseskontrakt> arenaYtelseskontraktListe;
        List<ArenaVedtak> arenaVedtak;
        ArenaSvar arenaSvar;
        String ident="";

       try {
           ArenaDbLeser arenaDbLeser = new ArenaDbLeser(entityManager);

           ident = hentYtelseskontraktListeRequest.getPersonidentifikator();
//        XMLGregorianCalendar fom = hentYtelseskontraktListeRequest.getPeriode().getFom();
//        XMLGregorianCalendar tom = hentYtelseskontraktListeRequest.getPeriode().getTom();

           if (ident != null || !ident.isEmpty()) {
               arenaSvar = arenaDbLeser.finnArenaSvarMedFnr(ident);
               arenaYtelseskontraktListe = arenaSvar.getYtelseskontraktListe();

               BrukerBygger brukerBygger = new BrukerBygger(arenaSvar);
               response.setBruker(brukerBygger.byggBruker());

               if (arenaYtelseskontraktListe != null) {
                   for (ArenaYtelseskontrakt a : arenaYtelseskontraktListe) {
                       arenaVedtak = arenaDbLeser.finnArenaVedtakMedYtelseId(a.getId());
                       YtelseskontraktBygger yb = new YtelseskontraktBygger(a, arenaVedtak);
                       response.getYtelseskontraktListe().add(yb.byggYtelseskontrakt());
                   }
               }
           }
       }
       catch (Exception e) {
           LOG.error("Exception {}", e.getStackTrace());
       }
        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/ytelseskontrakt_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.PingResponse")
    @Override
    public void ping() {
        LOG.info("Returned ping");
    }
}
