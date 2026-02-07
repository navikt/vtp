package no.nav.foreldrepenger.vtp.testmodell;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonNavn;
import no.nav.foreldrepenger.vtp.testmodell.util.FiktivtNavn;

class FiktivtNavnTest {

    @Test
    void fiktiv_navn_skal_fungere() {
        PersonNavn randomFemalName = FiktivtNavn.getRandomFemaleName();
        assertTrue(randomFemalName.getFornavn().length() > 1);
        assertTrue(randomFemalName.getEtternavn().length() > 1);

        PersonNavn randomMaleName = FiktivtNavn.getRandomMaleName();
        assertTrue(randomMaleName.getFornavn().length() > 1);
        assertTrue(randomMaleName.getEtternavn().length() > 1);
    }
}
