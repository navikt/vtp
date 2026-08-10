package no.nav.tjeneste.virksomhet.dpsak;

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

@Path("/dagpenger/datadeling/v1")
public class DagpengerDatadelingMock {

    private static final Logger LOG = LoggerFactory.getLogger(DagpengerDatadelingMock.class);

    @POST
    @Path("/perioder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DagpengerRettighetsperioderDto postDagpengerPerioder(PersonRequest personRequest) {
        LOG.info("DP-datadeling /perioder kalles for {} i perioden {} til {}", personRequest.personIdent(),
                personRequest.fraOgMedDato(), personRequest.tilOgMedDato());
        var person = PersonRepository.hentPerson(personRequest.personIdent());
        var perioder = PersonTilDagpengerVedtakMapper.tilPerioder(person, personRequest.fraOgMedDato(), personRequest.tilOgMedDato());
        LOG.info("DP-datadeling /perioder returnerer {} perioder for {}", perioder.size(), personRequest.personIdent());
        return new DagpengerRettighetsperioderDto(personRequest.personIdent(), perioder);
    }

    @POST
    @Path("/beregninger")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DagpengerUtbetalingDto[] postDagpengerBeregning(PersonRequest personRequest) {
        LOG.info("DP-datadeling /beregninger kalles for {} i perioden {} til {}", personRequest.personIdent(),
                personRequest.fraOgMedDato(), personRequest.tilOgMedDato());
        var person = PersonRepository.hentPerson(personRequest.personIdent());
        var utbetalinger = PersonTilDagpengerVedtakMapper.tilUtbetalinger(person, personRequest.fraOgMedDato(), personRequest.tilOgMedDato());
        LOG.info("DP-datadeling /beregninger returnerer {} utbetalinger for {}", utbetalinger.size(), personRequest.personIdent());
        return utbetalinger.toArray(DagpengerUtbetalingDto[]::new);
    }

    public record PersonRequest(String personIdent, LocalDate fraOgMedDato, LocalDate tilOgMedDato) { }

}
