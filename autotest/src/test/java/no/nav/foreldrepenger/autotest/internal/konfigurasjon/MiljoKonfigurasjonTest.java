package no.nav.foreldrepenger.autotest.internal.konfigurasjon;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.TestBase;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;

@Tag("internal")
public class MiljoKonfigurasjonTest extends TestBase{

    @Test
    public void testHentMiljø() {
        verifiser(MiljoKonfigurasjon.hentMiljø() != null, "Kunne ikke hente miljø");
    }
    
    @Test
    public void testHentApiRoot() {
        verifiser(MiljoKonfigurasjon.getRouteApi() != null, "Kunne ikke hente Rest root url for miljøet");
    }
}
