package no.nav.tjeneste.virksomhet.kelvin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ArbeidsavklaringspengerResponse(List<AAPVedtak> vedtak) {

    public record AAPVedtak(Integer barnMedStonad, Integer beregningsgrunnlag, Integer dagsats,
                            String kildesystem, AAPPeriode periode, String saksnummer,
                            String vedtakId, LocalDateTime vedtaksdato, List<AAPUtbetaling> utbetaling) { }



    public record AAPPeriode(LocalDate fraOgMedDato, LocalDate tilOgMedDato) {}

    public record AAPUtbetaling(AAPPeriode periode, Integer belop, Integer dagsats,
                                Integer barnetillegg, AAPReduksjon reduksjon, Integer utbetalingsgrad) {
    }

    public record AAPReduksjon(Integer annenReduksjon, Integer timerArbeidet) { }

}

