package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Arbeidsavtale(Yrke yrke,
                            Avlønningstype avlønningstype,
                            Integer avtaltArbeidstimerPerUke,
                            Integer stillingsprosent,
                            Integer beregnetAntallTimerPerUke,
                            @JsonProperty("sisteLønnsendringsdato") LocalDate sisteLønnnsendringsdato,
                            LocalDate fomGyldighetsperiode,
                            LocalDate tomGyldighetsperiode) {
}
