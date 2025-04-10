package no.nav.tjeneste.virksomhet.kelvin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ArbeidsavklaringspengerResponse(List<AAPVedtak> vedtak) {

    public record AAPVedtak(Integer barnMedStonad, Integer beregningsgrunnlag, Integer dagsats,
                            Kildesystem kildesystem, AAPPeriode periode, String saksnummer,
                            String vedtakId, LocalDate vedtaksdato, List<AAPUtbetaling> utbetaling) { }



    public record AAPPeriode(LocalDate fraOgMedDato, LocalDate tilOgMedDato) {}

    public record AAPUtbetaling(AAPPeriode periode, Integer belop, Integer dagsats,
                                Integer barnetillegg, AAPReduksjon reduksjon, Integer utbetalingsgrad) {
    }

    public record AAPReduksjon(BigDecimal annenReduksjon, BigDecimal timerArbeidet) { }

    public enum Kildesystem {
        ARENA, KELVIN
    }

}

