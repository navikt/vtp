package no.nav.tjeneste.virksomhet.dpsak;

import java.time.LocalDate;

public record DagpengerUtbetalingDto(LocalDate fraOgMed, LocalDate tilOgMed, DagpengerKilde kilde,
                                     Integer sats, Integer utbetaltBeløp, Integer gjenståendeDager) {

    public enum DagpengerKilde {
        DP_SAK,
        ARENA
    }
}
