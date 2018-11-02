package no.nav.foreldrepenger.autotest.foreldrepenger.eksempler;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;

import no.nav.foreldrepenger.autotest.foreldrepenger.FpsakTestBase;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTerminBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("eksempel")
public class Totrinnskontroll extends FpsakTestBase{

    public void behandleTotrinnskontrollAvTerminsøknad() throws Exception {
        //Oprett scenario og søknad
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        //Send inn søknad
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        //Behandle sak
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        AvklarFaktaTerminBekreftelse bekreftelse = saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaTerminBekreftelse.class);
        bekreftelse.setTermindato(LocalDate.now().plusWeeks(1));
        bekreftelse.setAntallBarn(1);
        saksbehandler.bekreftAksjonspunktBekreftelse(bekreftelse);
        
        //Behandle totrinnskontroll
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        FatterVedtakBekreftelse fatterVedtak = beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class);
        fatterVedtak.godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_TERMINBEKREFTELSE));
    }
}
