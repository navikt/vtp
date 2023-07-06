package no.nav.foreldrepenger.vtp.server.api.scenario;

import static no.nav.foreldrepenger.vtp.server.api.scenario.TestscenarioRestTjeneste.konverterTilTestscenarioDto;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.InntektYtelseModellMapper.tilInntektytelseModell;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.OrganisasjonsmodellMapper.tilOrganisasjonsmodeller;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilAnnenpart;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilBarn;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilPersonArbeidsgiver;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilPersonOpplysninger;
import static no.nav.foreldrepenger.vtp.server.api.scenario.mapper.PersonopplysningModellMapper.tilSøker;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.kontrakter.v2.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.v2.Rolle;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenario;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonWrapper;


@Tag(name = "Testscenario")
@Path("/api/testscenarios/v2")
public class TestscenarioV2RestTjeneste {
    private static final Logger logger = LoggerFactory.getLogger(TestscenarioV2RestTjeneste.class);
    private static final JacksonWrapper mapper = new JacksonWrapper(JacksonObjectMapperTestscenario.getObjectMapper());


    @Context
    private TestscenarioRepository testscenarioRepository;

    public TestscenarioV2RestTjeneste() {
        //CDI
    }

    public TestscenarioV2RestTjeneste(TestscenarioRepository testscenarioRepository) {
        this.testscenarioRepository = testscenarioRepository;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response initialiserTestScenarioFraDto(List<PersonDto> personer) {
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

        logger.info("Initialisert testscenario med uuid {}", testscenario.getId());
        return Response.status(Response.Status.OK)
                .entity(mapper.writeValueAsString(konverterTilTestscenarioDto(testscenario)))
                .build();
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
