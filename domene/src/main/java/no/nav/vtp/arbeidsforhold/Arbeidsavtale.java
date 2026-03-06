package no.nav.vtp.arbeidsforhold;

import java.time.LocalDate;

public record Arbeidsavtale(Integer avtaltArbeidstimerPerUke,
                            Integer stillingsprosent,
                            Integer beregnetAntallTimerPerUke,
                            LocalDate sisteLønnsendringsdato,
                            LocalDate fomGyldighetsperiode,
                            LocalDate tomGyldighetsperiode) {
}
