package no.nav.sigrun;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
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
    private static final String X_NATURLIGIDENT_HEADER = "x-naturligident";
    private static final String X_INNTEKTSAAR_HEADER = "x-inntektsaar";
    private static final String X_AKTOERID_HEADER = "x-aktoerid";

    public static final String HEADER_NAV_PERSONIDENT = "Nav-Personident";
    public static final String HEADER_INNTEKTSAAR = "inntektsaar";

    private final TestscenarioRepositoryImpl testscenarioRepository;

    public SigrunMock() {
        testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/beregnetskatt")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer beregnetskatt fra Sigrun")
    public Response buildPermitResponse(@Context UriInfo ui, @Context HttpHeaders httpHeaders) {

        String brukerFnr = null;
        String inntektsAar = null;
        String aktørId = null;

        if (httpHeaders.getRequestHeader(X_NATURLIGIDENT_HEADER) != null && !httpHeaders.getRequestHeader(X_NATURLIGIDENT_HEADER).isEmpty()) {
            brukerFnr = httpHeaders.getRequestHeader(X_NATURLIGIDENT_HEADER).get(0);
        }
        if (httpHeaders.getRequestHeader(X_INNTEKTSAAR_HEADER) != null && !httpHeaders.getRequestHeader(X_INNTEKTSAAR_HEADER).isEmpty()) {
            inntektsAar = httpHeaders.getRequestHeader(X_INNTEKTSAAR_HEADER).get(0);
        }
        if (httpHeaders.getRequestHeader(X_AKTOERID_HEADER) != null && !httpHeaders.getRequestHeader(X_AKTOERID_HEADER).isEmpty()) {
            aktørId = httpHeaders.getRequestHeader(X_AKTOERID_HEADER).get(0);
        }

        LOG.info("Sigrun for aktørId: {} ", aktørId);

        if (brukerFnr == null && aktørId != null) {
            brukerFnr = testscenarioRepository.getPersonIndeks().finnByAktørIdent(aktørId).getIdent();
        } else if (brukerFnr == null && aktørId == null) {
            LOG.info("sigrun. fnr eller aktoerid må være oppgitt.");
            return Response.status(400, "Kan ikke ha tomt felt for både aktoerid og naturligident.").build();
        } else if (inntektsAar == null) {
            LOG.info("sigrun. Inntektsår må være oppgitt.");
            return Response.status(404, "Det forespurte inntektsåret er ikke støttet.").build();
        }

        Optional<InntektYtelseModell> inntektYtelseModell = testscenarioRepository.getInntektYtelseModell(brukerFnr);
        String finalInntektsAar = inntektsAar;
        String response;

        if (inntektYtelseModell.isPresent()) {
            Optional<Inntektsår> aktuellInntektsår = inntektYtelseModell.get().sigrunModell().inntektsår().stream()
                    .filter(inntektsår -> inntektsår.år().equalsIgnoreCase(finalInntektsAar))
                    .findFirst();
            if (aktuellInntektsår.isPresent()) {
                String test = aktuellInntektsår.get().oppføring().stream()
                        .map(Oppføring::tilSigrunMockResponseFormat)
                        .collect(Collectors.joining(",\n"));
                response = String.format("[%n%s%n]", test);
            } else {
                LOG.info("Kunne ikke finne inntektsår");
                return Response.status(404, "Kunne ikke finne inntektsår").build();
            }
        } else {
            LOG.info("Kunne ikke finne bruker.");
            return Response.status(404, "Kunne ikke finne bruker").build();
        }
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/v1/summertskattegrunnlag")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer tom respons ved ved oppskal av summertskattegrunmlag fra Sigrun")
    public Response dummySummertSkattGrunnlag(@Context UriInfo ui, @Context HttpHeaders httpHeaders) {
        return Response.noContent().build();
    }

    @GET
    @Path("/v1/pensjonsgivendeinntektforfolketrygden")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer tom respons ved ved oppskal av summertskattegrunmlag fra Sigrun")
    public Response dummypgiFolketrygden(@Context UriInfo ui, @Context HttpHeaders httpHeaders) {

        String brukerFnr = httpHeaders.getRequestHeader(HEADER_NAV_PERSONIDENT) != null && !httpHeaders.getRequestHeader(HEADER_NAV_PERSONIDENT).isEmpty() ?
                httpHeaders.getRequestHeader(HEADER_NAV_PERSONIDENT).get(0) : null;
        String inntektsAar = httpHeaders.getRequestHeader(HEADER_INNTEKTSAAR) != null && !httpHeaders.getRequestHeader(HEADER_INNTEKTSAAR).isEmpty() ?
                httpHeaders.getRequestHeader(HEADER_INNTEKTSAAR).get(0) : null;


        LOG.info("Sigrun kalles for år: {} ", inntektsAar);

        if (brukerFnr == null) {
            LOG.info("sigrun. fnr må være oppgitt.");
            return Response.status(400, "Kan ikke ha tomt felt for Nav-Personident.").build();
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
