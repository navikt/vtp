package no.nav.foreldrepenger.fpwsproxy.arena;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.kontrakter.arena.request.ArenaRequestDto;
import no.nav.foreldrepenger.vtp.testmodell.Feilkode;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Path("/api/arena")
public class ArenaMock {
    private static final Logger LOG = LoggerFactory.getLogger(ArenaMock.class);

    private final TestscenarioBuilderRepository scenarioRepository;

    public ArenaMock() {
        scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter kontaktinformasjon for person")
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
