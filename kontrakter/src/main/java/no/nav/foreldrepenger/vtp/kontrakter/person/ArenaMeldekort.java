package no.nav.foreldrepenger.vtp.kontrakter.person;

import java.time.LocalDate;

public record ArenaMeldekort(LocalDate fom, LocalDate tom, Integer dagsats, Integer beløp, Integer utbetalingsgrad) {
}
