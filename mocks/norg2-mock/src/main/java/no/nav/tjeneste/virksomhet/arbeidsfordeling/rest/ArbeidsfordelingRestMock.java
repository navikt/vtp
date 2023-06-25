package no.nav.tjeneste.virksomhet.arbeidsfordeling.rest;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Tag(name = "ArbeidsfordelingMock")
@Path("/norg2/api/v1/arbeidsfordeling")
public class ArbeidsfordelingRestMock {

    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsfordelingRestMock.class);
    private static final String LOG_PREFIX = "Arbeidsfordeling Rest kall til {}";
    private final TestscenarioBuilderRepository scenarioRepository;

    public ArbeidsfordelingRestMock() {
        this.scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }

    @POST
    @Path("/enheter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer enheter fra NORG2")
    public ArbeidsfordelingResponse[] hentAlleEnheter(ArbeidsfordelingRequest request) {
        LOG.info(LOG_PREFIX, "allenheter");
        return scenarioRepository.getEnheterIndeks().getAlleEnheter().stream()
                .filter(e -> skalEnhetMed(e, request.getTema()))
                .map(e -> new ArbeidsfordelingResponse(e.enhetId(), e.navn(), e.status(), e.type()))
                .toArray(ArbeidsfordelingResponse[]::new);
    }

    private boolean skalEnhetMed(Norg2Modell enhet, String tema) {
        if (tema == null || enhet.tema() == null) return true;
        return tema.equalsIgnoreCase(enhet.tema());
    }

    @POST
    @Path("/enheter/bestmatch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer enheter fra NORG2")
    public ArbeidsfordelingResponse[] finnEnhet(ArbeidsfordelingRequest request) {
        LOG.info(LOG_PREFIX, "bestmatch");
        List<String> spesielleDiskrKoder = List.of("UFB", "SPSF", "SPFO");
        List<Norg2Modell> resultat = new ArrayList<>();
        if (request.getDiskresjonskode() != null && spesielleDiskrKoder.contains(request.getDiskresjonskode())) {
            resultat.add(scenarioRepository.getEnheterIndeks().finnByDiskresjonskode(request.getDiskresjonskode()));
        } else {
            resultat.add(scenarioRepository.getEnheterIndeks().finnByDiskresjonskode("NORMAL-"+request.getTema()));
        }
        return resultat.stream()
                .map(e -> new ArbeidsfordelingResponse(e.enhetId(), e.navn(), e.status(), e.type()))
                .toArray(ArbeidsfordelingResponse[]::new);
    }

}
