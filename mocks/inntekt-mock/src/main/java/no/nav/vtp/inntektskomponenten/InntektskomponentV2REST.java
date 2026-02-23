package no.nav.vtp.inntektskomponenten;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.inntektskomponenten.modell.InntektMapper;


@Path("/inntektskomponenten/v2")
public class InntektskomponentV2REST {
    private static final Logger LOG = LoggerFactory.getLogger(InntektskomponentV2REST.class);

    private final PersonRepository personRepository;

    public InntektskomponentV2REST(@Context PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/inntekt")
    public InntektResponse hentInntektlisteBolk(InntektRequest request) {
        LOG.info("Henter inntekter for: {}", request.personident());
        var person = personRepository.hentPerson(request.personident()).inntekt();
        if (person == null) {
            return new InntektResponse(List.of());
        }

        var inntektsinformasjon = InntektMapper.makeInntektsinformasjon(person, request.maanedFom(), request.maanedTom(), request.filter(), request.personident());
        return new InntektResponse(inntektsinformasjon);
    }

    @POST
    @Path("/inntekt/bulk")
    @Produces(MediaType.APPLICATION_JSON)
    public InntektBulkResponse hentInntektlisteBolk(InntektBulkRequest request) {
        LOG.info("Henter inntekter for: {}", request.personident());
        var person = personRepository.hentPerson(request.personident()).inntekt();
        if (person == null) {
            return new InntektBulkResponse(List.of());
        }

        return new InntektBulkResponse(request.filter().stream()
                .map(f -> new InntektBulkResponse.InntektBulk(f, InntektMapper.makeInntektsinformasjon(person, request.maanedFom(), request.maanedTom(), f, request.personident())))
                .toList());
    }


    @POST
    @Path("/abonnement/administrasjon/opprett")
    @Produces(MediaType.APPLICATION_JSON)
    public AbonnementAdministrasjonOpprettApiUt opprettAbonnement(Object ignoredRequest) {
        long randomId = java.util.concurrent.ThreadLocalRandom.current().nextLong();
        return new AbonnementAdministrasjonOpprettApiUt(randomId);
    }

    @POST
    @Path("/abonnement/administrasjon/opphoer")
    @Produces(MediaType.APPLICATION_JSON)
    public AbonnementAdministrasjonOpphoerApiUt opphørAbonnement(Object ignoredRequest) {
        return new AbonnementAdministrasjonOpphoerApiUt();
    }

    @POST
    @Path("/abonnement/hendelse/start")
    @Produces(MediaType.APPLICATION_JSON)
    public AbonnementHendelseStartApiUt startHendelse(Object ignoredRequest) {
        return new AbonnementHendelseStartApiUt(1L);
    }

    @POST
    @Path("/abonnement/hendelse")
    @Produces(MediaType.APPLICATION_JSON)
    public AbonnementHendelseApiUt hendelse(Object ignoredRequest) {
        return new AbonnementHendelseApiUt(Collections.emptyList());
    }

}
