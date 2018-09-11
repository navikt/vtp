package no.nav.foreldrepenger.autotest.internal.konfigurasjon;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.TestBase;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.TestKonfigurasjon;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.TestKonfigurasjon.Bruker;

@Tag("internal")
public class TestKonfigurasjonTest extends TestBase{

    private static String BRUKER_ROLLE = "saksbehandler";
    
    @Test
    public void hentBrukerRolleTest() {
        Bruker bruker = TestKonfigurasjon.hentBruker(BRUKER_ROLLE);
        verifiser(bruker != null, "Kunne ikke hente bruker");
        verifiser(bruker.passord != null, "Kunne ikke hente bruker passord");
        verifiser(bruker.brukernavn != null, "Kunne ikke hente bruker passord");
    }
}
