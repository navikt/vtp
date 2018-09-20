package no.nav.foreldrepenger.autotest.sut.fpsak.eksempel;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import no.nav.foreldrepenger.autotest.sut.fpsak.FpsakTestBase;

@Tag("develop")
public class Loginn extends FpsakTestBase{
	
    @ParameterizedTest
    @ValueSource(strings= {"Saksbehandler"})
    void loginnTest(String rolle) throws Exception {
        saksbehandler.erLoggetInnMedRolle(rolle);
    }
}
