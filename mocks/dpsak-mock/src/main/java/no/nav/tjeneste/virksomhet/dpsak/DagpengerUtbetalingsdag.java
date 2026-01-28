package no.nav.tjeneste.virksomhet.dpsak;

import java.time.LocalDate;

public record DagpengerUtbetalingsdag(LocalDate dato, Integer sats, Integer utbetaltBeløp, Integer gjenståendeDager) {

}
