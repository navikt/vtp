package no.nav.foreldrepenger.autotest.engangsstonad;

import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;


public class Adopsjon extends EngangsstonadTestBase{

    
    public void behandleAdopsjonMorGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    public void behandleAdopsjonMorAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    public void behandleAdopsjonMorOverstyrt() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    public void behandleAdopsjonFarGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
    }
}
