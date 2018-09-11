package no.nav.foreldrepenger.autotest.internal.prototype;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("smoke")
public class SmokeTest extends FpsakTestBase{
	
	@ParameterizedTest
    @ValueSource(strings= {"Saksbehandler"})
	void loginnTest(String rolle) throws Exception {
        saksbehandler.erLoggetInnMedRolle(rolle);
    }
}
