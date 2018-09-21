package no.nav.foreldrepenger.autotest.tests.eksempler;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;

@Tag("eksempel")
public class Loginn extends FpsakTestBase{
    
    @Test
    void loginnUtenRolle() throws UnsupportedEncodingException {
        saksbehandler.erLoggetInnUtenRolle();
    }
}
