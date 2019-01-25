package no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering;

import java.io.IOException;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.FPOppdragKlient;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.BehandlingIdDto;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.SimulerOppdragDto;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.SimuleringDto;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class SimuleringKlient extends FPOppdragKlient {

    static final String SIMULERING_URL = "/simulering";
    static final String HENT_SIMULERING_RESULTAT_URL = SIMULERING_URL + "/resultat-uten-inntrekk";
    static final String START_SIMULERING_URL = SIMULERING_URL + "/start";
    static final String KANSELLER_SIMULERING_URL = SIMULERING_URL + "/kanseller";


    public SimuleringKlient(HttpSession session) {
        super(session);
    }

    @Step("Henter simuleringsresultat")
    public SimuleringDto hentSimuleringResultat(BehandlingIdDto behandlingIdDto) throws IOException {
        String url = hentRestRotUrl() + HENT_SIMULERING_RESULTAT_URL;
        return postOgHentJson(url, behandlingIdDto, SimuleringDto.class, StatusRange.STATUS_SUCCESS);
    }

    @Step("Starter simulering.")
    public void startSimulering(SimulerOppdragDto simulerOppdragDto) throws IOException {
        String url = hentRestRotUrl() + START_SIMULERING_URL;
        postOgVerifiser(url, simulerOppdragDto, StatusRange.STATUS_SUCCESS);
    }

    @Step("Kansellerer simulering.")
    public void kansellerSimulering(BehandlingIdDto behandlingIdDto) throws IOException {
        String url = hentRestRotUrl() + KANSELLER_SIMULERING_URL;
        postOgVerifiser(url, behandlingIdDto, StatusRange.STATUS_SUCCESS);
    }
}
