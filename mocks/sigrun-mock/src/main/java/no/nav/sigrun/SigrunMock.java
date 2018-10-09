package no.nav.sigrun;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.sigrun.Inntektsår;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = {"Sigrun/beregnetskatt"})
@Path("/api/beregnetskatt")
public class SigrunMock {

    private static final Logger LOG = LoggerFactory.getLogger(SigrunMock.class);

    TestscenarioRepositoryImpl testscenarioRepository;

    public SigrunMock() {
        try {
            testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "beregnetskatt", notes = ("Returnerer beregnetskatt fra Sigrun"))
    public Response buildPermitResponse(@Context UriInfo ui, @Context HttpHeaders httpHeaders) {

        LOG.info("sigrun.");

        String brukerFnr = null;
        String inntektsAar = null;
        String aktørId = null;

        if (httpHeaders.getRequestHeader("x-naturligident").size() > 0) {
            brukerFnr = httpHeaders.getRequestHeader("x-naturligident").get(0);
        }
        if (httpHeaders.getRequestHeader("x-inntektsaar").size() > 0) {
            inntektsAar = httpHeaders.getRequestHeader("x-inntektsaar").get(0);
        }
        if (httpHeaders.getRequestHeader("x-aktoerid").size() > 0) {
            aktørId = httpHeaders.getRequestHeader("x-aktoerid").get(0);
        }

        if (brukerFnr == null && aktørId != null) {
            brukerFnr = testscenarioRepository.getPersonIndeks().finnByAktørIdent(aktørId).getIdent();
        } else if (brukerFnr == null && aktørId == null) {
            LOG.info("sigrun. fnr eller aktoerid må være oppgitt.");
            return Response.status(400, "Kan ikke ha tomt felt for både aktoerid og naturligident.").build();
        } else if (inntektsAar == null) {
            LOG.info("sigrun. Inntektsår må være oppgitt.");
            Response.status(404, "Det forespurte inntektsåret er ikke støttet.");
        }

        Optional<InntektYtelseModell> inntektYtelseModell = testscenarioRepository.getInntektYtelseModell(brukerFnr);
        String response = "";

        if (inntektYtelseModell.isPresent()) {
            List<Inntektsår> inntektsår = inntektYtelseModell.get().getSigrunModell().getInntektsår();
            String test = inntektsår.get(0).getOppføring().stream().map(t -> t.toString()).collect(Collectors.joining(",\n"));

            response = String.format("[\n%s\n]", test);
        } else {
            LOG.info("Kunne ikke finne bruker.");
            return Response.status(404,"Kunne ikke finne bruker").build();
        }
        return Response.status(200).entity(response).build();
    }

}
