package no.nav.tjeneste.virksomhet.arbeidsfordeling.v1;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.ArbeidsfordelingV1;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.FinnAlleBehandlendeEnheterListeUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.FinnBehandlendeEnhetListeUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.informasjon.*;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.*;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnAlleBehandlendeEnheterListeResponse;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnBehandlendeEnhetListeResponse;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.modell.Norg2DbLeser;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.modell.Norg2Entitet;

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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Addressing
@WebService(
        name = "Arbeidsfordeling_v1",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/"
)
@HandlerChain(file="Handler-chain.xml")
public class ArbeidsfordelingMockImpl implements ArbeidsfordelingV1 {

    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("norg2").createEntityManager();
    private static final String RESPONSE_ENHETS_ID = "1234";

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnBehandlendeEnhetListeRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "finnBehandlendeEnhetListe",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListe"
    )
    @ResponseWrapper(
            localName = "finnBehandlendeEnhetListeResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListeResponse"
    )
    @Override
    public FinnBehandlendeEnhetListeResponse finnBehandlendeEnhetListe(
            @WebParam(name = "request",targetNamespace = "") FinnBehandlendeEnhetListeRequest request)
            throws FinnBehandlendeEnhetListeUgyldigInput {

        Norg2DbLeser dbLeser = new Norg2DbLeser(entityManager);
        List<Norg2Entitet> norg2Liste = dbLeser.lesAlle();
        Map<String,String> norg2Map = new HashMap<>();
        norg2Liste.forEach(norg2 -> norg2Map.put(norg2.getNokkel(), norg2.getVerdi()));

        Geografi kommune = request.getArbeidsfordelingKriterier().getGeografiskTilknytning();
        String kommuneStr = (kommune != null ? kommune.getValue() : null);
        simulerFunksjonelleFeil(norg2Map, kommuneStr);

        Diskresjonskoder diskrKode = request.getArbeidsfordelingKriterier().getDiskresjonskode();
        String diskrKodeStr = (diskrKode != null ? diskrKode.getValue() : null);
        Organisasjonsenhet enhet = lagOrganisasjonsenhet(norg2Map, diskrKodeStr);

        FinnBehandlendeEnhetListeResponse response = new FinnBehandlendeEnhetListeResponse();
        response.getBehandlendeEnhetListe().add(enhet);

        return response;
    }

    private Organisasjonsenhet lagOrganisasjonsenhet(Map<String,String> norg2Map, String diskrKode) {

        List<String> spesielleDiskrKoder = Arrays.asList("UFB", "SPSF", "SPFO");

        String nøkkelPrefix;
        if (diskrKode != null && spesielleDiskrKoder.contains(diskrKode)) {
            nøkkelPrefix = diskrKode;
        } else {
            nøkkelPrefix = "NORMAL";
        }
        nøkkelPrefix += "_";

        Organisasjonsenhet enhet = new Organisasjonsenhet();

        enhet.setEnhetId(norg2Map.get(nøkkelPrefix + "ENHETID"));

        enhet.setEnhetNavn(norg2Map.get(nøkkelPrefix + "NAVN"));

        enhet.setOrganisasjonsnummer(norg2Map.get(nøkkelPrefix + "ORGANISASJONSNUMMER"));

        String status = norg2Map.get(nøkkelPrefix + "STATUS");
        if (status != null) {
            enhet.setStatus(Enhetsstatus.fromValue(status));
        }

        String typeStr = norg2Map.get(nøkkelPrefix + "TYPE");
        if (typeStr != null) {
            Enhetstyper type = new Enhetstyper();
            type.setValue(typeStr);
            enhet.setType(type);
        }

        return enhet;
    }

    private void simulerFunksjonelleFeil(Map<String,String> norg2Map, String kommune) throws FinnBehandlendeEnhetListeUgyldigInput {

        String feilkommune = norg2Map.get("FEILKOMMUNE");
        if (feilkommune != null && !feilkommune.isEmpty()) {
            if (kommune != null && !kommune.isEmpty()) {
                if (kommune.equals(feilkommune)) {
                    UgyldigInput ugyldigInput = new UgyldigInput();
                    ugyldigInput.setFeilaarsak("FEILKOMMUNE");
                    ugyldigInput.setFeilkilde("mock");
                    ugyldigInput.setFeilmelding("simulert feil");
                    ugyldigInput.setTidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now()));
                    throw new FinnBehandlendeEnhetListeUgyldigInput("feil", ugyldigInput);
                }
            }
        }
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/pingRequest"
    )
    @RequestWrapper(
            localName = "ping",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.Ping"
    )
    @ResponseWrapper(
            localName = "pingResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.PingResponse"
    )
    @Override
    public void ping() {
        // tom
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnAlleBehandlendeEnheterListeRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "finnAlleBehandlendeEnheterListe",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListe"
    )
    @ResponseWrapper(
            localName = "finnAlleBehandlendeEnheterListeResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
            className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListeResponse"
    )
    @Override
    public FinnAlleBehandlendeEnheterListeResponse finnAlleBehandlendeEnheterListe(
            @WebParam(name = "request",targetNamespace = "") FinnAlleBehandlendeEnheterListeRequest request)
            throws FinnAlleBehandlendeEnheterListeUgyldigInput {

        FinnAlleBehandlendeEnheterListeResponse response = new FinnAlleBehandlendeEnheterListeResponse();
        Organisasjonsenhet enhet1 = new Organisasjonsenhet();
        enhet1.setEnhetId(RESPONSE_ENHETS_ID);
        enhet1.setEnhetNavn("Anne Lier");
        enhet1.setOrganisasjonsnummer("5443");
        enhet1.setStatus(Enhetsstatus.AKTIV);
        response.getBehandlendeEnhetListe().add(enhet1);

        return response;
    }

}
