package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import static no.nav.tjeneste.virksomhet.arbeidsforhold.rs.ArbeidsforholdMapper.tilArbeidsforholdtypeAareg;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforholdstype;
import no.nav.vtp.PersonRepository;

@Path("aareg-services/api/v1/arbeidstaker")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AaregRSV1Mock {

    private static final Logger LOG = LoggerFactory.getLogger(AaregRSV1Mock.class);
    protected static final String HEADER_NAV_PERSONIDENT = "Nav-Personident";
    protected static final String QPRM_REGELVERK = "regelverk";
    protected static final String QPRM_FOM = "ansettelsesperiodeFom";
    protected static final String QPRM_TOM = "ansettelsesperiodeTom";
    protected static final String ARBEIDSFORHOLDTYPE = "arbeidsforholdtype";
    protected static final String REGELVERK = "regelverk";

    private final PersonRepository personRepository;

    public AaregRSV1Mock(@Context PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/arbeidsforhold")
    public List<ArbeidsforholdRS> hentArbeidsforholdFor(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo) {
        var ident = httpHeaders.getHeaderString(HEADER_NAV_PERSONIDENT);
        var qryparams = uriInfo.getQueryParameters();
        final List<String> filtrerArbeidsforholdtyper = qryparams.getFirst(ARBEIDSFORHOLDTYPE) != null ?
                Arrays.asList(qryparams.getFirst(ARBEIDSFORHOLDTYPE).split(","))
                : new ArrayList<>();
        var fom = LocalDate.parse(qryparams.getFirst(QPRM_FOM));
        final LocalDate tom = qryparams.getFirst(QPRM_TOM) != null ?
                LocalDate.parse(qryparams.getFirst(QPRM_TOM)) : null;

        if (ident == null || fom == null) {
            throw new IllegalArgumentException("Request uten ident eller fom");
        }

        LOG.info("AAREG REST {}", ident);
        return Optional.ofNullable(personRepository.hentPerson(ident))
                .map(person -> person
                    .arbeidsforhold()
                    .stream()
                    .filter(arbeidsforhold -> filterForArbeidsforholdType(filtrerArbeidsforholdtyper, arbeidsforhold))
                    .filter(arbeidsforhold -> erOverlapp(fom, tom, arbeidsforhold))
                    .map(ArbeidsforholdMapper::tilArbeidsforholdRS)
                    .toList())
                .orElse(List.of());
    }

    private boolean erOverlapp(LocalDate fom, LocalDate tom, no.nav.vtp.arbeidsforhold.Arbeidsforhold arbeidsforhold) {
        var ansettelsesperiodeFom = arbeidsforhold.ansettelsesperiodeFom();
        var ansettelsesperiodeTom = arbeidsforhold.ansettelsesperiodeTom();

        return (ansettelsesperiodeTom == null || !fom.isAfter(ansettelsesperiodeTom)) &&
                (tom == null || !tom.isBefore(ansettelsesperiodeFom));
    }

    private boolean filterForArbeidsforholdType(List<String> filtrerArbeidsforholdtyper, no.nav.vtp.arbeidsforhold.Arbeidsforhold a) {
        if (filtrerArbeidsforholdtyper.isEmpty()) {
            return !Arbeidsforholdstype.FRILANSER_OPPDRAGSTAKER_MED_MER.getKode().equalsIgnoreCase(tilArbeidsforholdtypeAareg(a.arbeidsforholdstype()));
        } else {
            return filtrerArbeidsforholdtyper.contains(tilArbeidsforholdtypeAareg(a.arbeidsforholdstype()));
        }
    }
}

