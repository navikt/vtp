package no.nav.infotrygdpaaroerendesykdom.rest;

import io.swagger.annotations.*;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.infotrygdpaaroerendesykdom.generated.model.Kodeverdi;
import no.nav.infotrygdpaaroerendesykdom.generated.model.PaaroerendeSykdom;
import no.nav.infotrygdpaaroerendesykdom.generated.model.SakDto;
import no.nav.infotrygdpaaroerendesykdom.generated.model.SakResult;

import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/paaroerendeSykdom")
@RequestScoped
@Api(description = "the paaroerendeSykdom API")
public class PårørendeSykdomMock {
    private final TestscenarioBuilderRepository scenarioRepository;

    public PårørendeSykdomMock(@Context TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    // todo: sjekk Autorization-token

    @GET
    @Path("/sak")
    @Produces({ "application/json" })
    @ApiOperation(value = "hentSak", notes = "", response = SakResult.class, authorizations = {
            @Authorization(value = "JWT")
    }, tags={ "paaroerende-sykdom-controller",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SakResult.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Void.class) })
    public Response hentSakUsingGET( @NotNull @ApiParam(value = "fnr",required=true)  @QueryParam("fnr") String fnr,  @NotNull @ApiParam(value = "fom",required=true)  @QueryParam("fom") LocalDate fom,  @ApiParam(value = "tom")  @QueryParam("tom") LocalDate tom) {
        Optional<InntektYtelseModell> inntektYtelseModellOptional = scenarioRepository.getInntektYtelseModell(fnr);

        if(inntektYtelseModellOptional.isEmpty()) {
            return Response.ok(new SakDto()).build();
        }

        InntektYtelseModell inntektYtelseModell = inntektYtelseModellOptional.get();

        List<SakDto> xx = inntektYtelseModell.getInfotrygdModell().getYtelser().stream().map(ytelse -> {
            SakDto sak = new SakDto();

            Kodeverdi tema = new Kodeverdi();
            tema.setKode(ytelse.getBehandlingtema().getTema());
            tema.setTermnavn("ukjent tema");
            sak.setTema(tema);

            Kodeverdi behandlingstema = new Kodeverdi();
            behandlingstema.setKode(ytelse.getBehandlingtema().getKode());
            behandlingstema.setTermnavn("ukjent behandlingstema");
            sak.setBehandlingstema(behandlingstema);

            sak.iverksatt(toLocalDate(ytelse.getIverksatt()));
            sak.setOpphoerFom(toLocalDate(ytelse.getOpphørFom()));
            sak.setRegistrert(toLocalDate(ytelse.getRegistrert()));


            if(ytelse.getResultat() != null) {
                Kodeverdi resultat = new Kodeverdi();
                resultat.setKode(ytelse.getResultat().getKode());
                resultat.setTermnavn("ukjent resultat");
                sak.setResultat(resultat);
            }

            sak.setSakId(ytelse.getSakId());

            if(ytelse.getSakStatus() != null) {
                sak.setStatus(kodeverdi(ytelse.getSakStatus().getKode()));
            }

            if(ytelse.getSakType() != null) {
                sak.setType(kodeverdi(ytelse.getSakType().getKode()));
            }

            sak.setVedtatt(toLocalDate(ytelse.getVedtatt()));

            return sak;
        }).collect(Collectors.toList());

        SakResult result = new SakResult();
        result.setSaker(xx.stream().filter(s -> s.getOpphoerFom() == null).collect(Collectors.toList()));
        result.setVedtak(xx.stream().filter(s -> s.getOpphoerFom() != null).collect(Collectors.toList()));

        return Response.ok(result).build();
    }

    @GET
    @Path("/grunnlag")
    @Produces({ "application/json" })
    @ApiOperation(value = "paaroerendeSykdom", notes = "", response = PaaroerendeSykdom.class, responseContainer = "List", authorizations = {
            @Authorization(value = "JWT")
    }, tags={ "paaroerende-sykdom-controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PaaroerendeSykdom.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Void.class) })
    public Response paaroerendeSykdomUsingGET1( @NotNull @ApiParam(value = "fnr",required=true)  @QueryParam("fnr") String fnr,  @NotNull @ApiParam(value = "fom",required=true)  @QueryParam("fom") LocalDate fom,  @ApiParam(value = "tom")  @QueryParam("tom") LocalDate tom) {
        PaaroerendeSykdom p = new PaaroerendeSykdom();
        List<PaaroerendeSykdom> result = List.of(p);
        return Response.ok(result).build();
    }

    private LocalDate toLocalDate(LocalDateTime localDateTime) {
        if(localDateTime != null) {
            return localDateTime.toLocalDate();
        }
        return null;
    }

    private Kodeverdi kodeverdi(String kode) {
        Kodeverdi kodeverdi = new Kodeverdi();
        kodeverdi.setKode(kode);
        kodeverdi.setTermnavn("ukjent");
        return kodeverdi;
    }
}
