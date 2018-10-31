package no.nav.foreldrepenger.autotest.foreldrepenger.eksempler;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;

import no.nav.foreldrepenger.autotest.foreldrepenger.FpsakTestBase;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("eksempel")
public class OppretteFagsak extends FpsakTestBase{

    
    public void oppretteTerminsøknad() throws Exception {
        //Opprett scenario og søknad
        TestscenarioDto testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        //Send inn søknad
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        //Behandle sak
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        saksbehandler.settBehandlingPåVent(LocalDate.now(), saksbehandler.kodeverk.Venteårsak.getKode("AVV_DOK"));
        verifiser(saksbehandler.valgtBehandling.erSattPåVent(), "Behandlingen er ikke satt på vent");
        
        saksbehandler.gjenopptaBehandling();
        verifiser(!saksbehandler.valgtBehandling.erSattPåVent(), "Behandlingen er satt på vent");
        
        saksbehandler.henleggBehandling(saksbehandler.henleggArsaker.getKode("HENLAGT_SØKNAD_TRUKKET"));
        
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
    }
}
