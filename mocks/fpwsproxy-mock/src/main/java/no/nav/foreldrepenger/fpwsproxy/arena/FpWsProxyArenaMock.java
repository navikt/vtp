package no.nav.foreldrepenger.fpwsproxy.arena;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.request.ArenaRequestDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.MeldekortUtbetalingsgrunnlagSakDto;
import no.nav.vtp.PersonRepository;
import no.nav.vtp.ytelse.Ytelse;
import no.nav.vtp.ytelse.YtelseType;

@Tag(name = "Fpwsproxy")
@Path("/api/fpwsproxy/arena")
public class FpWsProxyArenaMock {
    private static final Logger LOG = LoggerFactory.getLogger(FpWsProxyArenaMock.class);

    private final PersonRepository personRepository;

    public FpWsProxyArenaMock(@Context PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter meldekort for dagpenger og AAP for oppgitt periode")
    public Response henterDagpengerOgAAP(@Valid ArenaRequestDto arenaRequestDto) {
        LOG.info("Henter meldekort for {} for perioden {} til {}", arenaRequestDto.ident(), arenaRequestDto.fom(), arenaRequestDto.tom());
        var meldekort = hentMeldekort(arenaRequestDto);
        LOG.info("Hentet {} meldekort for {} i oppgitt periode", meldekort.size(), arenaRequestDto.ident());
        return Response.ok(meldekort).build();
    }

    public List<MeldekortUtbetalingsgrunnlagSakDto> hentMeldekort(ArenaRequestDto arenaRequestDto) {
        var person = personRepository.hentPerson(arenaRequestDto.ident());
        return person.ytelser().stream()
                .filter(ytelse -> Set.of(YtelseType.DAGPENGER, YtelseType.ARBEIDSAVKLARINGSPENGER).contains(ytelse.ytelse()))
                .filter(ytelse -> overlapperMedPeriode(arenaRequestDto, ytelse))
                .map(YtelseTilMeldekortMapper::tilMeldekort)
                .toList();
    }

    public boolean overlapperMedPeriode(ArenaRequestDto arenaRequestDto, Ytelse ytelse) {
        var requestFom = arenaRequestDto.fom();
        var requestTom = arenaRequestDto.tom();
        if (requestFom == null && requestTom == null) {
            return true;
        }

        var starterEtterRequest = requestTom != null && ytelse.fom().isAfter(requestTom);
        var slutterFørRequest = requestFom != null && ytelse.tom() != null && ytelse.tom().isBefore(requestFom);
        return !(starterEtterRequest || slutterFørRequest);
    }
}
