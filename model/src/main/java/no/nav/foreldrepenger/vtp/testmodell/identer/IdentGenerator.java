package no.nav.foreldrepenger.vtp.testmodell.identer;

public interface IdentGenerator {

    /**
     * Bruk denne når kjønn ikke har betydning for anvendt FNR. (Bør normalt brukes slik at en sikrer at applikasjonen ikke gjør antagelser om
     * koding av kjønn i FNR.
     */
    String tilfeldigFnr();

    /** Returnerer FNR for mann > 18 år */
    String tilfeldigMannFnr();

    /** Returnerer FNR for kvinne > 18 år */
    String tilfeldigKvinneFnr();

    /** Returnerer FNR for barn (tilfeldig kjønn) < 18 år */
    String tilfeldigBarnUnderTreAarFnr();

    /** Returnerer DNR for kvinne > 18 år */
    String tilfeldigKvinneDnr();

}
