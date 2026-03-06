package no.nav.vtp.inntektskomponenten;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.Context;
import no.nav.vtp.PersonRepository;

import no.nav.vtp.inntektskomponenten.modell.InntektMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Tag(name = "/inntektskomponenten/v2/inntekt")
@Path("/inntektskomponenten/v2/inntekt")
public class InntektskomponentV2REST {
    private static final Logger LOG = LoggerFactory.getLogger(InntektskomponentV2REST.class);

    private final PersonRepository personRepository;

    public InntektskomponentV2REST(@Context PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer inntektliste fra Inntektskomponenten")
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
    @Path("/bulk")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer inntektliste fra Inntektskomponenten")
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
}
