package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.kontrakter.person.PersonDto;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.person.arbeidsforhold.Arbeidsgiver;
import no.nav.vtp.person.arbeidsforhold.Organisasjon;
import no.nav.vtp.person.arbeidsforhold.PrivatArbeidsgiver;
import no.nav.vtp.person.ident.PersonIdent;
import no.nav.vtp.person.inntekt.Inntektsperiode;


@Path("/api/testscenarios/v2")
public class TestscenarioRestTjeneste {
    private static final Logger logger = LoggerFactory.getLogger(TestscenarioRestTjeneste.class);

    @Context
    private PersonRepository personRepository;

    public TestscenarioRestTjeneste() {
        //CDI
    }

    public TestscenarioRestTjeneste(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @POST
    @Path("/opprett")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initialiserTestScenarioFraDtoReturnerIdenter(List<PersonDto> personer) {
        var personerMapped = personer.stream()
                .map(PersonMapper::tilPerson)
                .toList();
        personRepository.leggTilPersoner(personerMapped);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/alleidenter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hentInitialiserteCaser() {
        var alleIdenter = personRepository.allePersoner().stream()
                .flatMap(p -> alleIdenterRegistertPåPerson(p).stream())
                .collect(Collectors.toSet());
        return Response.status(Response.Status.OK)
                .entity(alleIdenter)
                .build();
    }

    private static Set<String> alleIdenterRegistertPåPerson(Person person) {
        var personidenter = identerFraPersonIdent((PersonIdent) person.personopplysninger().identifikator());
        var arbeidsgivere = person.arbeidsforhold().stream()
                .map(Arbeidsforhold::arbeidsgiver)
                .flatMap(TestscenarioRestTjeneste::identFraArbeidsgiver)
                .collect(Collectors.toSet());
        var arbeidsgivereFraInntektsperioder = person.inntekt().stream()
                .map(Inntektsperiode::arbeidsgiver)
                .flatMap(TestscenarioRestTjeneste::identFraArbeidsgiver)
                .collect(Collectors.toSet());
        var set = new HashSet<String>();
        set.addAll(personidenter);
        set.addAll(arbeidsgivere);
        set.addAll(arbeidsgivereFraInntektsperioder);
        return set;
    }

    private static Stream<String> identFraArbeidsgiver(Arbeidsgiver a) {
        return switch (a) {
            case PrivatArbeidsgiver pa -> identerFraPersonIdent(pa.ident()).stream();
            case Organisasjon oa -> Set.of(oa.identifikator()).stream();
            default -> throw new IllegalStateException("Unexpected value: " + a);
        };
    }

    private static Set<String> identerFraPersonIdent(PersonIdent personIdent) {
        return Set.of(personIdent.fnr(), personIdent.aktørId());
    }
}
