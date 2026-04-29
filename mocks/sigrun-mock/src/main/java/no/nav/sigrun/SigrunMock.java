package no.nav.sigrun;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.vtp.person.PersonRepository;

@Path("/api")
public class SigrunMock {
    private static final Logger LOG = LoggerFactory.getLogger(SigrunMock.class);

    @POST
    @Path("/v1/pensjonsgivendeinntektforfolketrygden")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPgiFolketrygden(PensjonsgivendeInntektForFolketrygdenRequest request) {

        String brukerFnr = request != null ? request.personident() : null;
        String inntektsAar = request != null ? request.inntektsaar() : null;

        LOG.info("Sigrun POST kalles for år: {} ", inntektsAar);

        if (brukerFnr == null) {
            LOG.info("sigrun. fnr må være oppgitt.");
            return Response.status(400, "Kan ikke ha tomt felt for personident.").build();
        } else if (inntektsAar == null) {
            LOG.info("sigrun. Inntektsår må være oppgitt.");
            return Response.status(404, "Det er ikke oppgitt noe inntektsår.").build();
        }

        var inntektsÅrInt = Integer.parseInt(inntektsAar);
        var person = PersonRepository.hentPerson(brukerFnr);
        if (person == null) {
            LOG.info("Fant ikke person med fnr i PersonRepository");
            return Response.status(404, "Kunne ikke finne person").build();
        }
        var skatteopplysninger = person.skatteopplysninger();
        if (skatteopplysninger == null) {
            LOG.info("Person har ingen skatteopplysninger");
            return Response.status(404, "Kunne ikke finne inntektsår").build();
        }
        var næringsBeløp = skatteopplysninger.stream()
                .filter(s -> s.år().equals(inntektsÅrInt))
                .map(s -> s.beløp().longValue())
                .findFirst()
                .orElse(null);

        if (næringsBeløp == null) {
            LOG.info("Kunne ikke finne inntektsår {}", inntektsAar);
            return Response.status(404, "Kunne ikke finne inntektsår").build();
        }

        var ferdiglignet = LocalDate.now().withYear(inntektsÅrInt + 1).withMonth(5).withDayOfMonth(17);
        var pgi = new PgiFolketrygdenResponse.Pgi(
                PgiFolketrygdenResponse.Skatteordning.FASTLAND,
                ferdiglignet,
                null,
                null,
                næringsBeløp,
                null
        );
        var response = new PgiFolketrygdenResponse(brukerFnr, inntektsÅrInt, List.of(pgi));
        return Response.status(200).entity(response).build();
    }
}
