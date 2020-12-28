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
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("aareg-services/api/v1/arbeidstaker")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = {"AARegister Rest"})
public class AaregRSV1Mock {

    private static final Logger LOG = LoggerFactory.getLogger(AaregRSV1Mock.class);
    private static final String HEADER_NAV_PERSONIDENT = "Nav-Personident";
    private static final String QPRM_REGELVERK = "regelverk";
    private static final String QPRM_FOM = "ansettelsesperiodeFom";
    private static final String QPRM_TOM = "ansettelsesperiodeTom";

    @Context
    private TestscenarioBuilderRepository scenarioRepository;


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
        String ident = httpHeaders.getHeaderString(HEADER_NAV_PERSONIDENT);
        var qryparams = uriInfo.getQueryParameters();
        LocalDate fom = LocalDate.parse(qryparams.getFirst(QPRM_FOM));
        LocalDate tom = LocalDate.parse(qryparams.getFirst(QPRM_TOM));

        if (ident == null || fom == null)
            throw new IllegalArgumentException("Request uten ident eller fom");
        InntektYtelseModell inntektYtelseModell = scenarioRepository.getInntektYtelseModellFraAktørId(ident)
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

    private boolean erOverlapp(LocalDate periodeFom, LocalDate periodeTom, Arbeidsforhold arbeidsforhold) {
        LocalDate ansettelsesperiodeFom = arbeidsforhold.ansettelsesperiodeFom();
        LocalDate ansettelsesperiodeTom = arbeidsforhold.ansettelsesperiodeTom();
        if (!periodeFom.isBefore(ansettelsesperiodeFom) && (ansettelsesperiodeTom == null || !periodeFom.isAfter(ansettelsesperiodeTom))) {
            return true;
        }
        if (periodeTom != null && !periodeTom.isBefore(ansettelsesperiodeFom) && (ansettelsesperiodeTom == null || !periodeTom.isAfter(ansettelsesperiodeTom))) {
            return true;
        }
        if (!periodeFom.isAfter(ansettelsesperiodeFom) &&  (periodeTom == null || !periodeTom.isBefore(ansettelsesperiodeFom))) {
            return true;
        }
        return false;
    }
}

