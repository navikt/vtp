package no.nav.tjeneste.virksomhet.arbeidsfordeling.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/norg2/api/v1/arbeidsfordeling")
public class ArbeidsfordelingRestMock {

    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsfordelingRestMock.class);
    private static final String LOG_PREFIX = "Arbeidsfordeling Rest kall til {}";

    // Kopi av enheter.json
    private final Map<String, Norg2Modell> ENHETER = Map.ofEntries(
            Map.entry("UFB", new Norg2Modell("UFB", "1900", "NAV Troms", "KONTAKT", "AKTIV", null)),
            Map.entry("SPFO", new Norg2Modell("SPFO", "1001", "NAV Kristiansand", "KONTAKT", "AKTIV", null)),
            Map.entry("SPSF", new Norg2Modell("SPSF", "2103", "NAV Viken", "KO", "AKTIV", null)), Map.entry("NORMAL-FOR",
                    new Norg2Modell("NORMAL-FOR", "4833", "NAV Familie og Pensjonsytelser Oslo", "FPY", "AKTIV", "FOR")),
            Map.entry("NORMAL-OMS", new Norg2Modell("NORMAL-OMS", "4409", "NAV Arbeid og ytelser Arendal", "YTA", "AKTIV", "OMS")),
            Map.entry("NORMAL-SYK", new Norg2Modell("NORMAL-SYK", "4409", "NAV Arbeid og ytelser Arendal", "YTA", "AKTIV", "SYK")),
            Map.entry("NORMAL-FRI",
                    new Norg2Modell("NORMAL-FRI", "4863", "NAV Familie- og pensjonsytelser midlertidig enhet", "KO", "AKTIV", "FRI")),
            Map.entry("NORMAL-UNG", new Norg2Modell("NORMAL-UNG", "4409", "NAV Arbeid og ytelser Arendal", "YTA", "AKTIV", "UNG")));

    @POST
    @Path("/enheter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ArbeidsfordelingResponse[] hentAlleEnheter(ArbeidsfordelingRequest request) {
        LOG.info(LOG_PREFIX, "allenheter");
        return ENHETER.values().stream()
                .filter(e -> skalEnhetMed(e, request.getTema()))
                .map(e -> new ArbeidsfordelingResponse(e.enhetId(), e.navn(), e.status(), e.type()))
                .toArray(ArbeidsfordelingResponse[]::new);
    }

    private boolean skalEnhetMed(Norg2Modell enhet, String tema) {
        if (tema == null || enhet.tema() == null) {
            return true;
        }
        return tema.equalsIgnoreCase(enhet.tema());
    }

    @POST
    @Path("/enheter/bestmatch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ArbeidsfordelingResponse[] finnEnhet(ArbeidsfordelingRequest request) {
        LOG.info(LOG_PREFIX, "bestmatch");
        List<String> spesielleDiskrKoder = List.of("UFB", "SPSF", "SPFO");
        List<Norg2Modell> resultat = new ArrayList<>();
        if (request.getDiskresjonskode() != null && spesielleDiskrKoder.contains(request.getDiskresjonskode())) {
            resultat.add(ENHETER.get(request.getDiskresjonskode()));
        } else {
            resultat.add(ENHETER.get("NORMAL-" + request.getTema()));
        }
        return resultat.stream()
                .map(e -> new ArbeidsfordelingResponse(e.enhetId(), e.navn(), e.status(), e.type()))
                .toArray(ArbeidsfordelingResponse[]::new);
    }

}
