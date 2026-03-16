package no.nav.medl2.rest.api.v1;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import no.nav.vtp.PersonRepository;

@Path("medl2/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedlemskapsunntakMock {

    @Context
    private PersonRepository personRepository;

    @POST
    @Path("/periode/soek")
    public List<Medlemskapsunntak> hentMedlemsperioder(@NotNull MedlemRequest request) {
        if (request.personident() == null) {
            return List.of();
        }
        var person = personRepository.hentPerson(request.personident());
        return MedlemskapsunntakMapper.tilMedlemskapsunntak(person);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MedlemRequest(@NotNull String personident) { }
}
