package no.nav.vtp.inntektskomponenten;

import java.time.YearMonth;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public record InntektBulkRequest(@NotNull String personident, @NotNull List<String> filter, @NotNull String formaal,
                                 @NotNull YearMonth maanedFom, @NotNull YearMonth maanedTom) {
}
