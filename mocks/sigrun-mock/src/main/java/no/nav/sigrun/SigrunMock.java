package no.nav.sigrun;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.Inntektsår;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.Oppføring;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.PgiFolketrygdenResponse;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.SigrunModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Tag(name = "Sigrun/beregnetskatt")
@Path("/api")
public class SigrunMock {
    private static final Logger LOG = LoggerFactory.getLogger(SigrunMock.class);

    private final TestscenarioRepositoryImpl testscenarioRepository;

    public SigrunMock() {
        testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }

    @POST
    @Path("/v1/pensjonsgivendeinntektforfolketrygden")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer pensjonsgivende inntekt for folketrygden fra Sigrun (POST)")
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

        Optional<InntektYtelseModell> inntektYtelseModell = testscenarioRepository.getInntektYtelseModell(brukerFnr);

        if (inntektYtelseModell.isPresent()) {
            var næringsBeløp = inntektYtelseModell.get().sigrunModell().inntektsår().stream()
                    .filter(inntektsår -> inntektsår.år().equalsIgnoreCase(inntektsAar))
                    .map(Inntektsår::oppføring)
                    .flatMap(Collection::stream)
                    .filter(o -> SigrunModell.SIGRUN_OPPFØRING_TEKNISK_NAVN.equals(o.tekniskNavn()))
                    .map(Oppføring::verdi)
                    .map(Long::parseLong)
                    .findFirst().orElse(null);
            if (næringsBeløp != null) {
                var inntektsÅrInt = Integer.parseInt(inntektsAar);
                var ferdiglignet = LocalDate.now().withYear(inntektsÅrInt + 1).withMonth(5).withDayOfMonth(17);

                var pgi = new PgiFolketrygdenResponse.Pgi(PgiFolketrygdenResponse.Skatteordning.FASTLAND, ferdiglignet, null,
                        null, næringsBeløp, null);
                var response = new PgiFolketrygdenResponse(brukerFnr, inntektsÅrInt, List.of(pgi));
                return Response.status(200).entity(response).build();
            } else {
                LOG.info("Kunne ikke finne inntektsår");
                return Response.status(404, "Kunne ikke finne inntektsår").build();
            }
        } else {
            LOG.info("Kunne ikke finne bruker.");
            return Response.status(404, "Kunne ikke finne bruker").build();
        }
    }
}
