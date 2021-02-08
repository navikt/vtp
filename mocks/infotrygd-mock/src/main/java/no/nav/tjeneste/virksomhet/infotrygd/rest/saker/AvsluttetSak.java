package no.nav.tjeneste.virksomhet.infotrygd.rest.saker;

import java.time.LocalDate;
import java.util.List;

public record AvsluttetSak(LocalDate iverksatt,
                           LocalDate stoppdato,
                           List<Utbetaling> utbetalinger) {
}
