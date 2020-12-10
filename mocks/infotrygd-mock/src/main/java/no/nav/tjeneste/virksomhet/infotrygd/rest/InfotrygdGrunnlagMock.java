package no.nav.tjeneste.virksomhet.infotrygd.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Grunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.TRexModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Api(tags = {"Infotrygdmock/grunnlag"})
@Path("/infotrygd/grunnlag")
public class InfotrygdGrunnlagMock {

    private static final Logger LOG = LoggerFactory.getLogger(InfotrygdGrunnlagMock.class);
    private static final String LOG_PREFIX = "InfotrygdGrunnlag Rest kall til {}";
    private TestscenarioBuilderRepository scenarioRepository;

    public InfotrygdGrunnlagMock() {
        try {
            this.scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/foreldrepenger")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer foreldrepenger fra Infotrygd"))
    public Grunnlag[] getForeldrepenger(@QueryParam("fnr") String fnr,
                                        @QueryParam("fom") String fom,
                                        @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "foreldrepenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return scenarioRepository.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::foreldrepenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    /**
     * @param tom
     */
    @SuppressWarnings("unused")
    @GET
    @Path("/svangerskapspenger")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer svangerskapspenger fra Infotrygd"))
    public Grunnlag[] getSvangerskapspenger(@QueryParam("fnr") String fnr,
                                        @QueryParam("fom") String fom,
                                        @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "svangerskapspenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return scenarioRepository.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::svangerskapspenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/sykepenger")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer sykepenger fra Infotrygd"))
    public Grunnlag[] getSykepenger(@QueryParam("fnr") String fnr,
                                            @QueryParam("fom") String fom,
                                            @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "sykepenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return scenarioRepository.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::sykepenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/paaroerende-sykdom")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer barns sykdom fra Infotrygd"))
    public Grunnlag[] getPaaroerendeSykdom(@QueryParam("fnr") String fnr,
                                    @QueryParam("fom") String fom,
                                    @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "pårørendesykdom");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return scenarioRepository.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::barnsykdom).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

}
