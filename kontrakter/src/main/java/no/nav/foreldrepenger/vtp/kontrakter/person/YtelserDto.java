package no.nav.foreldrepenger.vtp.kontrakter.person;

import java.time.LocalDate;

public record YtelserDto(
        YtelseType type,
        LocalDate fom,
        LocalDate tom,
        Integer dagsats,
        Integer utbetalingsgrad,
        Kilde kilde) {

    public enum YtelseType {
        ARBEIDSAVKLARINGSPENGER,
        DAGPENGER,
        SYKEPENGER,
        PLEIEPENGER,
        OMSORGSPENGER,
        OPPLÆRINGSPENGER,
        UFØRETRYGD,
        FORELDREPENGER,
        SVANGERSKAPSPENGER
    }

    public enum Kilde {
        ARENA,
        INFOTRYGD,
        PESYS
    }
}
