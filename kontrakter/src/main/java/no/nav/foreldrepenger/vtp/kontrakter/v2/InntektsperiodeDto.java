package no.nav.foreldrepenger.vtp.kontrakter.v2;


import java.time.LocalDate;

public record InntektsperiodeDto(LocalDate fom,
                                 LocalDate tom,
                                 Integer beløp,
                                 InntektTypeDto inntektType,
                                 InntektYtelseType inntektYtelseType,
                                 InntektFordelDto inntektFordel,
                                 Arbeidsgiver arbeidsgiver) {

    public InntektsperiodeDto(LocalDate fom,
                              LocalDate tom,
                              Integer beløp,
                              InntektTypeDto inntektType,
                              InntektFordelDto inntektFordel,
                              Arbeidsgiver arbeidsgiver) {
        this(fom, tom, beløp, inntektType, null, inntektFordel, arbeidsgiver);
    }

    public InntektsperiodeDto(LocalDate fom,
                              LocalDate tom,
                              Integer beløp,
                              InntektYtelseType inntektYtelseType,
                              InntektFordelDto inntektFordel,
                              Arbeidsgiver arbeidsgiver) {
        this(fom, tom, beløp, null, inntektYtelseType, inntektFordel, arbeidsgiver);
    }

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
