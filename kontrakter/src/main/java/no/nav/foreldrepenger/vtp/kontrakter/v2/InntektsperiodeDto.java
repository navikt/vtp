package no.nav.foreldrepenger.vtp.kontrakter.v2;


import java.time.LocalDate;

public record InntektsperiodeDto(LocalDate fom,
                                 LocalDate tom,
                                 Integer beløp,
                                 InntektTypeDto inntektType,
                                 InntektFordelDto inntektFordel,
                                 Arbeidsgiver arbeidsgiver) {
    public enum InntektTypeDto {
        LØNNSINNTEKT,
        NÆRINGSINNTEKT,
        PENSJON_ELLER_TRYGD,
        YTELSE_FRA_OFFENTLIGE
    }

    public enum InntektFordelDto {
        KONTANTYTELSE,
        UTGIFTSGODTGJØRELSE,
        NATURALYTELSE
    }

}
