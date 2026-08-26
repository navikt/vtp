package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.time.LocalDate;

/**
 * V2-ytelse støtter kun nye mocker: Spøkelse (sykepenger), Kelvin (AAP) og
 * DP-datadeling (dagpenger). V1-kontrakten benyttes for de gamle mockene
 * (Infotrygd, Arena, Pesys).
 */
public record YtelseDto(YtelseType type,
                        LocalDate fom,
                        LocalDate tom,
                        Integer dagsats,
                        Integer utbetalingsgrad) {

    public enum YtelseType {
        FORELDREPENGER,
        SVANGERSKAPSPENGER,
        PLEIEPENGER,
        DAGPENGER,
        SYKEPENGER,
        ARBEIDSAVKLARINGSPENGER,
        OMSORGSPENGER,
        OPPLÆRINGSPENGER,
        UFØREPENSJON
    }
}
