package no.nav.foreldrepenger.autotest.tests.eksempler;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;

@Tag("eksempel")
public class Loginn extends FpsakTestBase{
    
    void loginnUtenRolle() throws UnsupportedEncodingException {
        saksbehandler.erLoggetInnUtenRolle();
    }
}
