package no.nav.foreldrepenger.autotest.engangsstonad;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

public class Termin extends EngangsstonadTestBase{

    @Tag("utvikling")
    @Test
    public void behandleTerminMorGodkjent()  throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.terminMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
    }
    
    public void behandleTerminMorAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    public void behandleTerminMorOvertyrt()  throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    public void behandleTerminMorUtenTerminbekreftelse()  throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    public void behandleTerminFarGodkjent()  throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    
}
