package no.nav.vtp.inntektskomponenten;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.vtp.inntektskomponenten.modell.InntektModellMapper;


@Tag(name = "/inntektskomponenten/v2/inntekt")
@Path("/inntektskomponenten/v2/inntekt")
public class InntektskomponentV2REST {
    private static final Logger LOG = LoggerFactory.getLogger(InntektskomponentV2REST.class);

    private final TestscenarioRepositoryImpl testscenarioRepository;

    public InntektskomponentV2REST() {
        testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer inntektliste fra Inntektskomponenten")
    public InntektResponse hentInntektlisteBolk(InntektRequest request) {

        LOG.info("Henter inntekter for: {}", request.personident());

        var imodell = testscenarioRepository.getInntektYtelseModellFraAktørId(request.personident())
                .map(InntektYtelseModell::inntektskomponentModell);
        if (imodell.isEmpty()) {
            return new InntektResponse(List.of());
        }

        var inntektsinformasjon = InntektModellMapper.makeInntektsinformasjon(
                imodell.get(), request.maanedFom(), request.maanedTom(), request.filter());

        return new InntektResponse(inntektsinformasjon);

    }

    @POST
    @Path("/bulk")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer inntektliste fra Inntektskomponenten")
    public InntektBulkResponse hentInntektlisteBolk(InntektBulkRequest request) {

        LOG.info("Henter inntekter for: {}", request.personident());

        List<InntektBulkResponse.InntektBulk> bulkinntekter = new ArrayList<>();
        var imodell = testscenarioRepository.getInntektYtelseModellFraAktørId(request.personident())
                .map(InntektYtelseModell::inntektskomponentModell);
        if (imodell.isEmpty()) {
            return new InntektBulkResponse(List.of());
        }

        for (var f : request.filter()) {
            var inntektsinformasjon = InntektModellMapper.makeInntektsinformasjon(
                    imodell.get(), request.maanedFom(), request.maanedTom(), f);
            bulkinntekter.add(new InntektBulkResponse.InntektBulk(f, inntektsinformasjon));
        }

        return new InntektBulkResponse(bulkinntekter);

    }

}
