package no.nav.foreldrepenger.autotest.sut.fpsak.selftest;

import java.io.IOException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.sut.fpsak.FpsakTestBase;

@Tag("smoke")
public class Selftest extends FpsakTestBase{
    
    @Test
    public void VerifiserSelftest() throws IOException {
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentSelftest();
    }
}
