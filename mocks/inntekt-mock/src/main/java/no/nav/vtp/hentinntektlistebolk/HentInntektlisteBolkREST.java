package no.nav.vtp.hentinntektlistebolk;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.tjenester.aordningen.inntektsinformasjon.Aktoer;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsInntektIdent;
import no.nav.tjenester.aordningen.inntektsinformasjon.request.HentInntektListeBolkRequest;
import no.nav.tjenester.aordningen.inntektsinformasjon.response.HentInntektListeBolkResponse;
import no.nav.vtp.hentinntektlistebolk.modell.HentInntektlisteBolkMapperRest;


@Tag(name = "/inntektskomponenten-ws/rs/api/v1/hentinntektlistebolk")
@Path("/inntektskomponenten-ws/rs/api/v1/hentinntektlistebolk")
public class HentInntektlisteBolkREST {
    private static final Logger LOG = LoggerFactory.getLogger(HentInntektlisteBolkREST.class);

    private final TestscenarioRepositoryImpl testscenarioRepository;

    public HentInntektlisteBolkREST() {
        testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer inntektliste fra Inntektskomponenten")
    public HentInntektListeBolkResponse hentInntektlisteBolk(HentInntektListeBolkRequest request){

        List<Aktoer> identListe = request.getIdentListe();
        LOG.info("Henter inntekter for personer: {}", identListe.stream().map(Aktoer::getIdentifikator).collect(Collectors.joining(",")));

        YearMonth fom = request.getMaanedFom() != null ? request.getMaanedFom() : YearMonth.of(1990,1);
        YearMonth tom = request.getMaanedTom() != null ? request.getMaanedTom() : YearMonth.of(1990,1);
        request.getMaanedTom();

        HentInntektListeBolkResponse response = new HentInntektListeBolkResponse();
        response.setArbeidsInntektIdentListe(new ArrayList<>());

        for(Aktoer aktoer : identListe){
            Optional<InntektYtelseModell> inntektYtelseModell = testscenarioRepository.getInntektYtelseModellFraAktørId(aktoer.getIdentifikator());
            if(inntektYtelseModell.isPresent()) {
                InntektskomponentModell inntektskomponentModell = inntektYtelseModell.get().inntektskomponentModell();
                ArbeidsInntektIdent arbeidsInntektIdent = HentInntektlisteBolkMapperRest.makeArbeidsInntektIdent(
                        inntektskomponentModell
                        , aktoer
                        , fom
                        , tom);
                response.getArbeidsInntektIdentListe().add(arbeidsInntektIdent);
            }

        }

        return response;

    }

}
