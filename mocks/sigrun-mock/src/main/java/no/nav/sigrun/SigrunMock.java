package no.nav.sigrun;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.Inntektsår;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.Oppføring;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Api(tags = {"Sigrun/beregnetskatt"})
@Path("/api/beregnetskatt")
public class SigrunMock {
    private static final Logger LOG = LoggerFactory.getLogger(SigrunMock.class);

    public static final String X_NATURLIGIDENT_HEADER = "x-naturligident";
    public static final String X_INNTEKTSAAR_HEADER = "x-inntektsaar";
    public static final String X_AKTOERID_HEADER = "x-aktoerid";

    TestscenarioRepositoryImpl testscenarioRepository;

    public SigrunMock() {
        try {
            testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "beregnetskatt", notes = ("Returnerer beregnetskatt fra Sigrun"))
    public Response buildPermitResponse(@Context UriInfo ui, @Context HttpHeaders httpHeaders) {

        String brukerFnr = null;
        String inntektsAar = null;
        String aktørId = null;

        if (!httpHeaders.getRequestHeader(X_NATURLIGIDENT_HEADER).isEmpty()) {
            brukerFnr = httpHeaders.getRequestHeader(X_NATURLIGIDENT_HEADER).get(0);
        }
        if (!httpHeaders.getRequestHeader(X_INNTEKTSAAR_HEADER).isEmpty()) {
            inntektsAar = httpHeaders.getRequestHeader(X_INNTEKTSAAR_HEADER).get(0);
        }
        if (!httpHeaders.getRequestHeader(X_AKTOERID_HEADER).isEmpty()) {
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
}
