package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
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
            @ApiImplicitParam(name = "sporingsinformasjon", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = QPRM_REGELVERK, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "historikk", dataType = "string", paramType = "query")
    })
    public List<ArbeidsforholdRS> hentArbeidsforholdFor(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo) {
        var ident = httpHeaders.getHeaderString(HEADER_NAV_PERSONIDENT);
        var qryparams = uriInfo.getQueryParameters();
        var fom = LocalDate.parse(qryparams.getFirst(QPRM_FOM));
        final LocalDate tom;
        if (qryparams.getFirst(QPRM_TOM) != null) {
            tom = LocalDate.parse(qryparams.getFirst(QPRM_TOM));
        } else {
            tom = null;
        }


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
                .filter(a -> erOverlapp(fom, tom, a))
                .map(ArbeidsforholdRS::new)
                .collect(Collectors.toList());
    }

    private boolean erOverlapp(LocalDate requestFom, LocalDate requestTom, Arbeidsforhold arbeidsforhold) {
        var ansettelsesperiodeFom = arbeidsforhold.ansettelsesperiodeFom();
        var ansettelsesperiodeTom = arbeidsforhold.ansettelsesperiodeTom();

        if ((ansettelsesperiodeTom == null) || !requestFom.isAfter(ansettelsesperiodeTom))
            if ((requestTom == null) || !requestTom.isBefore(ansettelsesperiodeFom)) {
                return true;
            }
        return false;
    }
}

