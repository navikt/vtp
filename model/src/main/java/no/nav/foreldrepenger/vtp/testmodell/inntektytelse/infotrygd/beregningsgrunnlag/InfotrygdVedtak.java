package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.time.LocalDate;

public record InfotrygdVedtak(LocalDate fom,
                              LocalDate tom,
                              Integer utbetalingsgrad) {
}
