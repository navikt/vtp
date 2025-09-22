package no.nav.vtp.inntektskomponenten;

import java.time.YearMonth;

import jakarta.validation.constraints.NotNull;

public record InntektRequest(@NotNull String personident, @NotNull String filter, @NotNull String formaal,
                             @NotNull YearMonth maanedFom, @NotNull YearMonth maanedTom) {
}
