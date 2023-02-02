package no.nav.foreldrepenger.vtp.testmodell.identer;

import no.nav.foreldrepenger.vtp.testmodell.enums.IdentType;
import no.nav.foreldrepenger.vtp.testmodell.enums.Kjønn;
import no.nav.foreldrepenger.vtp.testmodell.util.TestdataUtil;

/**
 * Hent et tilfeldig gyldig men fiktivt Fødselsnummer.
 *
 * @see https://confluence.adeo.no/pages/viewpageattachments.action?pageId=211653415&metadataLink=true (SKD fiktive identer)
 */
public class FiktiveFnr implements IdentGenerator {
    /**
     * Bruk denne når kjønn ikke har betydning for anvendt FNR. (Bør normalt brukes slik at en sikrer at applikasjonen ikke gjør antagelser om
     * koding av kjønn i FNR.
     */
    @Override
    public String tilfeldigFnr() {
        return new FoedselsnummerGenerator.Builder()
                .fodselsdato(TestdataUtil.generateRandomPlausibleBirtdayParent())
                .buildAndGenerate();
    }

    /** Returnerer FNR for mann > 18 år */
    @Override
    public String tilfeldigMannFnr() {
        return new FoedselsnummerGenerator.Builder()
                .kjonn(Kjønn.MANN)
                .fodselsdato(TestdataUtil.generateRandomPlausibleBirtdayParent())
                .buildAndGenerate();
    }

    /** Returnerer FNR for kvinne > 18 år */
    @Override
    public String tilfeldigKvinneFnr() {
        return new FoedselsnummerGenerator.Builder()
                .kjonn(Kjønn.KVINNE)
                .fodselsdato(TestdataUtil.generateRandomPlausibleBirtdayParent())
                .buildAndGenerate();
    }

    /** Returnerer FNR for barn (tilfeldig kjønn) < 3 år */
    @Override
    public String tilfeldigBarnUnderTreAarFnr() {
        return new FoedselsnummerGenerator.Builder()
                .fodselsdato(TestdataUtil.generateBirthdateNowMinusThreeYears())
                .buildAndGenerate();
    }

    /** Returnerer DNR for kvinne > 18 år */
    @Override
    public String tilfeldigKvinneDnr() {
        return new FoedselsnummerGenerator.Builder()
                .fodselsdato(TestdataUtil.generateRandomPlausibleBirtdayParent())
                .identType(IdentType.DNR)
                .buildAndGenerate();
    }


}
