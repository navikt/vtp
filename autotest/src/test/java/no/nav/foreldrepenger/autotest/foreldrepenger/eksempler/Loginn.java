package no.nav.foreldrepenger.autotest.foreldrepenger.eksempler;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.foreldrepenger.FpsakTestBase;

@Tag("eksempel")
public class Loginn extends FpsakTestBase{
    
    @Test
    void loginnUtenRolle() throws Exception {
        saksbehandler.erLoggetInnUtenRolle();
    }
    
    @Test
    void loginnMedRolle() throws Exception {
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
    }
}
