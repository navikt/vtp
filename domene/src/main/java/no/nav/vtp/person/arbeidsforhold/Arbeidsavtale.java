package no.nav.vtp.person.arbeidsforhold;

import java.time.LocalDate;

public record Arbeidsavtale(Integer avtaltArbeidstimerPerUke,
                            Integer stillingsprosent,
                            Integer beregnetAntallTimerPerUke,
                            LocalDate sisteLønnsendringsdato,
                            LocalDate fom,
                            LocalDate tom) {
}
