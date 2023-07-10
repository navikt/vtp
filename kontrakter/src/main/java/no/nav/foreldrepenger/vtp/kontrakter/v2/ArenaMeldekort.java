package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;

public record ArenaMeldekort(LocalDate fom, LocalDate tom, Integer dagsats, Integer beløp, Integer utbetalingsgrad) {
}
