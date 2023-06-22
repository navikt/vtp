package no.nav.tjeneste.virksomhet.infotrygd.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Grunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.TRexModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.infotrygd.rest.saker.Saker;

@Tag(name = "Infotrygdmock")
@Path("/infotrygd")
public class InfotrygdMock {

    private static final Logger LOG = LoggerFactory.getLogger(InfotrygdMock.class);
    private static final String LOG_PREFIX = "Infotrygd Rest kall til {}";

    private final TestscenarioBuilderRepository scenarioRepository;

    public InfotrygdMock(@Context TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/grunnlag/foreldrepenger")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer foreldrepenger fra Infotrygd")
    public Grunnlag[] getForeldrepenger(@QueryParam("fnr") String fnr,
                                        @QueryParam("fom") String fom,
                                        @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "foreldrepenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return scenarioRepository.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::trexModell)
                .map(TRexModell::foreldrepenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    /**
     * @param tom
     */
    @SuppressWarnings("unused")
    @GET
    @Path("/grunnlag/svangerskapspenger")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer svangerskapspenger fra Infotrygd")
    public Grunnlag[] getSvangerskapspenger(@QueryParam("fnr") String fnr,
                                            @QueryParam("fom") String fom,
                                            @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "svangerskapspenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return scenarioRepository.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::trexModell)
                .map(TRexModell::svangerskapspenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/grunnlag/sykepenger")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer sykepenger fra Infotrygd")
    public Grunnlag[] getSykepenger(@QueryParam("fnr") String fnr,
                                    @QueryParam("fom") String fom,
                                    @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "sykepenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return scenarioRepository.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::trexModell)
                .map(TRexModell::sykepenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/grunnlag/paaroerende-sykdom")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer barns sykdom fra Infotrygd")
    public Grunnlag[] getPaaroerendeSykdom(@QueryParam("fnr") String fnr,
                                           @QueryParam("fom") String fom,
                                           @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "pårørendesykdom");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return scenarioRepository.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::trexModell)
                .map(TRexModell::barnsykdom).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag/paaroerende-sykdom")
    @Produces({"application/json"})
    @Operation(description = "foreldrepenger", responses = {
            @ApiResponse(responseCode = "OK", description = "OK", content =@Content(schema = @Schema(implementation  = Grunnlag.class))),
            @ApiResponse(responseCode = "UNAUTHORIZED", description = "UNAUTHORIZED")
    })
    public Grunnlag[] paaroerendeSykdomUsingPost(PersonRequest personRequest) {
        return personRequest.fnr().stream().flatMap(fnr -> {
            LOG.info(LOG_PREFIX, "pårørendesykdom");
            List<Grunnlag> tomresponse = new ArrayList<>();
            return scenarioRepository.getInntektYtelseModell(fnr)
                    .map(InntektYtelseModell::trexModell)
                    .map(TRexModell::barnsykdom).orElse(tomresponse).stream();
        }).toArray(Grunnlag[]::new);
    }

    @GET
    @Path("/saker/foreldrepenger")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer dummy respons av saker fra Infotrygd")
    public Response hentSakUsingGET(@QueryParam("fnr") String fnr,
                                    @QueryParam("fom") LocalDate fom,
                                    @QueryParam("tom") LocalDate tom) {
        LOG.info(LOG_PREFIX, "saker");
        return Response.ok(new Saker()).build();
    }
}
