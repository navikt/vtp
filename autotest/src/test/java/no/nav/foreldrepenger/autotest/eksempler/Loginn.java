package no.nav.foreldrepenger.autotest.eksempler;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.FpsakTestBase;

@Tag("eksempel")
public class Loginn extends FpsakTestBase{
    
    @Test
    void loginnUtenRolle() throws Exception {
        saksbehandler.erLoggetInnUtenRolle();
    }
    
    @Test
    void loginnMedRolle() throws Exception {
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
    }
}
