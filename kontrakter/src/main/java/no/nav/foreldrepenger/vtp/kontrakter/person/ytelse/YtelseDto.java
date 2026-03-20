package no.nav.foreldrepenger.vtp.kontrakter.person.ytelse;

import java.time.LocalDate;
import java.util.List;

public record YtelseDto(YtelseType ytelse,
                        LocalDate fra,
                        LocalDate til,
                        Integer dagsats,
                        Integer utbetalt,
                        List<Beregningsgrunnlag> beregningsgrunnlag) {
}
