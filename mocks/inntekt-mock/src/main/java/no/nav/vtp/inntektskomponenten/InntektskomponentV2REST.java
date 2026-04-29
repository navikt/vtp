package no.nav.vtp.inntektskomponenten;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.inntektskomponenten.modell.InntektMapper;


@Path("/inntektskomponenten/v2")
public class InntektskomponentV2REST {
    private static final Logger LOG = LoggerFactory.getLogger(InntektskomponentV2REST.class);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/inntekt")
    public InntektResponse hentInntektlisteBolk(InntektRequest request) {
        LOG.info("Henter inntekter for: {}", request.personident());
        var inntektsinformasjon = Optional.ofNullable(PersonRepository.hentPerson(request.personident()))
                .map(person -> person.inntekt())
                .map(inntekt -> InntektMapper.makeInntektsinformasjon(inntekt, request.maanedFom(), request.maanedTom(),
                        request.filter(), request.personident()))
                .orElse(List.of());
        return new InntektResponse(inntektsinformasjon);
    }

    @POST
    @Path("/inntekt/bulk")
    @Produces(MediaType.APPLICATION_JSON)
    public InntektBulkResponse hentInntektlisteBolk(InntektBulkRequest request) {
        LOG.info("Henter inntekter for: {}", request.personident());
        return Optional.ofNullable(PersonRepository.hentPerson(request.personident()))
                .map(person -> person.inntekt())
                .map(inntekt -> new InntektBulkResponse(request.filter()
                        .stream()
                        .map(f -> new InntektBulkResponse.InntektBulk(f,
                                InntektMapper.makeInntektsinformasjon(inntekt, request.maanedFom(), request.maanedTom(), f,
                                        request.personident())))
                        .toList()))
                .orElse(new InntektBulkResponse(List.of()));
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
