package no.nav.vtp.inntektskomponenten;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.vtp.inntektskomponenten.modell.InntektModellMapper;


@Path("/inntektskomponenten/v2")
public class InntektskomponentV2REST {
    private static final Logger LOG = LoggerFactory.getLogger(InntektskomponentV2REST.class);

    private final TestscenarioRepositoryImpl testscenarioRepository;

    public InntektskomponentV2REST() {
        testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/inntekt")
    public InntektResponse hentInntektlisteBolk(InntektRequest request) {

        LOG.info("Henter inntekter for: {}", request.personident());

        var imodell = testscenarioRepository.getInntektYtelseModellFraAktørId(request.personident())
                .map(InntektYtelseModell::inntektskomponentModell);
        if (imodell.isEmpty()) {
            return new InntektResponse(List.of());
        }

        var inntektsinformasjon = InntektModellMapper.makeInntektsinformasjon(imodell.get(), request.maanedFom(), request.maanedTom(),
                request.filter(), request.personident());

        return new InntektResponse(inntektsinformasjon);

    }

    @POST
    @Path("/inntekt/bulk")
    @Produces(MediaType.APPLICATION_JSON)
    public InntektBulkResponse hentInntektlisteBolk(InntektBulkRequest request) {

        LOG.info("Henter inntekter for: {}", request.personident());

        List<InntektBulkResponse.InntektBulk> bulkinntekter = new ArrayList<>();
        var imodell = testscenarioRepository.getInntektYtelseModellFraAktørId(request.personident())
                .map(InntektYtelseModell::inntektskomponentModell);
        if (imodell.isEmpty()) {
            return new InntektBulkResponse(List.of());
        }

        for (var f : request.filter()) {
            var inntektsinformasjon = InntektModellMapper.makeInntektsinformasjon(imodell.get(), request.maanedFom(),
                    request.maanedTom(), f, request.personident());
            bulkinntekter.add(new InntektBulkResponse.InntektBulk(f, inntektsinformasjon));
        }

        return new InntektBulkResponse(bulkinntekter);

    }


    @POST
    @Path("/abonnement/administrasjon/opprett")
    @Produces(MediaType.APPLICATION_JSON)
    public AbonnementAdministrasjonOpprettApiUt opprettAbonnement(Object ignoredRequest) {
        long randomId = java.util.concurrent.ThreadLocalRandom.current().nextLong();
        return new AbonnementAdministrasjonOpprettApiUt(randomId);
    }

    @POST
    @Path("/abonnement/administrasjon/opphoer")
    @Produces(MediaType.APPLICATION_JSON)
    public AbonnementAdministrasjonOpphoerApiUt opphørAbonnement(Object ignoredRequest) {
        return new AbonnementAdministrasjonOpphoerApiUt();
    }

    @POST
    @Path("/abonnement/hendelse/start")
    @Produces(MediaType.APPLICATION_JSON)
    public AbonnementHendelseStartApiUt startHendelse(Object ignoredRequest) {
        return new AbonnementHendelseStartApiUt(1L);
    }

    @POST
    @Path("/abonnement/hendelse")
    @Produces(MediaType.APPLICATION_JSON)
    public AbonnementHendelseApiUt hendelse(Object ignoredRequest) {
        return new AbonnementHendelseApiUt(Collections.emptyList());
    }

}
