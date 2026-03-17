package no.nav.omsorgspenger.rammemeldinger;

import java.time.Duration;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

@Path("/omsorgspenger-rammemeldinger")
public class OmsorgspengerMock {
    private final PersonRepository personRepository;

    public OmsorgspengerMock(@Context PersonRepository personRepository) {
        this.personRepository = personRepository;

    }

    @SuppressWarnings("unused")
    @POST
    @Path("/hentAleneOmOmsorgen")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AleneOmOmsorgenResponse aleneOmOmsorgen(RammemeldingRequest request) {
        return new AleneOmOmsorgenResponse();
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/hentOverfoeringer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public OverføringerResponse overføringer(RammemeldingRequest request) {
        var person = personRepository.hentPerson(request.getIdentitetsnummer());
        var omsorgspengerYtelse = person.ytelser().stream()
                .filter(ytelse -> YtelseType.OMSORGSPENGER.equals(ytelse.ytelse()))
                .findFirst();
        return new OverføringerResponse(List.of(), omsorgspengerYtelse.map(OmsorgspengerMock::tilOverføringFått).orElse(List.of()));
    }

    private static List<OverføringerResponse.OverføringFåttRammemelding> tilOverføringFått(Ytelse omsorgspenger) {
        return List.of(new OverføringerResponse.OverføringFåttRammemelding(
                omsorgspenger.fom(),
                omsorgspenger.fom(),
                omsorgspenger.tom(),
                Duration.ofDays(20),
                new OverføringerResponse.OverføringFåttRammemelding.Person("02499541043", "Identitetsnummer", null),
                List.of(new OverføringerResponse.OverføringFåttRammemelding.Kilde("2", "OmsorgspengerRammemeldinger"))
        ));
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/hent-korona-overforinger")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public KoronaOverføringerResponse koronaOverføringer(RammemeldingRequest request) {
        return new KoronaOverføringerResponse();
    }
}
