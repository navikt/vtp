package no.nav.foreldrepenger.autotest.engangsstonad;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;

public class Omsorgsovertakelse extends EngangsstonadTestBase{

    public void behenadleOmsorgsovertakelseMorGodkjent() throws IOException {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    public void behenadleOmsorgsovertakelseMorAvvist() throws IOException {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    public void behenadleOmsorgsovertakelseMorOverstyrt() throws IOException {
        TestscenarioDto testscenario = opprettScenario("55");
    }
    
    public void behenadleOmsorgsovertakelseFarGodkjent() throws IOException {
        TestscenarioDto testscenario = opprettScenario("55");
    }
}
