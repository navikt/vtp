package no.nav.tjeneste.virksomhet.infotrygd.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.Grunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex.TRexModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

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
    @Path("/grunnlag/sykepenger")
    @Produces({"application/json"})
    @Operation(description = "foreldrepenger", responses = {
            @ApiResponse(responseCode = "OK", description = "OK", content =@Content(schema = @Schema(implementation  = Grunnlag.class))),
            @ApiResponse(responseCode = "UNAUTHORIZED", description = "UNAUTHORIZED")
    })
    public Grunnlag[] getSykepenger(PersonRequest personRequest) {
        return personRequest.fnr().stream().flatMap(fnr -> {
            LOG.info(LOG_PREFIX, "sykepenger");
            List<Grunnlag> tomresponse = new ArrayList<>();
            return scenarioRepository.getInntektYtelseModell(fnr)
                    .map(InntektYtelseModell::trexModell)
                    .map(TRexModell::sykepenger).orElse(tomresponse).stream();
        }).toArray(Grunnlag[]::new);
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

}
