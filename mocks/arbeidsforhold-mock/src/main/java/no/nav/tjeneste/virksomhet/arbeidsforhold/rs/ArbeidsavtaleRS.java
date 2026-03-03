package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.time.LocalDate;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale;

public record ArbeidsavtaleRS(Double stillingsprosent,
                              Double antallTimerPrUke,
                              Double beregnetAntallTimerPrUke,
                              LocalDate sistLoennsendring,
                              PeriodeRS gyldighetsperiode,
                              String yrke) {

    public static ArbeidsavtaleRS fra(Arbeidsavtale avtale) {
        return new ArbeidsavtaleRS(
                avtale.stillingsprosent() != null ? avtale.stillingsprosent().doubleValue() : null,
                avtale.avtaltArbeidstimerPerUke() != null ? avtale.avtaltArbeidstimerPerUke().doubleValue() : null,
                avtale.beregnetAntallTimerPerUke() != null ? avtale.beregnetAntallTimerPerUke().doubleValue() : null,
                avtale.sisteLønnnsendringsdato(),
                new PeriodeRS(avtale.fomGyldighetsperiode(), avtale.tomGyldighetsperiode()),
                avtale.yrke() != null && avtale.yrke().yrke() != null ? avtale.yrke().yrke() : "8269102"
        );
    }
}
