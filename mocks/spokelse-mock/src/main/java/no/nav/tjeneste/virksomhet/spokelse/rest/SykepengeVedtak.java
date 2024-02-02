package no.nav.tjeneste.virksomhet.spokelse.rest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SykepengeVedtak(String vedtaksreferanse, List<SykepengeUtbetaling> utbetalinger, LocalDateTime vedtattTidspunkt) {

    public List<SykepengeUtbetaling> utbetalingerNonNull() {
        return utbetalinger != null ? utbetalinger : List.of();
    }

    public record SykepengeUtbetaling(LocalDate fom, LocalDate tom, BigDecimal grad) {

        public BigDecimal gradScale2() {
            return grad() != null ? grad().setScale(2, RoundingMode.HALF_UP) : null;
        }
    }
}

