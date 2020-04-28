package no.nav.tjeneste.virksomhet.arbeidsfordeling.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Api(tags = {"ArbeidsfordelingMock"})
@Path("/norg2/api/v1/arbeidsfordeling")
public class ArbeidsfordelingRestMock {

    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsfordelingRestMock.class);
    private static final String LOG_PREFIX = "Arbeidsfordeling Rest kall til {}";
    private TestscenarioBuilderRepository scenarioRepository;

    public ArbeidsfordelingRestMock() {
        try {
            this.scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @POST
    @Path("/enheter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "allenheter", notes = ("Returnerer enheter fra NORG2"))
    public ArbeidsfordelingResponse[] hentAlleEnheter(ArbeidsfordelingRequest request) {
        LOG.info(LOG_PREFIX, "allenheter");
        return scenarioRepository.getEnheterIndeks().getAlleEnheter().stream()
                .filter(e -> skalEnhetMed(e, request.getTema()))
                .map(e -> new ArbeidsfordelingResponse(e.getEnhetId(), e.getNavn(), e.getStatus(), e.getType()))
                .toArray(ArbeidsfordelingResponse[]::new);
    }

    private boolean skalEnhetMed(Norg2Modell enhet, String tema) {
        if (tema == null || enhet.getTema() == null) return true;
        return tema.equalsIgnoreCase(enhet.getTema());
    }

    @POST
    @Path("/enheter/bestmatch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "finnenhet", notes = ("Returnerer  enheter fra NORG2"))
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
                .map(e -> new ArbeidsfordelingResponse(e.getEnhetId(), e.getNavn(), e.getStatus(), e.getType()))
                .toArray(ArbeidsfordelingResponse[]::new);
    }

}
