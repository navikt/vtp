package no.nav.foreldrepenger.autotest.aktoerer.fpoppdrag;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.SimuleringKlient;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.BehandlingIdDto;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.SimulerOppdragDto;
import no.nav.foreldrepenger.autotest.klienter.fpoppdrag.simulering.dto.SimuleringDto;

public class Saksbehandler extends Aktoer {

    SimuleringKlient simuleringKlient;

    public Saksbehandler() {
        simuleringKlient = new SimuleringKlient(session);
    }

    public SimuleringDto hentSimuleringResultat(BehandlingIdDto behandlingIdDto) throws IOException {
        return simuleringKlient.hentSimuleringResultat(behandlingIdDto);
    }


    public void startSimulering(SimulerOppdragDto simulerOppdragDto) throws IOException {
        simuleringKlient.startSimulering(simulerOppdragDto);
    }

    public void kansellerSimulering(BehandlingIdDto behandlingIdDto) throws IOException {
        simuleringKlient.kansellerSimulering(behandlingIdDto);
    }
}
