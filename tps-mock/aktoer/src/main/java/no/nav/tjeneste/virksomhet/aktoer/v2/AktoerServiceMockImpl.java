package no.nav.tjeneste.virksomhet.aktoer.v2;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.AktoerV2;
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentAktoerIdForIdentPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentIdentForAktoerIdPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.aktoer.v2.feil.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.AktoerIder;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentListeRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentListeResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdListeRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdListeResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.IdentDetaljer;
import no.nav.tjeneste.virksomhet.person.v3.data.PersonDbLeser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Addressing
@WebService(name = "Aktoer_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2")
@HandlerChain(file="Handler-chain.xml")
public class AktoerServiceMockImpl implements AktoerV2 {

    private static final Logger LOG = LoggerFactory.getLogger(AktoerServiceMockImpl.class);
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("tps").createEntityManager();


    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentIdentForAktoerIdRequest")
    @WebResult(name = "hentIdentForAktoerIdResponse", targetNamespace = "")
    @RequestWrapper(localName = "hentIdentForAktoerId", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerId")
    @ResponseWrapper(localName = "hentIdentForAktoerIdResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerIdResponse")
    public HentIdentForAktoerIdResponse hentIdentForAktoerId(
            @WebParam(name = "hentIdentForAktoerIdRequest", targetNamespace = "")
            HentIdentForAktoerIdRequest request)
            throws HentIdentForAktoerIdPersonIkkeFunnet {
        LOG.info("hentIdentForAktoerId: " + request.getAktoerId());

        String ident = new PersonDbLeser(entityManager).finnIdent(request.getAktoerId());
        if (ident == null) {
            throw new HentIdentForAktoerIdPersonIkkeFunnet("Fant ingen ident for aktoerid: " + request.getAktoerId(), new PersonIkkeFunnet());
        }
        HentIdentForAktoerIdResponse response = new HentIdentForAktoerIdResponse();
        response.setIdent(ident);
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentAktoerIdForIdentRequest")
    @WebResult(name = "hentAktoerIdForIdentResponse", targetNamespace = "")
    @RequestWrapper(localName = "hentAktoerIdForIdent", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdent")
    @ResponseWrapper(localName = "hentAktoerIdForIdentResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdentResponse")
    public HentAktoerIdForIdentResponse hentAktoerIdForIdent(
        @WebParam(name = "hentAktoerIdForIdentRequest", targetNamespace = "")
        HentAktoerIdForIdentRequest request)
        throws HentAktoerIdForIdentPersonIkkeFunnet {
        LOG.info("hentIdentForAktoerId: " + request.getIdent());

        Long aktoerId = new PersonDbLeser(entityManager).finnAktoerId(request.getIdent());
        if (aktoerId == null) {
            throw new HentAktoerIdForIdentPersonIkkeFunnet("Fant ingen aktoerid for ident: " + request.getIdent(), new PersonIkkeFunnet());
        }
        HentAktoerIdForIdentResponse response = new HentAktoerIdForIdentResponse();
        response.setAktoerId(String.valueOf(aktoerId));
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentAktoerIdForIdentListeRequest")
    @WebResult(name = "hentAktoerIdForIdentListeResponse", targetNamespace = "")
    @RequestWrapper(localName = "hentAktoerIdForIdentListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdentListe")
    @ResponseWrapper(localName = "hentAktoerIdForIdentListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdentListeResponse")
    public HentAktoerIdForIdentListeResponse hentAktoerIdForIdentListe(
        @WebParam(name = "hentAktoerIdForIdentListeRequest", targetNamespace = "")
        HentAktoerIdForIdentListeRequest hentAktoerIdForIdentListeRequest) {

        LOG.info("hentIdentForAktoerId: " + hentAktoerIdForIdentListeRequest.getIdentListe().stream().collect(Collectors.joining(",")));

        Map<String, Long> aktørTilIdent = hentAktoerIdForIdentListeRequest.getIdentListe().stream()
                .collect(Collectors.toMap(Function.identity(), ident -> new PersonDbLeser(entityManager).finnAktoerId(ident)));

        aktørTilIdent.forEach((ident, aktørId) -> {
            if (aktørId == null) {
                throw new RuntimeException("Fant ingen aktoerid for ident: " + ident);
            }
        });


        HentAktoerIdForIdentListeResponse response = new HentAktoerIdForIdentListeResponse();
        List<AktoerIder> aktoerListe = response.getAktoerListe();
        aktørTilIdent.forEach((ident, aktoerId) -> {
            AktoerIder aktoerIder = new AktoerIder();
            IdentDetaljer identDetaljer = new IdentDetaljer();
            identDetaljer.setDatoFom(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now().minusYears(1)));
            identDetaljer.setTpsId("Tullball, aner ikke hva dette brukes til");
            aktoerIder.setGjeldendeIdent(identDetaljer);
            aktoerIder.setAktoerId(aktoerId.toString());

            aktoerListe.add(aktoerIder);
        });

        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentIdentForAktoerIdListeRequest")
    @WebResult(name = "hentIdentForAktoerIdListeResponse", targetNamespace = "")
    @RequestWrapper(localName = "hentIdentForAktoerIdListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerIdListe")
    @ResponseWrapper(localName = "hentIdentForAktoerIdListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerIdListeResponse")
    public HentIdentForAktoerIdListeResponse hentIdentForAktoerIdListe(
        @WebParam(name = "hentIdentForAktoerIdListeRequest", targetNamespace = "")
        HentIdentForAktoerIdListeRequest hentIdentForAktoerIdListeRequest) {

        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2", className = "no.nav.tjeneste.virksomhet.aktoer.v2.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }
}
