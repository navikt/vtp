
package no.nav.vtp.ytelse;

import java.time.LocalDate;
import java.util.List;

public record Ytelse(YtelseType ytelse,
                     LocalDate fom,
                     LocalDate tom,
                     Integer dagsats,
                     Integer utbetalt,
                     List<Beregningsgrunnlag> beregningsgrunnlag) {
}
