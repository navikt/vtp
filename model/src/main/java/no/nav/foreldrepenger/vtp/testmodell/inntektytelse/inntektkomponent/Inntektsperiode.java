package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;

public record Inntektsperiode(LocalDate fom,
                              LocalDate tom,
                              String aktorId,
                              Integer beløp,
                              String orgnr,
                              @JsonProperty("type") InntektType inntektType,
                              @JsonProperty("fordel") InntektFordel inntektFordel,
                              String beskrivelse,
                              String skatteOgAvgiftsregel,
                              Boolean inngaarIGrunnlagForTrekk,
                              Boolean utloeserArbeidsgiveravgift,
                              PersonArbeidsgiver arbeidsgiver) {
}
