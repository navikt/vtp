package no.nav.foreldrepenger.autotest.tests.eksempler;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTerminBekreftelse;
import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

@Tag("eksempel")
public class Totrinnskontroll extends FpsakTestBase{

    public void behandleTotrinnskontrollAvTerminsøknad() throws Exception {
        /*
        TestscenarioImpl testscenario = testscenarioRepository.opprettTestscenario(TestscenarioTemplateRepositoryImpl.getInstance().finn("50"));
        Soeknad søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøker().getIdent());
        
        fordel.erLoggetInnUtenRolle();
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario);
        
        saksbehandler.erLoggetInnUtenRolle();
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.aksjonspunktBekreftelse(AvklarFaktaTerminBekreftelse.class)
            .antallBarn(1)
            .utstedtdato(LocalDate.now().minusWeeks(4))
            .termindato(LocalDate.now().plusWeeks(3));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTerminBekreftelse.class);
        verifiserLikhet(saksbehandler.valgtBehandling.status.navn, "behandling ferdig");
        */
    }
}
