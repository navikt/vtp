package no.nav.foreldrepenger.fpmock2.testmodell.identer;

import no.nav.foreldrepenger.fpmock2.testmodell.enums.Kjonn;
import no.nav.foreldrepenger.fpmock2.testmodell.util.TestdataUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
        return new FoedselsnummerGenerator
                .FodselsnummerGeneratorBuilder()
                .fodselsdato(TestdataUtil.generateRandomPlausibleBirtdayParent())
                .buildAndGenerate();

    }

    /** Returnerer FNR for mann > 18 år */
    @Override
    public String tilfeldigMannFnr() {
        //return neste("mann");
        return new FoedselsnummerGenerator
                .FodselsnummerGeneratorBuilder()
                .kjonn(Kjonn.MANN)
                .fodselsdato(TestdataUtil.generateRandomPlausibleBirtdayParent())
                .buildAndGenerate();
    }

    /** Returnerer FNR for kvinne > 18 år */
    @Override
    public String tilfeldigKvinneFnr() {
        return new FoedselsnummerGenerator
                .FodselsnummerGeneratorBuilder()
                .kjonn(Kjonn.KVINNE)
                .fodselsdato(TestdataUtil.generateRandomPlausibleBirtdayParent())
                .buildAndGenerate();
    }

    /** Returnerer FNR for barn (tilfeldig kjønn) < 3 år */
    @Override
    public String tilfeldigBarnUnderTreAarFnr() {
        return new FoedselsnummerGenerator
                .FodselsnummerGeneratorBuilder()
                .fodselsdato(TestdataUtil.generateBirthdateNowMinusThreeYears())
                .buildAndGenerate();

    }
}
