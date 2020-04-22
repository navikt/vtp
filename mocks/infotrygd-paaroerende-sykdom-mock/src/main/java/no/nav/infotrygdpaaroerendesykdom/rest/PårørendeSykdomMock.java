package no.nav.infotrygdpaaroerendesykdom.rest;

import io.swagger.annotations.*;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdArbeidsforhold;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdPårørendeSykdomBeregningsgrunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.infotrygdpaaroerendesykdom.generated.model.*;

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

    @SuppressWarnings("unused")
    @GET
    @Path("/saker")
    @Produces({ "application/json" })
    @ApiOperation(value = "hentSak", notes = "", response = SakResult.class, authorizations = {
            @Authorization(value = "JWT")
    }, tags={ "paaroerende-sykdom-controller",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SakResult.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class)})
    public Response hentSakUsingGET( @NotNull @ApiParam(value = "fnr",required=true)  @QueryParam("fnr") String fnr,  @NotNull @ApiParam(value = "fom",required=true)  @QueryParam("fom") LocalDate fom,  @ApiParam(value = "tom")  @QueryParam("tom") LocalDate tom) {
        Optional<InntektYtelseModell> inntektYtelseModellOptional = scenarioRepository.getInntektYtelseModell(fnr);

        if(inntektYtelseModellOptional.isEmpty()) {
            return Response.ok(new SakDto()).build();
        }

        InntektYtelseModell inntektYtelseModell = inntektYtelseModellOptional.get();

        SakResult result = getSakResult(inntektYtelseModell);

        return Response.ok(result).build();
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/grunnlag")
    @Produces({ "application/json" })
    @ApiOperation(value = "paaroerendeSykdom", notes = "", response = PaaroerendeSykdom.class, responseContainer = "List", authorizations = {
            @Authorization(value = "JWT")
    }, tags={ "paaroerende-sykdom-controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PaaroerendeSykdom.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class)})
    public Response paaroerendeSykdomUsingGET1( @NotNull @ApiParam(value = "fnr",required=true)  @QueryParam("fnr") String fnr,  @NotNull @ApiParam(value = "fom",required=true)  @QueryParam("fom") LocalDate fom,  @ApiParam(value = "tom")  @QueryParam("tom") LocalDate tom) {
        Optional<InntektYtelseModell> inntektYtelseModell = scenarioRepository.getInntektYtelseModell(fnr);
        if(inntektYtelseModell.isEmpty()) {
            return Response.ok(List.of()).build();
        }

        List<PaaroerendeSykdom> result = inntektYtelseModell.get().getInfotrygdModell().getGrunnlag().stream()
                .filter(it -> it instanceof InfotrygdPårørendeSykdomBeregningsgrunnlag)
                .map(it -> mapGrunnlagToPaaroerendeSykdom((InfotrygdPårørendeSykdomBeregningsgrunnlag) it))
                .filter(it -> it.getTema().getKode().equals("BS"))
                .collect(Collectors.toList());

        return Response.ok(result).build();
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/vedtakForPleietrengende")
    @Produces({ "application/json" })
    @ApiOperation(value = "Finner vedtak basert på fødselsnummeret til pleietrengende.", notes = "", response = VedtakPleietrengendeDto.class, responseContainer = "List", authorizations = {
            @Authorization(value = "JWT")
    }, tags={ "paaroerende-sykdom-controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = VedtakPleietrengendeDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class)})
    public Response finnVedtakForPleietrengendeUsingGET( @NotNull @ApiParam(value = "Pleietrengendes fødselsnummer",required=true)  @QueryParam("fnr") String fnr,  @NotNull @ApiParam(value = "Fra-dato for søket. Matcher vedtaksperiode for vedtak eller registrertdato for saker.",required=true)  @QueryParam("fom") LocalDate fom,  @ApiParam(value = "Til-dato for søket. Matcher vedtaksperiode for vedtak eller registrertdato for saker.")  @QueryParam("tom") LocalDate tom) {
        return Response.ok(List.of()).build();
    }

    @GET
    @Path("/rammevedtak/omsorgspenger")
    @Produces({ "application/json" })
    @ApiOperation(value = "Finner rammevedtak basert på fødselsnummeret til søker.", notes = "", response = RammevedtakDto.class, responseContainer = "List", authorizations = {
            @Authorization(value = "JWT")
    }, tags={ "paaroerende-sykdom-controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = RammevedtakDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class) })
    public Response finnRammevedtakForOmsorgspengerUsingGET( @NotNull @ApiParam(value = "Søkers fødselsnummer",required=true)  @QueryParam("fnr") String fnr,  @NotNull @ApiParam(value = "Fra-dato for søket. Matcher vedtaksperiode eller dato for rammevedtak.",required=true)  @QueryParam("fom") LocalDate fom,  @ApiParam(value = "Til-dato for søket. Matcher vedtaksperiode eller dato for rammevedtak.")  @QueryParam("tom") LocalDate tom) {
        return Response.ok(List.of()).build();
    }

    private SakResult getSakResult(InntektYtelseModell inntektYtelseModell) {
        List<SakDto> sakerOgVedtak = inntektYtelseModell.getInfotrygdModell().getYtelser().stream()
                .map(this::mapYtelseToSak)
                .filter(it -> it.getTema().getKode().equals("BS"))
                .collect(Collectors.toList());

        SakResult result = new SakResult();
        result.setSaker(sakerOgVedtak.stream().filter(s -> s.getOpphoerFom() == null).collect(Collectors.toList()));
        result.setVedtak(sakerOgVedtak.stream().filter(s -> s.getOpphoerFom() != null).collect(Collectors.toList()));
        return result;
    }

    private SakDto mapYtelseToSak(InfotrygdYtelse ytelse) {
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
    }

    private PaaroerendeSykdom mapGrunnlagToPaaroerendeSykdom(InfotrygdPårørendeSykdomBeregningsgrunnlag grunnlag) {
        PaaroerendeSykdom r = new PaaroerendeSykdom();

        List<Arbeidsforhold> arbeidsforholdListe = grunnlag.getArbeidsforhold().stream()
                .map(this::mapArbeidsforhold)
                .collect(Collectors.toList());

        r.setArbeidsforhold(arbeidsforholdListe);

        if(grunnlag.getArbeidskategori() != null) {
            r.setArbeidskategori(kodeverdi(grunnlag.getArbeidskategori().getKode()));
        }

        r.setBehandlingstema(kodeverdi(grunnlag.getBehandlingTema().getKode()));
        r.setFoedselsdatoPleietrengende(grunnlag.getFødselsdatoPleietrengende());

        // Feltet finnes ikke i modellen. Legges til ved behov
        // r.setFoedselsnummerPleietrengende();

        r.setIdentdato(grunnlag.getStartdato());
        r.setIverksatt(grunnlag.getStartdato());

        if(grunnlag.getTom() != null) {
            r.setOpphoerFom(grunnlag.getTom().plusDays(1));
        }

        if(grunnlag.getFom() != null && grunnlag.getTom() != null) {
            Periode periode = new Periode();
            periode.setFom(grunnlag.getFom());
            periode.setTom(grunnlag.getTom());
            r.setPeriode(periode);
        }

        // Disse feltene finnes ikke i modellen. Legges til ved behov
        // r.setRegistrert();
        // r.setSaksbehandlerId();
        // r.setStatus(); // ingen verdi for PN, men har for de andre ytelsene

        r.setTema(kodeverdi(grunnlag.getBehandlingTema().getTema()));

        r.setVedtak(grunnlag.getVedtak().stream().map( vedtak -> {
            Vedtak v = new Vedtak();
            Periode periode = new Periode();
            periode.setFom(vedtak.getFom());
            periode.setTom(vedtak.getTom());
            v.setPeriode(periode);
            v.setUtbetalingsgrad(vedtak.getUtbetalingsgrad());
            return v;
        }).collect(Collectors.toList()));

        return r;
    }

    private Arbeidsforhold mapArbeidsforhold(InfotrygdArbeidsforhold arbeidsforhold) {
        Arbeidsforhold af = new Arbeidsforhold();
        af.setArbeidsgiverOrgnr(arbeidsforhold.getOrgnr());
        af.setInntektForPerioden(arbeidsforhold.getBeløp());

        if(arbeidsforhold.getInntektsPeriodeType() != null) {
            af.setInntektsperiode(kodeverdi(arbeidsforhold.getInntektsPeriodeType().getKode()));
        }

        af.refusjon(false); // Feltet finnes ikke i modellen. Legges til ved behov

        return af;
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
