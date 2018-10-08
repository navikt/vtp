package no.nav.foreldrepenger.autotest.tests.engangsstonad;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

public class SettSakPoVentOgGjenoppta extends EngangsstonadTestBase{

    
    public void settBehandlingPåVentOgGjenoppta() throws Exception {
        //Opprett scenario og søknad
        TestscenarioImpl testscenario = opprettScenario("50");
        Soeknad søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøker().getAktørIdent());
        
        //Send inn søknad
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.settBehandlingPåVent(LocalDate.now(), saksbehandler.kodeverk.Venteårsak.getKode("AVV_DOK"));
    }
}
