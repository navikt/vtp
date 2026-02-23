package no.nav.vtp.personopplysninger;

import java.time.LocalDate;

public record Sivilstand(Sivilstander sivilstand, LocalDate fom, LocalDate tom){

    public enum Sivilstander {
        ENKE,
        GIFT,
        GJPA,
        GLAD,
        REPA,
        SAMB,
        SEPA,
        SEPR,
        SKIL,
        SKPA,
        UGIF;
    }

}
