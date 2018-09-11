package no.nav.foreldrepenger.autotest.sut.fpsak;

import org.junit.jupiter.api.BeforeEach;

import no.nav.foreldrepenger.autotest.TestBase;
import no.nav.foreldrepenger.autotest.aktoerer.saksbehandler.Saksbehandler;

public class FpsakTestBase extends TestBase{

    protected Saksbehandler saksbehandler;
    
    @BeforeEach
    void setUp() throws Exception{
        saksbehandler = new Saksbehandler();
    }
    
}
