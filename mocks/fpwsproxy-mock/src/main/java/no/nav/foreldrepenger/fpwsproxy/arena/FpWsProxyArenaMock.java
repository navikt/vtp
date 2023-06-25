package no.nav.foreldrepenger.fpwsproxy.arena;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.request.ArenaRequestDto;
import no.nav.foreldrepenger.vtp.testmodell.Feilkode;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Tag(name = "Fpwsproxy")
@Path("/api/fpwsproxy/arena")
public class FpWsProxyArenaMock {
    private static final Logger LOG = LoggerFactory.getLogger(FpWsProxyArenaMock.class);

    private final TestscenarioBuilderRepository scenarioRepository;

    public FpWsProxyArenaMock() {
        scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter meldekort for dagpenger og AAP for oppgitt periode")
    public Response henterDagpengerOgAAP(@Valid ArenaRequestDto arenaRequestDto) {
        var ident = arenaRequestDto.ident();
        LOG.info("Henter meldekort for {} for perioden {} til {}", ident, arenaRequestDto.fom(), arenaRequestDto.tom());
        var iyIndeksOpt = scenarioRepository.getInntektYtelseModell(ident);

        if (iyIndeksOpt.isEmpty()) {
            return Response.noContent().build(); // Håndteres 204 no content?
        }

        var arenaModell = iyIndeksOpt.get().arenaModell();
        var feilkode = arenaModell.feilkode();
        if (feilkode != null) {
            return haandterExceptions(feilkode, ident);
        }

        var meldekort = ArenaSakerTilMeldekortMapper.tilMeldekortUtbetalingsgrunnlagSakDto(arenaRequestDto, arenaModell.saker());
        LOG.info("Hentet {} meldekort for {} i oppgitt periode", meldekort.size(), ident);

        return Response.ok(meldekort).build();
    }

    private Response haandterExceptions(Feilkode feilkode, String ident) {
        return switch (feilkode) {
            case UGYLDIG_INPUT -> Response.status(Response.Status.BAD_REQUEST).build();
            case PERSON_IKKE_FUNNET -> Response.status(Response.Status.NOT_FOUND).entity("Fant ikke person " + ident).build();
            case SIKKERHET_BEGRENSNING -> Response.status(Response.Status.FORBIDDEN).build();
        };
    }

}
