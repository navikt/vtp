package no.nav.foreldrepenger.autotest.tests;

import org.junit.jupiter.api.BeforeEach;

import no.nav.foreldrepenger.autotest.aktoerer.fordel.Fordel;
import no.nav.foreldrepenger.autotest.aktoerer.saksbehandler.Saksbehandler;

public class FpsakTestBase extends TestBase{

    protected Fordel fordel;
    protected Saksbehandler saksbehandler;
    
    @BeforeEach
    void setUp() throws Exception{
        fordel = new Fordel();
        
        saksbehandler = new Saksbehandler();
    }
    
}
