package no.nav.tjeneste.virksomhet.kelvin;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import no.nav.vtp.person.PersonRepository;

@Path("/kelvin")
public class KelvinMock {

    private static final Logger LOG = LoggerFactory.getLogger(KelvinMock.class);

    @POST
    @Path("/maksimum")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ArbeidsavklaringspengerResponse postAAP(PersonRequest personRequest) {
        LOG.info("Kelvin /maksimum kalles for {} i perioden {} til {}", personRequest.personidentifikator(),
                personRequest.fraOgMedDato(), personRequest.tilOgMedDato());
        var person = PersonRepository.hentPerson(personRequest.personidentifikator());
        var vedtak = PersonTilAapVedtakMapper.tilAapVedtak(person, personRequest.fraOgMedDato(), personRequest.tilOgMedDato());
        LOG.info("Kelvin /maksimum returnerer {} vedtak for {}", vedtak.size(), personRequest.personidentifikator());
        return new ArbeidsavklaringspengerResponse(vedtak);
    }


    public record PersonRequest(String personidentifikator, LocalDate fraOgMedDato, LocalDate tilOgMedDato) { }

}
