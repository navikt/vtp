package no.nav.infotrygdpaaroerendesykdom.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdArbeidsforhold;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdPårørendeSykdomBeregningsgrunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdRammevedtaksGrunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

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
    @POST
    @Path("/saker")
    @Produces({"application/json"})
    @Operation(description = "hentSak", responses = {@ApiResponse(responseCode = "OK", description = "paaroerende-sykdom-controller", content = @Content(schema = @Schema(implementation = SakResult[].class))), @ApiResponse(responseCode = "UNAUTHORIZED", description = "Unauthorized")})
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
    @POST
    @Path("/grunnlag")
    @Produces({"application/json"})
    @Operation(description = "paaroerendeSykdom", responses = {@ApiResponse(responseCode = "OK", description = "paaroerendeSykdom", content = @Content(schema = @Schema(implementation = PaaroerendeSykdom[].class))), @ApiResponse(responseCode = "AUTHORIZATION", description = "Unauthorized")})
    public Response paaroerendeSykdomUsingPost(PersonRequest personRequest) {
        var result = personRequest.fnr().stream().flatMap(fnr -> {
            Optional<InntektYtelseModell> inntektYtelseModell = scenarioRepository.getInntektYtelseModell(fnr);
            if (inntektYtelseModell.isEmpty()) {
                return Stream.empty();
            }

            return inntektYtelseModell.get()
                    .infotrygdModell()
                    .grunnlag()
                    .stream()
                    .filter(InfotrygdPårørendeSykdomBeregningsgrunnlag.class::isInstance)
                    .map(it -> mapGrunnlagToPaaroerendeSykdom((InfotrygdPårørendeSykdomBeregningsgrunnlag) it, fnr))
                    .filter(it -> it.tema().kode().equals("BS"));
        }).collect(Collectors.toList());

        return Response.ok(result).build();
    }


    @SuppressWarnings("unused")
    @POST
    @Path("/vedtakForPleietrengende")
    @Produces({(MediaType.APPLICATION_JSON)})
    @Operation(description = "Finner vedtak basert på fødselsnummeret til pleietrengende.", responses = {@ApiResponse(responseCode = "OK", description = "paaroerendeSykdom", content = @Content(schema = @Schema(implementation = VedtakPleietrengendeDto[].class))), @ApiResponse(responseCode = "UNAUTHORIZED", description = "Unauthorized")})
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
        }).collect(Collectors.toList());
        return Response.ok(alleVedtak).build();
    }

    private Optional<VedtakPleietrengendeDto> finnVedtak(PersonModell person) {
        var inntektYtelseModell = scenarioRepository.getInntektYtelseModell(person.getIdent());
        return inntektYtelseModell.map(modell -> getVedtak(modell, person.getIdent()));
    }


    @POST
    @Path("/rammevedtak/omsorgspenger")
    @Produces({(MediaType.APPLICATION_JSON)})
    @Operation(description = "Finner rammevedtak basert på fødselsnummeret til søker.", responses = {@ApiResponse(responseCode = "OK", description = "paaroerendeSykdom", content = @Content(schema = @Schema(implementation = RammevedtakDto[].class))), @ApiResponse(responseCode = "UNAUTHORIZED", description = "Unauthorized")})
    public Response finnRammevedtakForOmsorgspenger(@NotNull PersonRequest request) {
        if (request.fnr.size() != 1){
            throw new IllegalArgumentException("Forventet nøyaktig ett FNR, fikk " + request.fnr.size());
        }
        Optional<InntektYtelseModell> inntektYtelseModell = scenarioRepository.getInntektYtelseModell(request.fnr.getFirst());
        if (inntektYtelseModell.isEmpty()) {
            return Response.ok(List.of()).build();
        }


        List<RammevedtakDto> result = inntektYtelseModell.get()
                .infotrygdModell()
                .grunnlag()
                .stream()
                .filter(InfotrygdRammevedtaksGrunnlag.class::isInstance)
                .map(it -> mapGrunnlagToRammevedtak((InfotrygdRammevedtaksGrunnlag) it))
                .filter(it -> datoOverlapper(request.fom, request.tom, it.fom(), it.tom()))
                //Legg til filter på fra og til (TODO)
                .collect(Collectors.toList());

        return Response.ok(result).build();
    }

    public record PersonRequest(@NotNull LocalDate fom, LocalDate tom, @NotNull List<String> fnr, Boolean inkluderHistoriskeIdenter) {
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
        List<SakDto> sakerOgVedtak = inntektYtelseModell.infotrygdModell()
                .ytelser()
                .stream()
                .map(this::mapYtelseToSak)
                .filter(it -> it.tema().kode().equals("BS"))
                .toList();

        return new VedtakPleietrengendeDto(søkerFnr,
                sakerOgVedtak.stream().filter(s -> s.opphoerFom() != null).toList());
    }


    private SakResult getSakResult(InntektYtelseModell inntektYtelseModell) {
        var sakerOgVedtak = inntektYtelseModell.infotrygdModell()
                .ytelser()
                .stream()
                .map(this::mapYtelseToSak)
                .filter(it -> it.tema().kode().equals("BS"))
                .toList();

        var saker = sakerOgVedtak.stream().filter(s -> s.opphoerFom() == null).toList();
        var vedtak = sakerOgVedtak.stream().filter(s -> s.opphoerFom() != null).toList();
        return new SakResult(saker, vedtak);
    }

    private SakDto mapYtelseToSak(InfotrygdYtelse ytelse) {
        var tema = kodeverdi(ytelse.behandlingstema().getTema());
        var behandlingstema = kodeverdi(ytelse.behandlingstema().name());

        var resultat = Optional.ofNullable(ytelse.resultat()).map(k -> kodeverdi(k.getKode())).orElse(null);
        var sakStatus = Optional.ofNullable(ytelse.sakStatus()).map(k -> kodeverdi(k.name())).orElse(null);
        var sakType = Optional.ofNullable(ytelse.sakType()).map(st -> kodeverdi(st.getKode())).orElse(null);

        return new SakDto(behandlingstema, toLocalDate(ytelse.iverksatt()),
                toLocalDate(ytelse.opphør()),
                toLocalDate(ytelse.registrert()),
                resultat,
                ytelse.sakId(),
                sakStatus,
                tema,
                sakType,
                toLocalDate(ytelse.vedtatt()));
    }

    private PaaroerendeSykdom mapGrunnlagToPaaroerendeSykdom(InfotrygdPårørendeSykdomBeregningsgrunnlag grunnlag, String fnrSøker) {



        List<Arbeidsforhold> arbeidsforholdListe = grunnlag.getArbeidsforhold()
                .stream()
                .map(this::mapArbeidsforhold)
                .collect(Collectors.toList());

        var arbKat = Optional.ofNullable(grunnlag.getArbeidskategori()).map(k -> kodeverdi(k.getKode())).orElse(null);
        var behandlingstema = kodeverdi(grunnlag.getBehandlingTema().name());

        var opphørFom = Optional.ofNullable(grunnlag.getTom()).map(t -> t.plusDays(1)).orElse(null);
        var periode = grunnlag.getFom() != null && grunnlag.getTom() != null ? new Periode(grunnlag.getFom(), grunnlag.getTom()) : null;
        var tema = kodeverdi(grunnlag.getBehandlingTema().getTema());
        var vedtakene = grunnlag.getVedtak().stream()
                .map(vedtak -> new Vedtak(new Periode(vedtak.fom(), vedtak.tom()), vedtak.utbetalingsgrad()))
                .toList();

        return new PaaroerendeSykdom(fnrSøker,
                arbeidsforholdListe, arbKat, behandlingstema,
                grunnlag.getFødselsdatoPleietrengende(), null,
                grunnlag.getStartdato(), grunnlag.getStartdato(), opphørFom,
                periode, null, null, null, tema, vedtakene);
    }

    private RammevedtakDto mapGrunnlagToRammevedtak(InfotrygdRammevedtaksGrunnlag it) {
        if (it == null) {
            return null;
        }
        return new RammevedtakDto(it.getStartdato(), it.getRammevedtakTekst(), it.getFom(), it.getTom());
    }

    private Arbeidsforhold mapArbeidsforhold(InfotrygdArbeidsforhold arbeidsforhold) {
        var ipt = Optional.ofNullable(arbeidsforhold.getInntektsPeriodeType())
                .map(kode -> kodeverdi(kode.getKode()))
                .orElse(null);
        return new Arbeidsforhold(arbeidsforhold.getOrgnr(), arbeidsforhold.getBeløp(), ipt, false);
    }

    private LocalDate toLocalDate(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.toLocalDate();
        }
        return null;
    }

    private Kodeverdi kodeverdi(String kode) {
        return new Kodeverdi(kode, "ukjent");
    }
}
