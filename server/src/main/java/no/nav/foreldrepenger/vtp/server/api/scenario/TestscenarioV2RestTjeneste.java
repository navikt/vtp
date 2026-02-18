package no.nav.foreldrepenger.vtp.server.api.scenario;

import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.InntektYtelseModellMapper.tilInntektytelseModell;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.OrganisasjonsmodellMapper.tilOrganisasjonsmodeller;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilAnnenpart;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilBarn;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilPersonArbeidsgiver;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilPersonOpplysninger;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilSøker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Rolle;
import no.nav.foreldrepenger.vtp.kontrakter.v2.TilordnetIdentDto;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;


@Tag(name = "Testscenario")
@Path("/api/testscenarios/v2")
public class TestscenarioV2RestTjeneste {
    private static final Logger logger = LoggerFactory.getLogger(TestscenarioV2RestTjeneste.class);


    @Context
    private TestscenarioRepository testscenarioRepository;

    public TestscenarioV2RestTjeneste() {
        //CDI
    }

    public TestscenarioV2RestTjeneste(TestscenarioRepository testscenarioRepository) {
        this.testscenarioRepository = testscenarioRepository;
    }

    @POST
    @Path("/opprett")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initialiserTestScenarioFraDtoReturnerIdenter(List<PersonDto> personer) {
        var søker = søker(personer);
        var annenpart = annenpart(personer, søker);
        var barnene = barnene(personer);
        var privatArbeidsgiver = privatArbeidsgiver(personer);

        var mappedePersoner = new HashMap<UUID, BrukerModell>();
        mappedePersoner.put(søker.id(), tilSøker(søker));
        annenpart.ifPresent(a -> mappedePersoner.put(a.id(), tilAnnenpart(a)));
        barnene.forEach(b -> mappedePersoner.put(b.id(), tilBarn(b)));
        privatArbeidsgiver.ifPresent(p -> mappedePersoner.put(p.id(), tilPersonArbeidsgiver(p)));

        var personopplysninger = annenpart
                .map(a -> tilPersonOpplysninger(søker, barnene, mappedePersoner, a))
                .orElseGet(() -> tilPersonOpplysninger(søker, barnene, mappedePersoner));
        var inntektytelseSøker = tilInntektytelseModell(søker.inntektytelse(), mappedePersoner);
        var inntektytelseAnnenpart = annenpart
                .map(p -> tilInntektytelseModell(p.inntektytelse(), mappedePersoner))
                .orElse(null);
        var organisasjonsmodeller = annenpart
                .map(a -> tilOrganisasjonsmodeller(søker, a))
                .orElseGet(() -> tilOrganisasjonsmodeller(søker));

        var testscenario = privatArbeidsgiver
                .map(p -> testscenarioRepository.opprettTestscenario(personopplysninger, inntektytelseSøker, inntektytelseAnnenpart,
                        organisasjonsmodeller, (PersonArbeidsgiver) mappedePersoner.get(p.id())))
                .orElseGet(() -> testscenarioRepository.opprettTestscenario(personopplysninger, inntektytelseSøker, inntektytelseAnnenpart,
                        organisasjonsmodeller));

        var nyidenter = mappedePersoner.entrySet().stream()
                .map(e -> new TilordnetIdentDto(e.getKey(), e.getValue().getIdent(), e.getValue().getAktørIdent()))
                .collect(Collectors.toSet());

        logger.info("Initialisert testscenario med uuid {}", testscenario.getId());
        return Response.status(Response.Status.OK)
                .entity(nyidenter)
                .build();
    }


    @GET
    @Path("/alleidenter")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter alle testcaser som er initiert i minnet til VTP", responses = {
            @ApiResponse(responseCode = "OK", content = @Content(schema = @Schema(implementation  = String[].class))),
    })
    public Response hentInitialiserteCaser() {
        Set<String> testscenarioidenter = new HashSet<>();
        testscenarioRepository.getTestscenarios().forEach((key, ts) -> {
            hentUtIdenter(testscenarioidenter, ts.getPersonopplysninger().getSøker());
            hentUtIdenter(testscenarioidenter, ts.getPersonopplysninger().getAnnenPart());
            Optional.ofNullable(ts.getSøkerInntektYtelse())
                    .map(InntektYtelseModell::arbeidsforholdModell)
                    .map(ArbeidsforholdModell::arbeidsforhold).orElseGet(List::of)
                    .forEach(a -> {
                        hentUtIdenter(testscenarioidenter, a.personArbeidsgiver());
                        Optional.ofNullable(a.arbeidsgiverAktorId()).ifPresent(testscenarioidenter::add);
                        Optional.ofNullable(a.arbeidsgiverOrgnr()).ifPresent(testscenarioidenter::add);
                    });
            Optional.ofNullable(ts.getSøkerInntektYtelse())
                    .map(InntektYtelseModell::inntektskomponentModell)
                    .map(InntektskomponentModell::frilansarbeidsforholdperioder).orElseGet(List::of)
                    .forEach(a -> {
                        hentUtIdenter(testscenarioidenter, a.arbeidsgiver());
                        Optional.ofNullable(a.aktorId()).ifPresent(testscenarioidenter::add);
                        Optional.ofNullable(a.orgnr()).ifPresent(testscenarioidenter::add);
                    });
            Optional.ofNullable(ts.getSøkerInntektYtelse())
                    .map(InntektYtelseModell::inntektskomponentModell)
                    .map(InntektskomponentModell::inntektsperioder).orElseGet(List::of)
                    .forEach(a -> {
                        hentUtIdenter(testscenarioidenter, a.arbeidsgiver());
                        Optional.ofNullable(a.aktorId()).ifPresent(testscenarioidenter::add);
                        Optional.ofNullable(a.orgnr()).ifPresent(testscenarioidenter::add);
                    });
        });

        return Response.status(Response.Status.OK)
                .entity(testscenarioidenter)
                .build();
    }

    private void hentUtIdenter(Set<String> identer, BrukerModell person) {
        Optional.ofNullable(person).map(BrukerModell::getIdent).ifPresent(identer::add);
        Optional.ofNullable(person).map(BrukerModell::getAktørIdent).ifPresent(identer::add);
    }


    private static PersonDto søker(List<PersonDto> personer) {
        return personer.stream()
                .filter(p -> Rolle.MOR.equals(p.rolle()))
                .findFirst()
                .orElseGet(() -> personer.stream()
                        .filter(p -> Rolle.FAR.equals(p.rolle()))
                        .findFirst()
                        .orElseThrow());
    }

    private static Optional<PersonDto> annenpart(List<PersonDto> personer, PersonDto søker) {
        return personer.stream()
                .filter(p -> søker.rolle().equals(Rolle.MOR) && Set.of(Rolle.FAR, Rolle.MEDMOR).contains(p.rolle()))
                .findFirst()
                .or(() -> personer.stream().filter(p -> Rolle.MEDFAR.equals(p.rolle())).findFirst());
    }

    private static List<PersonDto> barnene(List<PersonDto> personer) {
        return personer.stream()
                .filter(p -> Rolle.BARN.equals(p.rolle()))
                .toList();
    }

    private static Optional<PersonDto> privatArbeidsgiver(List<PersonDto> personer) {
        return personer.stream()
                .filter(p -> Rolle.PRIVATE_ARBEIDSGIVER.equals(p.rolle()))
                .findFirst();
    }
}
