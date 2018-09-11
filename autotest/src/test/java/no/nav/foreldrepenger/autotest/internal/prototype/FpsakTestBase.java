package no.nav.foreldrepenger.autotest.internal.prototype;

import org.junit.jupiter.api.BeforeEach;

import no.nav.foreldrepenger.autotest.TestBase;
import no.nav.foreldrepenger.autotest.aktoerer.saksbehandler.Saksbehandler;

public class FpsakTestBase extends TestBase{

    Saksbehandler saksbehandler;
    
    @BeforeEach
    void setUp() throws Exception{
        saksbehandler = new Saksbehandler();
    }
    
}
