package no.nav.tjeneste.virksomhet.infotrygd.rest.saker;

import java.time.LocalDate;

public record Utbetaling(int gradering,
                         LocalDate utbetaltFom,
                         LocalDate utbetaltTom) {
}
