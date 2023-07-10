package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;

public record SivilstandDto(Sivilstander sivilstand, LocalDate fom, LocalDate tom){

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
