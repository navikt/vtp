package no.nav.omsorgspenger.rammemeldinger;

import java.util.List;
import java.util.Optional;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.AleneOmOmsorgen;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OmsorgspengerRammemeldingerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Tag(name = "Omsorgspenger/AleneOmOmsorgen")
@Path("/omsorgspenger-rammemeldinger")
public class OmsorgspengerMock {
    private final TestscenarioBuilderRepository scenarioRepository;

    public OmsorgspengerMock(@Context TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/hentAleneOmOmsorgen")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer rammemeldinger / AleneOmOmsorgen")
    public AleneOmOmsorgenResponse aleneOmOmsorgen(RammemeldingRequest request) {
        Optional<InntektYtelseModell> inntektYtelseModellOptional = scenarioRepository.getInntektYtelseModell(request.getIdentitetsnummer());
        if(inntektYtelseModellOptional.isEmpty()) {
            return new AleneOmOmsorgenResponse();
        }
        InntektYtelseModell inntektYtelseModell = inntektYtelseModellOptional.get();
        List<AleneOmOmsorgen> aleneOmOmsorgen = inntektYtelseModell.omsorgspengerModell().rammemeldinger().aleneOmOmsorgen();
        AleneOmOmsorgenResponse response = new AleneOmOmsorgenResponse();
        response.setAleneOmOmsorgen(aleneOmOmsorgen);
        return response;
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/hentOverfoeringer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer overføringer om omsorgspenger")
    public OverføringerResponse overføringer(RammemeldingRequest request) {
        Optional<InntektYtelseModell> inntektYtelseModellOptional = scenarioRepository.getInntektYtelseModell(request.getIdentitetsnummer());
        if(inntektYtelseModellOptional.isEmpty()) {
            return new OverføringerResponse();
        }
        InntektYtelseModell inntektYtelseModell = inntektYtelseModellOptional.get();

        OmsorgspengerRammemeldingerModell rammemeldinger = inntektYtelseModell.omsorgspengerModell().rammemeldinger();

        OverføringerResponse response = new OverføringerResponse();
        response.setGitt(rammemeldinger.overføringerGitt());
        response.setFått(rammemeldinger.overføringerFått());
        return response;
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/hent-korona-overforinger")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer koronaoverføringer om omsorgspenger")
    public KoronaOverføringerResponse koronaOverføringer(RammemeldingRequest request) {
        Optional<InntektYtelseModell> inntektYtelseModellOptional = scenarioRepository.getInntektYtelseModell(request.getIdentitetsnummer());
        if(inntektYtelseModellOptional.isEmpty()) {
            return new KoronaOverføringerResponse();
        }
        InntektYtelseModell inntektYtelseModell = inntektYtelseModellOptional.get();

        OmsorgspengerRammemeldingerModell rammemeldinger = inntektYtelseModell.omsorgspengerModell().rammemeldinger();

        KoronaOverføringerResponse response = new KoronaOverføringerResponse();
        response.setGitt(rammemeldinger.koronaOverføringerGitt());
        response.setFått(rammemeldinger.koronaOverføringerFått());
        return response;
    }
}
