package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ArenaMeldekort(LocalDate fom,
                             LocalDate tom,
                             BigDecimal dagsats,
                             BigDecimal beløp,
                             BigDecimal utbetalingsgrad) {
}
