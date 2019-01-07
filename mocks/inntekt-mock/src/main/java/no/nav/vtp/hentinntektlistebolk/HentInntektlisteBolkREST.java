package no.nav.vtp.hentinntektlistebolk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.tjeneste.virksomhet.inntekt.v3.modell.HentInntektlisteBolkMapperRest;
import no.nav.tjenester.aordningen.inntektsinformasjon.Aktoer;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsInntektIdent;
import no.nav.tjenester.aordningen.inntektsinformasjon.request.HentInntektListeBolkRequest;
import no.nav.tjenester.aordningen.inntektsinformasjon.response.HentInntektListeBolkResponse;


@Path("/inntektskomponenten-ws/rs/api/v1/hentinntektlistebolk")
public class HentInntektlisteBolkREST {
    private static final Logger LOG = LoggerFactory.getLogger(HentInntektlisteBolkREST.class);


    TestscenarioRepositoryImpl testscenarioRepository;

    public HentInntektlisteBolkREST() {
        try {
            testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public HentInntektListeBolkResponse hentInntektlisteBolk(HentInntektListeBolkRequest request){
        //TODO: (OL) implementer representasjon av Inntekt

        List<Aktoer> identListe = request.getIdentListe();

        request.getMaanedFom();
        request.getMaanedTom();

        HentInntektListeBolkResponse response = new HentInntektListeBolkResponse();
        response.setArbeidsInntektIdentListe(new ArrayList<>());

        for(Aktoer aktoer : identListe){
            InntektskomponentModell inntektskomponentModell = testscenarioRepository.getInntektYtelseModellFraAktørId(aktoer.getIdentifikator()).get().getInntektskomponentModell();
            ArbeidsInntektIdent arbeidsInntektIdent = HentInntektlisteBolkMapperRest.makeArbeidsInntektIdent(
                    inntektskomponentModell
                    , aktoer
                    , request.getMaanedFom()
                    , request.getMaanedTom());
            response.getArbeidsInntektIdentListe().add(arbeidsInntektIdent);
        }

        return response;

    }

}
