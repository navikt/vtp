package no.nav.digdir;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.util.HashMap;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.vtp.PersonRepository;

@Path("/digdir")
public class DigdirKrrProxyMock {

    public static final String HEADER_NAV_PERSONIDENT = "Nav-Personident";

    private final PersonRepository personRepository;


    public DigdirKrrProxyMock(@Context PersonRepository personRepository) {this.personRepository = personRepository;
    }

    @Deprecated(forRemoval = true)
    @GET
    @Path("/rest/v1/person")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hentKontaktinformasjon(@HeaderParam(HEADER_NAV_PERSONIDENT) @NotNull String fnr,
                                           @HeaderParam(AUTHORIZATION) String authorizationHeader) {
        var spraak = hentUtForetrukketSpråkFraBruker(fnr);
        if (spraak == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setSpraak(spraak);
        return Response.ok(kontaktinformasjon).build();
    }

    @POST
    @Path("/rest/v1/personer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hentKontaktinformasjon(@Valid @NotNull Personidenter personidenter) {
        var kontatkinformasjonerMap = new HashMap<String, Kontaktinformasjoner.Kontaktinformasjon>();
        var feilMap = new HashMap<String, Kontaktinformasjoner.FeilKode>();
        for (var personident : personidenter.personidenter()) {
            var spraak = hentUtForetrukketSpråkFraBruker(personident);
            if (spraak != null) {
                kontatkinformasjonerMap.put(personident, new Kontaktinformasjoner.Kontaktinformasjon(spraak));
            } else {
                feilMap.put(personident, Kontaktinformasjoner.FeilKode.person_ikke_funnet);
            }
        }
        return Response.ok(new Kontaktinformasjoner(kontatkinformasjonerMap, feilMap)).build();
    }

    private String hentUtForetrukketSpråkFraBruker(String fnr) {
        var person = personRepository.hentPerson(fnr);
        if (person == null) {
            return null;
        }
        var personopplysninger = person.personopplysninger();
        if (personopplysninger == null) {
            return null;
        }
        if (personopplysninger.identifikator() != null && personopplysninger.identifikator().value().equals(fnr)) {
            var språk = personopplysninger.språk();
            return språk != null ? språk.name() : null;
        }
        return null;
    }

    public record Personidenter(List<@NotNull String> personidenter) {
    }
}
