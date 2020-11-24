package no.nav.omsorgspenger.rammemeldinger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.AleneOmOmsorgen;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OmsorgspengerRammemeldingerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Api(tags = {"Omsorgspenger/AleneOmOmsorgen"})
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
    @ApiOperation(value = "aleneOmOmsorgen", notes = ("Returnerer rammemeldinger / AleneOmOmsorgen"))
    public AleneOmOmsorgenResponse aleneOmOmsorgen(RammemeldingRequest request) {
        Optional<InntektYtelseModell> inntektYtelseModellOptional = scenarioRepository.getInntektYtelseModell(request.getIdentitetsnummer());
        if(inntektYtelseModellOptional.isEmpty()) {
            return new AleneOmOmsorgenResponse();
        }
        InntektYtelseModell inntektYtelseModell = inntektYtelseModellOptional.get();
        List<AleneOmOmsorgen> aleneOmOmsorgen = inntektYtelseModell.getOmsorgspengerModell().getRammemeldinger().getAleneOmOmsorgen();
        AleneOmOmsorgenResponse response = new AleneOmOmsorgenResponse();
        response.setAleneOmOmsorgen(aleneOmOmsorgen);
        return response;
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/hentOverfoeringer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "overføringer", notes = ("Returnerer overføringer om omsorgspenger"))
    public OverføringerResponse overføringer(RammemeldingRequest request) {
        Optional<InntektYtelseModell> inntektYtelseModellOptional = scenarioRepository.getInntektYtelseModell(request.getIdentitetsnummer());
        if(inntektYtelseModellOptional.isEmpty()) {
            return new OverføringerResponse();
        }
        InntektYtelseModell inntektYtelseModell = inntektYtelseModellOptional.get();

        OmsorgspengerRammemeldingerModell rammemeldinger = inntektYtelseModell.getOmsorgspengerModell().getRammemeldinger();

        OverføringerResponse response = new OverføringerResponse();
        response.setGitt(rammemeldinger.getOverføringerGitt());
        response.setFått(rammemeldinger.getOverføringerFått());
        return response;
    }
}
