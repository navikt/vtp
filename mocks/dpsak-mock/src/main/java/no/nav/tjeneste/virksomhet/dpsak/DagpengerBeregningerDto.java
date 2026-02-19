package no.nav.tjeneste.virksomhet.dpsak;

import java.time.LocalDate;

public record DagpengerBeregningerDto(LocalDate fraOgMed, LocalDate tilOgMed, DagpengerKilde kilde,
                                      Integer sats, Integer utbetaltBeløp, Integer gjenståendeDager) {

}
