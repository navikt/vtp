
package no.nav.vtp.person.ytelse;

import java.time.LocalDate;
public record Ytelse(YtelseType ytelse,
                     LocalDate fom,
                     LocalDate tom,
                     Integer dagsats,
                     Integer utbetalingsgrad) {
}
