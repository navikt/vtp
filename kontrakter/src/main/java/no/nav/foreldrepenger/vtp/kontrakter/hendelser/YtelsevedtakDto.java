package no.nav.foreldrepenger.vtp.kontrakter.hendelser;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representerer et vedtak om en ekstern ytelse som en person har mottatt.
 * Brukes i autotest for å simulere at en søker har fått innvilget ytelse fra et annet NAV-system.
 *
 * @param fnr             fødselsnummer til søker som har fått vedtaket
 * @param ytelseType      type ytelse
 * @param fom             første dag i vedtaksperioden
 * @param tom             siste dag i vedtaksperioden
 * @param utbetalingsgrad utbetalingsgrad i prosent (0–100)
 */
public record YtelsevedtakDto(
        String fnr,
        YtelseType ytelseType,
        LocalDate fom,
        LocalDate tom,
        BigDecimal utbetalingsgrad
) {}
