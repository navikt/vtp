package no.nav.foreldrepenger.vtp.testmodell.jackson;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.medlemskap.MedlemskapperiodeModell;

class MedlemskapperiodeModellTest extends TestscenarioSerializationTestBase {

    @Test
    void roundtriptestMedlemskapperiodeModell( ) {
        test(new MedlemskapperiodeModell(null, null, null, null, null, null, null, null, null));
    }
}
