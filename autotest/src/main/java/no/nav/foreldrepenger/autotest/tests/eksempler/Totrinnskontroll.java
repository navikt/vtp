package no.nav.foreldrepenger.autotest.tests.eksempler;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTerminBekreftelse;
import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;

@Tag("eksempel")
public class Totrinnskontroll extends FpsakTestBase{

    public void behandleTotrinnskontrollAvTerminsøknad() throws Exception {
        fordel.erLoggetInnUtenRolle();
        long saksnummer = fordel.sendInnSøknad(null);
        
        saksbehandler.erLoggetInnUtenRolle();
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.aksjonspunktBekreftelse(AvklarFaktaTerminBekreftelse.class)
            .antallBarn(1)
            .utstedtdato(LocalDate.now().minusWeeks(4))
            .termindato(LocalDate.now().plusWeeks(3));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTerminBekreftelse.class);
        verifiserLikhet(saksbehandler.valgtBehandling.status.navn, "behandling ferdig");
    }
}
