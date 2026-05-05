package no.nav.foreldrepenger.vtp.server.api.scenario;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class FiktivtNavnTest {

    @Test
    void fiktiv_navn_skal_fungere() {
        PersonNavn randomFemalName = FiktivtNavn.getRandomFemaleName();
        assertTrue(randomFemalName.fornavn().length() > 1);
        assertTrue(randomFemalName.etternavn().length() > 1);

        PersonNavn randomMaleName = FiktivtNavn.getRandomMaleName();
        assertTrue(randomMaleName.fornavn().length() > 1);
        assertTrue(randomMaleName.etternavn().length() > 1);
    }
}
