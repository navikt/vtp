
package no.nav.vtp.person.ytelse;

import java.time.LocalDate;
import java.util.List;

public record Ytelse(YtelseType ytelse,
                     LocalDate fom,
                     LocalDate tom,
                     Integer dagsats,
                     Integer utbetalt,
                     Integer utbetalingsgrad,
                     LegacyKilde kilde,
                     List<Beregningsgrunnlag> beregningsgrunnlag) {
}
