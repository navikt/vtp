package no.nav.infotrygdpaaroerendesykdom.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdArbeidsforhold;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdPårørendeSykdomBeregningsgrunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdRammevedtaksGrunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.infotrygdpaaroerendesykdom.generated.model.Arbeidsforhold;
import no.nav.infotrygdpaaroerendesykdom.generated.model.Kodeverdi;
import no.nav.infotrygdpaaroerendesykdom.generated.model.PaaroerendeSykdom;
import no.nav.infotrygdpaaroerendesykdom.generated.model.Periode;
import no.nav.infotrygdpaaroerendesykdom.generated.model.RammevedtakDto;
import no.nav.infotrygdpaaroerendesykdom.generated.model.SakDto;
import no.nav.infotrygdpaaroerendesykdom.generated.model.SakResult;
import no.nav.infotrygdpaaroerendesykdom.generated.model.Vedtak;
import no.nav.infotrygdpaaroerendesykdom.generated.model.VedtakPleietrengendeDto;

@Path("/paaroerendeSykdom")
@RequestScoped
@Tag(name = "the paaroerendeSykdom API")
public class PårørendeSykdomMock {
    private TestscenarioBuilderRepository scenarioRepository;

    public PårørendeSykdomMock() {
    }

    public PårørendeSykdomMock(@Context TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

// todo: sjekk Autorization-token

    @SuppressWarnings("unused")
    @GET
    @Path("/saker")
    @Produces({(MediaType.APPLICATION_JSON)})
    @Operation(description = "hentSak", responses = {
            @ApiResponse(responseCode = "OK", description = "paaroerende-sykdom-controller", content = @Content(schema = @Schema(implementation  = SakResult.class))),
            @ApiResponse(responseCode = "UNAUTHORIZED", description = "Unauthorized")
    })
    public Response hentSakUsingGET(@NotNull @Parameter(name = "fnr", required = true) @QueryParam("fnr") String fnr,
                                    @NotNull @Parameter(name = "fom", required = true) @QueryParam("fom") LocalDate fom,
                                    @Parameter(name = "tom") @QueryParam("tom") LocalDate tom) {
        Optional<InntektYtelseModell> inntektYtelseModellOptional = scenarioRepository.getInntektYtelseModell(fnr);

        if (inntektYtelseModellOptional.isEmpty()) {
            return Response.ok(new SakDto()).build();
        }

        InntektYtelseModell inntektYtelseModell = inntektYtelseModellOptional.get();

        SakResult result = getSakResult(inntektYtelseModell);

        return Response.ok(result).build();
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/saker")
    @Produces({"application/json"})
    @Operation(description = "hentSak", responses = {
            @ApiResponse(responseCode = "OK", description = "paaroerende-sykdom-controller",  content = @Content(schema = @Schema(implementation  = SakResult[].class))),
            @ApiResponse(responseCode = "UNAUTHORIZED", description = "Unauthorized")
    })
    public Response hentSakUsingPost(PersonRequest personRequest) {
        var result = personRequest.fnr().stream().flatMap(fnr -> {
            Optional<InntektYtelseModell> inntektYtelseModellOptional = scenarioRepository.getInntektYtelseModell(fnr);
            if (inntektYtelseModellOptional.isEmpty()) {
                return Stream.empty();
            }
            InntektYtelseModell inntektYtelseModell = inntektYtelseModellOptional.get();
            return Stream.of(getSakResult(inntektYtelseModell));
        }).collect(Collectors.toList());

        return Response.ok(result).build();
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/grunnlag")
    @Produces({(MediaType.APPLICATION_JSON)})
    @Operation(description = "paaroerendeSykdom", responses = {
            @ApiResponse(responseCode = "OK", description = "paaroerendeSykdom", content = @Content(schema = @Schema(implementation  = PaaroerendeSykdom[].class))),
            @ApiResponse(responseCode = "UNAUTHORIZED", description = "Unauthorized")
    })
    public Response paaroerendeSykdomUsingGET1(@NotNull @Parameter(name = "fnr", required = true) @QueryParam("fnr") String fnr,
                                               @NotNull @Parameter(name = "fom", required = true) @QueryParam("fom") LocalDate fom,
                                               @Parameter(name = "tom") @QueryParam("tom") LocalDate tom) {
        Optional<InntektYtelseModell> inntektYtelseModell = scenarioRepository.getInntektYtelseModell(fnr);
        if (inntektYtelseModell.isEmpty()) {
            return Response.ok(List.of()).build();
        }

        List<PaaroerendeSykdom> result = inntektYtelseModell.get().infotrygdModell().grunnlag().stream()
                .filter(it -> it instanceof InfotrygdPårørendeSykdomBeregningsgrunnlag)
                .map(it -> mapGrunnlagToPaaroerendeSykdom((InfotrygdPårørendeSykdomBeregningsgrunnlag) it, fnr))
                .filter(it -> it.getTema().getKode().equals("BS"))
                .collect(Collectors.toList());

        return Response.ok(result).build();
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag")
    @Produces({"application/json"})
    @Operation(description = "paaroerendeSykdom", responses = {
            @ApiResponse(responseCode = "OK", description = "paaroerendeSykdom",  content = @Content(schema = @Schema(implementation  = PaaroerendeSykdom[].class))),
            @ApiResponse(responseCode = "AUTHORIZATION", description = "Unauthorized")
    })
    public Response paaroerendeSykdomUsingPost(PersonRequest personRequest) {
        var result = personRequest.fnr().stream().flatMap(fnr -> {
            Optional<InntektYtelseModell> inntektYtelseModell = scenarioRepository.getInntektYtelseModell(fnr);
            if (inntektYtelseModell.isEmpty()) {
                return Stream.empty();
            }

            return inntektYtelseModell.get().infotrygdModell().grunnlag().stream()
                    .filter(it -> it instanceof InfotrygdPårørendeSykdomBeregningsgrunnlag)
                    .map(it -> mapGrunnlagToPaaroerendeSykdom((InfotrygdPårørendeSykdomBeregningsgrunnlag) it, fnr))
                    .filter(it -> it.getTema().getKode().equals("BS"));
        }).collect(Collectors.toList());

        return Response.ok(result).build();
    }


    @SuppressWarnings("unused")
    @GET
    @Path("/vedtakForPleietrengende")
    @Produces({(MediaType.APPLICATION_JSON)})
    @Operation(description = "Finner vedtak basert på fødselsnummeret til pleietrengende.", responses = {
            @ApiResponse(responseCode = "OK", description = "paaroerendeSykdom",  content = @Content(schema = @Schema(implementation  = VedtakPleietrengendeDto[].class))),
            @ApiResponse(responseCode = "UNAUTHORIZED", description = "Unauthorized")
    })
    public Response finnVedtakForPleietrengendeUsingGET(@NotNull @Parameter(name = "Pleietrengendes fødselsnummer", required = true) @QueryParam("fnr") String fnr,
                                                        @NotNull @Parameter(name = "Fra-dato for søket. Matcher vedtaksperiode for vedtak eller registrertdato for saker.", required = true) @QueryParam("fom") LocalDate fom,
                                                        @Parameter(name = "Til-dato for søket. Matcher vedtaksperiode for vedtak eller registrertdato for saker.") @QueryParam("tom") LocalDate tom) {
        return Response.ok(List.of()).build();
    }


    @SuppressWarnings("unused")
    @POST
    @Path("/vedtakForPleietrengende")
    @Produces({(MediaType.APPLICATION_JSON)})
    @Operation(description = "Finner vedtak basert på fødselsnummeret til pleietrengende.", responses = {
            @ApiResponse(responseCode = "OK", description = "paaroerendeSykdom",  content = @Content(schema = @Schema(implementation  = VedtakPleietrengendeDto[].class))),
            @ApiResponse(responseCode = "UNAUTHORIZED", description = "Unauthorized")
    })
    public Response finnVedtakForPleietrengendeUsingPost(PersonRequest personRequest) {
        var alleVedtak = personRequest.fnr().stream().flatMap(fnr -> {
                    var vedtakList = new ArrayList<VedtakPleietrengendeDto>();
                    var personOpplysninger = scenarioRepository.getPersonIndeks().finnPersonopplysningerByIdent(fnr);
                    var søker = personOpplysninger.getSøker();
                    var annenPart = personOpplysninger.getAnnenPart();
                    if (søker != null) {
                        finnVedtak(søker).ifPresent(vedtakList::add);
                    }
                    if (annenPart != null) {
                        finnVedtak(annenPart).ifPresent(vedtakList::add);
                    }
                    return vedtakList.stream();
                }
        ).collect(Collectors.toList());
        return Response.ok(alleVedtak).build();
    }

    private Optional<VedtakPleietrengendeDto> finnVedtak(PersonModell person) {
        var inntektYtelseModell = scenarioRepository.getInntektYtelseModell(person.getIdent());
        return inntektYtelseModell.map(modell -> getVedtak(modell, person.getIdent()));
    }


    @GET
    @Path("/rammevedtak/omsorgspenger")
    @Produces({(MediaType.APPLICATION_JSON)})
    @Operation(description = "Finner rammevedtak basert på fødselsnummeret til søker.", responses = {
            @ApiResponse(responseCode = "OK", description = "paaroerendeSykdom",  content = @Content(schema = @Schema(implementation  = RammevedtakDto[].class))),
            @ApiResponse(responseCode = "UNAUTHORIZED", description = "Unauthorized")
    })
    public Response finnRammevedtakForOmsorgspengerUsingGET(@NotNull @Parameter(name = "Søkers fødselsnummer", required = true) @QueryParam("fnr") String fnr,
                                                            @NotNull @Parameter(name = "Fra-dato for søket. Matcher vedtaksperiode eller dato for rammevedtak.", required = true) @QueryParam("fom") LocalDate fom,
                                                            @Parameter(name = "Til-dato for søket. Matcher vedtaksperiode eller dato for rammevedtak.") @QueryParam("tom") LocalDate tom) {
        Optional<InntektYtelseModell> inntektYtelseModell = scenarioRepository.getInntektYtelseModell(fnr);
        if (inntektYtelseModell.isEmpty()) {
            return Response.ok(List.of()).build();
        }


        List<RammevedtakDto> result = inntektYtelseModell.get().infotrygdModell().grunnlag().stream()
                .filter(it -> it instanceof InfotrygdRammevedtaksGrunnlag)
                .map(it -> mapGrunnlagToRammevedtak((InfotrygdRammevedtaksGrunnlag) it))
                .filter(it -> datoOverlapper(fom, tom, it.getFom(), it.getTom()))
                //Legg til filter på fra og til (TODO)
                .collect(Collectors.toList());

        return Response.ok(result).build();
    }


    private boolean datoOverlapper(LocalDate fom1, LocalDate tom1, LocalDate fom2, LocalDate tom2) {
        if (fom1 == null) {
            fom1 = LocalDate.MIN;
        }
        if (tom1 == null) {
            tom1 = LocalDate.MAX;
        }
        if (fom2 == null) {
            fom2 = LocalDate.MIN;
        }
        if (tom2 == null) {
            tom2 = LocalDate.MAX;
        }
        return (fom1.isBefore(tom2) && (tom1.isAfter(fom2)));
    }

    private VedtakPleietrengendeDto getVedtak(InntektYtelseModell inntektYtelseModell, String søkerFnr) {
        List<SakDto> sakerOgVedtak = inntektYtelseModell.infotrygdModell().ytelser().stream()
                .map(this::mapYtelseToSak)
                .filter(it -> it.getTema().getKode().equals("BS"))
                .collect(Collectors.toList());

        VedtakPleietrengendeDto result = new VedtakPleietrengendeDto();
        result.setSoekerFnr(søkerFnr);
        result.setVedtak(sakerOgVedtak.stream().filter(s -> s.getOpphoerFom() != null).collect(Collectors.toList()));
        return result;
    }


    private SakResult getSakResult(InntektYtelseModell inntektYtelseModell) {
        List<SakDto> sakerOgVedtak = inntektYtelseModell.infotrygdModell().ytelser().stream()
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
        tema.setKode(ytelse.behandlingstema().getTema());
        tema.setTermnavn("ukjent tema");
        sak.setTema(tema);

        Kodeverdi behandlingstema = new Kodeverdi();
        behandlingstema.setKode(ytelse.behandlingstema().name());
        behandlingstema.setTermnavn("ukjent behandlingstema");
        sak.setBehandlingstema(behandlingstema);

        sak.iverksatt(toLocalDate(ytelse.iverksatt()));
        sak.setOpphoerFom(toLocalDate(ytelse.opphør()));
        sak.setRegistrert(toLocalDate(ytelse.registrert()));

        if (ytelse.resultat() != null) {
            Kodeverdi resultat = new Kodeverdi();
            resultat.setKode(ytelse.resultat().getKode());
            resultat.setTermnavn("ukjent resultat");
            sak.setResultat(resultat);
        }

        sak.setSakId(ytelse.sakId());

        if (ytelse.sakStatus() != null) {
            sak.setStatus(kodeverdi(ytelse.sakStatus().name()));
        }

        if (ytelse.sakType() != null) {
            sak.setType(kodeverdi(ytelse.sakType().getKode()));
        }

        sak.setVedtatt(toLocalDate(ytelse.vedtatt()));

        return sak;
    }

    private PaaroerendeSykdom mapGrunnlagToPaaroerendeSykdom(InfotrygdPårørendeSykdomBeregningsgrunnlag grunnlag, String fnrSøker) {
        PaaroerendeSykdom r = new PaaroerendeSykdom();

        r.foedselsnummerSoeker(fnrSøker);

        List<Arbeidsforhold> arbeidsforholdListe = grunnlag.getArbeidsforhold().stream()
                .map(this::mapArbeidsforhold)
                .collect(Collectors.toList());

        r.setArbeidsforhold(arbeidsforholdListe);

        if (grunnlag.getArbeidskategori() != null) {
            r.setArbeidskategori(kodeverdi(grunnlag.getArbeidskategori().getKode()));
        }

        r.setBehandlingstema(kodeverdi(grunnlag.getBehandlingTema().name()));
        r.setFoedselsdatoPleietrengende(grunnlag.getFødselsdatoPleietrengende());

        // Feltet finnes ikke i modellen. Legges til ved behov
        // r.setFoedselsnummerPleietrengende();

        r.setIdentdato(grunnlag.getStartdato());
        r.setIverksatt(grunnlag.getStartdato());

        if (grunnlag.getTom() != null) {
            r.setOpphoerFom(grunnlag.getTom().plusDays(1));
        }

        if (grunnlag.getFom() != null && grunnlag.getTom() != null) {
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

        r.setVedtak(grunnlag.getVedtak().stream().map(vedtak -> {
            Vedtak v = new Vedtak();
            Periode periode = new Periode();
            periode.setFom(vedtak.fom());
            periode.setTom(vedtak.tom());
            v.setPeriode(periode);
            v.setUtbetalingsgrad(vedtak.utbetalingsgrad());
            return v;
        }).collect(Collectors.toList()));

        return r;
    }

    private RammevedtakDto mapGrunnlagToRammevedtak(InfotrygdRammevedtaksGrunnlag it) {
        if (it == null) {
            return null;
        }
        RammevedtakDto rammevedtak = new RammevedtakDto();
        rammevedtak.setDate(it.getStartdato());
        rammevedtak.setFom(it.getFom());
        rammevedtak.setTom(it.getTom());
        rammevedtak.setTekst(it.getRammevedtakTekst());
        return rammevedtak;

    }

    private Arbeidsforhold mapArbeidsforhold(InfotrygdArbeidsforhold arbeidsforhold) {
        Arbeidsforhold af = new Arbeidsforhold();
        af.setArbeidsgiverOrgnr(arbeidsforhold.getOrgnr());
        af.setInntektForPerioden(arbeidsforhold.getBeløp());

        if (arbeidsforhold.getInntektsPeriodeType() != null) {
            af.setInntektsperiode(kodeverdi(arbeidsforhold.getInntektsPeriodeType().getKode()));
        }

        af.refusjon(false); // Feltet finnes ikke i modellen. Legges til ved behov

        return af;
    }

    private LocalDate toLocalDate(LocalDateTime localDateTime) {
        if (localDateTime != null) {
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
