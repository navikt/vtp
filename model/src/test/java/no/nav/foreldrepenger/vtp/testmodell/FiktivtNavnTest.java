package no.nav.foreldrepenger.vtp.testmodell;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonNavn;
import no.nav.foreldrepenger.vtp.testmodell.util.FiktivtNavn;
import org.junit.Assert;
import org.junit.Test;

public class FiktivtNavnTest {

    @Test
    public void fiktiv_navn_skal_fungere() {
        PersonNavn randomFemalName = FiktivtNavn.getRandomFemaleName();
        Assert.assertTrue(randomFemalName.getFornavn().length() > 1);
        Assert.assertTrue(randomFemalName.getEtternavn().length() > 1);

        PersonNavn randomMaleName = FiktivtNavn.getRandomMaleName();
        Assert.assertTrue(randomMaleName.getFornavn().length() > 1);
        Assert.assertTrue(randomMaleName.getEtternavn().length() > 1);
    }
}
