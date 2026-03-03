package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import no.nav.vtp.PersonRepository;

@Path("aareg-services/api/v1/arbeidstaker")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "AARegister Rest")
public class AaregRSV1Mock {

    private static final Logger LOG = LoggerFactory.getLogger(AaregRSV1Mock.class);
    protected static final String HEADER_NAV_PERSONIDENT = "Nav-Personident";
    protected static final String QPRM_REGELVERK = "regelverk";
    protected static final String QPRM_FOM = "ansettelsesperiodeFom";
    protected static final String QPRM_TOM = "ansettelsesperiodeTom";
    protected static final String ARBEIDSFORHOLDTYPE = "arbeidsforholdtype";
    protected static final String REGELVERK = "regelverk";

    private TestscenarioBuilderRepository scenarioRepository;
    private PersonRepository personRepository;

    public AaregRSV1Mock(@Context TestscenarioBuilderRepository scenarioRepository, @Context PersonRepository personRepository) {
        this.scenarioRepository = scenarioRepository;
        this.personRepository = personRepository;
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/arbeidsforhold")
    @Operation(description = "Henter arbeidsforhold for en person", parameters = {
            @Parameter(name = HEADER_NAV_PERSONIDENT, required = true, in = ParameterIn.HEADER),
            @Parameter(name = QPRM_FOM, in = ParameterIn.QUERY),
            @Parameter(name = QPRM_TOM, in = ParameterIn.QUERY),
            @Parameter(name = ARBEIDSFORHOLDTYPE, in = ParameterIn.QUERY),
            @Parameter(name = "sporingsinformasjon", in = ParameterIn.QUERY),
            @Parameter(name = QPRM_REGELVERK, in = ParameterIn.QUERY),
            @Parameter(name = "historikk", in = ParameterIn.QUERY)
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
        var responsFraGammelModell = inntektYtelseModell.arbeidsforholdModell()
                .arbeidsforhold()
                .stream()
                .filter(a -> filterForArbeidsforholdType(filtrerArbeidsforholdtyper, a))
                .filter(a -> erOverlapp(fom, tom, a))
                .map(ArbeidsforholdRS::fra)
                .collect(Collectors.toList());

        var respponseFraNyModell = personRepository.hentPerson(ident)
                .arbeidsforhold()
                .stream()
                .filter(arbeidsforhold -> filterForArbeidsforholdType(filtrerArbeidsforholdtyper, arbeidsforhold))
                .filter(arbeidsforhold -> erOverlapp(fom, tom, arbeidsforhold))
                .map(ArbeidsforholdMapper::tilArbeidsforholdRS)
                .toList();


        if (!responsFraGammelModell.equals(respponseFraNyModell)) {
            LOG.info("Feil i mappen fra ny modell til gammel modell for ident {}. "
                    + "Gammel modell returnerer {} arbeidsforhold, mens ny modell returnerer {}", ident, responsFraGammelModell.size(), respponseFraNyModell.size());
        }

        return responsFraGammelModell;
    }

    private boolean erOverlapp(LocalDate fom, LocalDate tom, no.nav.vtp.arbeidsforhold.Arbeidsforhold arbeidsforhold) {
        var ansettelsesperiodeFom = arbeidsforhold.ansettelsesperiodeFom();
        var ansettelsesperiodeTom = arbeidsforhold.ansettelsesperiodeTom();

        return (ansettelsesperiodeTom == null || !fom.isAfter(ansettelsesperiodeTom)) &&
                (tom == null || !tom.isBefore(ansettelsesperiodeFom));
    }

    private boolean filterForArbeidsforholdType(List<String> filtrerArbeidsforholdtyper, no.nav.vtp.arbeidsforhold.Arbeidsforhold a) {
        if (filtrerArbeidsforholdtyper.isEmpty()) {
            return !a.arbeidsforholdstype().getKode().equalsIgnoreCase(Arbeidsforholdstype.FRILANSER_OPPDRAGSTAKER_MED_MER.getKode());
        } else {
            return filtrerArbeidsforholdtyper.contains(a.arbeidsforholdstype().getKode());
        }
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

