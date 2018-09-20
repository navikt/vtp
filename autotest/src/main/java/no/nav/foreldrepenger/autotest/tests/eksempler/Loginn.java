package no.nav.foreldrepenger.autotest.tests.eksempler;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;

@Tag("smoke")
public class Loginn extends FpsakTestBase{
	
    @ParameterizedTest
    @ValueSource(strings= {"Saksbehandler"})
    void loginnTest(String rolle) throws Exception {
        saksbehandler.erLoggetInnMedRolle(rolle);
    }
    
    @Test
    void loginnUtenRolle() {
        //TODO
    }
}
