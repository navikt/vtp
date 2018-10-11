package no.nav.foreldrepenger.autotest.engangsstonad;

import java.io.IOException;

import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;

public class Termin extends EngangsstonadTestBase{

    public void behandleTerminMorGodkjent()  throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
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
