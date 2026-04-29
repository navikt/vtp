package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.time.LocalDate;

public record ArbeidsavtaleRS(Double stillingsprosent,
                              Double antallTimerPrUke,
                              Double beregnetAntallTimerPrUke,
                              LocalDate sistLoennsendring,
                              PeriodeRS gyldighetsperiode,
                              String yrke) {
}
