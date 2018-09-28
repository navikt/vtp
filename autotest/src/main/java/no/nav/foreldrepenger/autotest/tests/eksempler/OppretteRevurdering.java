package no.nav.foreldrepenger.autotest.tests.eksempler;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTerminBekreftelse;
import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

@Tag("eksempel")
public class OppretteRevurdering extends FpsakTestBase{
    
    public void opretteRevurderingPåTerminsøknad() throws Exception {
        //Oprett scenario og søknad
        TestscenarioImpl testscenario = opprettScenario("50");
        Soeknad søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøker().getAktørIdent());
        
        //Send inn søknad
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        //Behandle sak
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
        AvklarFaktaTerminBekreftelse bekreftelse = saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaTerminBekreftelse.class);
        bekreftelse.setTermindato(LocalDate.now().plusWeeks(1));
        bekreftelse.setAntallBarn(1);
        saksbehandler.bekreftAksjonspunktBekreftelse(bekreftelse);
        
        verifiserLikhet(saksbehandler.valgtFagsak.hentStatus(), "Avsluttet");
        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        
        //Opprette Revurdering
        saksbehandler.opprettBehandlingRevurdering();
        saksbehandler.velgBehandling(saksbehandler.behandlinger.get(1));
        
        verifiserLikhet(saksbehandler.valgtFagsak.hentStatus(), "Under behandling");
    }
}
