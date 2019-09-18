package no.nav.foreldrepenger.vtp.testmodell;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonNavn;
import no.nav.foreldrepenger.vtp.testmodell.util.FiktivtNavn;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void hente_navn_på_forbokstaver() {
        List<String> inputList = new ArrayList<>();
        inputList.add("Word");
        inputList.add("NotAWordThatStartsWith W");
        List<String> outputList1 = FiktivtNavn.getAlitterationsFromList(inputList, "Vord");
        Assert.assertTrue(outputList1.size() == 1);
        Assert.assertTrue(outputList1.get(0) == "Word");

        List<String> outputList2 = FiktivtNavn.getAlitterationsFromList(inputList, "AnotherWord");
        Assert.assertTrue(outputList2.size() == 0);
    }
    /*
    @Test
    public void printNames(){
        for (int x = 0; x <= 5; x++)
            System.out.println(FiktivtNavn.getRandomMaleName().getFulltnavn());
        for (int x = 0; x <= 5; x++)
            System.out.println(FiktivtNavn.getRandomFemaleName().getFulltnavn());
    }
    */
}
