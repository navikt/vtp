package no.nav.tjeneste.virksomhet.spokelse.rest;

import java.time.LocalDate;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import no.nav.vtp.person.PersonRepository;

@Path("/spokelse")
public class SpøkelseMock {

    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SykepengeVedtak[] postSykepenger(PersonRequest personRequest) {
        var person = PersonRepository.hentPerson(personRequest.fodselsnummer());
        var vedtak = PersonTilSykepengeVedtakMapper.tilSykepengeVedtak(person, personRequest.fom());
        return vedtak.toArray(SykepengeVedtak[]::new);
    }

    public record PersonRequest(String fodselsnummer, LocalDate fom) { }

}
