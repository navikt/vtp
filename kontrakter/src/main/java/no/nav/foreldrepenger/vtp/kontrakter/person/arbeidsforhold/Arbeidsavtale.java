package no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold;

import java.time.LocalDate;

public record Arbeidsavtale(Integer avtaltArbeidstimerPerUke,
                            Integer stillingsprosent,
                            Integer beregnetAntallTimerPerUke,
                            LocalDate sisteLønnsendringsdato,
                            LocalDate fomGyldighetsperiode,
                            LocalDate tomGyldighetsperiode) {
}
