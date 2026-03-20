package no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold;

import java.time.LocalDate;

public record ArbeidsavtaleDto(Integer avtaltArbeidstimerPerUke,
                               Integer stillingsprosent,
                               Integer beregnetAntallTimerPerUke,
                               LocalDate sisteLønnsendringsdato,
                               LocalDate fomGyldighetsperiode,
                               LocalDate tomGyldighetsperiode) {
}
