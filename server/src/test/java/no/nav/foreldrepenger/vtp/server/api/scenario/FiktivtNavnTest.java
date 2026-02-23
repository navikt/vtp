package no.nav.foreldrepenger.vtp.server.api.scenario;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


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
