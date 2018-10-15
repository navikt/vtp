package no.nav.foreldrepenger.autotest.eksempler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.FpsakTestBase;

@Tag("eksempel")
public class Loginn extends FpsakTestBase{
    
    @Test
    @Tag("utvikling")
    void loginnUtenRolle() throws Exception {
        saksbehandler.erLoggetInnUtenRolle();
    }
    
    void loginnMedRolle() throws Exception {
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
    }
}
