package no.nav.system.os.eksponering;

import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningRequest;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningResponse;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.Oppdrag;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.Oppdragslinje;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SimuleringGeneratorTest {

    SimuleringGenerator simuleringGenerator = new SimuleringGenerator();

    @Test
    public void SimuleringTest(){
        SimulerBeregningResponse response = simuleringGenerator.opprettSimuleringsResultat(simTestDataFpNegativ());
        assertThat(response.getResponse().getSimulering().getGjelderId()).isNotEmpty();
    }

    private SimulerBeregningRequest simTestDataFpNegativ(){
        SimulerBeregningRequest request = new SimulerBeregningRequest();
        request.setRequest(new no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.SimulerBeregningRequest());
        request.getRequest().setOppdrag(new Oppdrag());
        request.getRequest().getOppdrag().setKodeEndring("NY");
        request.getRequest().getOppdrag().setKodeFagomraade("FP");
        request.getRequest().getOppdrag().setFagsystemId("123456789");
        request.getRequest().getOppdrag().setOppdragGjelderId("12345678901");
        request.getRequest().getOppdrag().setSaksbehId("saksbeh");

        Oppdragslinje oppdragslinje = new Oppdragslinje();
        oppdragslinje.setVedtakId("2020-11-27");
        oppdragslinje.setDelytelseId("1122334455667700");
        oppdragslinje.setKodeKlassifik("FPADATORD");
        oppdragslinje.setDatoVedtakFom("2020-07-27");
        oppdragslinje.setDatoVedtakTom("2020-11-08");
        oppdragslinje.setDatoStatusFom("2020-10-19");
        oppdragslinje.setSats(BigDecimal.valueOf(2339));
        oppdragslinje.setTypeSats("DAG");
        oppdragslinje.setSaksbehId("saksbeh");
        oppdragslinje.setUtbetalesTilId("12345678901");
        oppdragslinje.setHenvisning("123456");
        request.getRequest().getOppdrag().getOppdragslinje().add(oppdragslinje);

        return request;
    }
}
