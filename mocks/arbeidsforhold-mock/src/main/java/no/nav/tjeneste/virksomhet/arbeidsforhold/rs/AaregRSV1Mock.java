package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforholdstype;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("aareg-services/api/v1/arbeidstaker")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = {"AARegister Rest"})
public class AaregRSV1Mock {

    private static final Logger LOG = LoggerFactory.getLogger(AaregRSV1Mock.class);
    protected static final String HEADER_NAV_PERSONIDENT = "Nav-Personident";
    protected static final String QPRM_REGELVERK = "regelverk";
    protected static final String QPRM_FOM = "ansettelsesperiodeFom";
    protected static final String QPRM_TOM = "ansettelsesperiodeTom";
    protected static final String ARBEIDSFORHOLDTYPE = "arbeidsforholdtype";
    protected static final String REGELVERK = "regelverk";

    private final TestscenarioBuilderRepository scenarioRepository;

    public AaregRSV1Mock(@Context TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/arbeidsforhold")
    @ApiOperation(value = "Henter arbeidsforhold for en person")
    @ApiImplicitParams({
            @ApiImplicitParam(name = HEADER_NAV_PERSONIDENT, required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = QPRM_FOM, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = QPRM_TOM, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = ARBEIDSFORHOLDTYPE, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sporingsinformasjon", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = QPRM_REGELVERK, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "historikk", dataType = "string", paramType = "query")
    })
    public List<ArbeidsforholdRS> hentArbeidsforholdFor(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo) {
        var ident = httpHeaders.getHeaderString(HEADER_NAV_PERSONIDENT);
        var qryparams = uriInfo.getQueryParameters();
        final List<String> filtrerArbeidsforholdtyper = qryparams.getFirst(ARBEIDSFORHOLDTYPE) != null ?
                Arrays.asList(qryparams.getFirst(ARBEIDSFORHOLDTYPE).split(","))
                : new ArrayList<>();
        var fom = LocalDate.parse(qryparams.getFirst(QPRM_FOM));
        final LocalDate tom = qryparams.getFirst(QPRM_TOM) != null ?
                LocalDate.parse(qryparams.getFirst(QPRM_TOM)) : null;

        if (ident == null || fom == null)
            throw new IllegalArgumentException("Request uten ident eller fom");
        var inntektYtelseModell = scenarioRepository.getInntektYtelseModellFraAktørId(ident)
                .orElseGet(() -> scenarioRepository.getInntektYtelseModell(ident).orElse(null));
        if (inntektYtelseModell == null || inntektYtelseModell.arbeidsforholdModell() == null) {
            LOG.warn("AAREG REST finnArbeidsforholdPrArbeidstaker kunne ikke finne etterspurt bruker");
            return List.of();
        }

        LOG.info("AAREG REST {}", ident);
        return inntektYtelseModell.arbeidsforholdModell().arbeidsforhold().stream()
                .filter(a -> filterForArbeidsforholdType(filtrerArbeidsforholdtyper, a))
                .filter(a -> erOverlapp(fom, tom, a))
                .map(ArbeidsforholdRS::new)
                .collect(Collectors.toList());
    }

    private boolean erOverlapp(LocalDate requestFom, LocalDate requestTom, Arbeidsforhold arbeidsforhold) {
        var ansettelsesperiodeFom = arbeidsforhold.ansettelsesperiodeFom();
        var ansettelsesperiodeTom = arbeidsforhold.ansettelsesperiodeTom();

        return (ansettelsesperiodeTom == null || !requestFom.isAfter(ansettelsesperiodeTom)) &&
                (requestTom == null || !requestTom.isBefore(ansettelsesperiodeFom));
    }

    private boolean filterForArbeidsforholdType(List<String> arbeidsforholdtyper, Arbeidsforhold arbeidsforhold) {
        if (arbeidsforholdtyper.isEmpty()) {
            return !arbeidsforhold.arbeidsforholdstype().getKode().equalsIgnoreCase(Arbeidsforholdstype.FRILANSER_OPPDRAGSTAKER_MED_MER.getKode());
        } else {
            return arbeidsforholdtyper.contains(arbeidsforhold.arbeidsforholdstype().getKode());
        }
    }
}

