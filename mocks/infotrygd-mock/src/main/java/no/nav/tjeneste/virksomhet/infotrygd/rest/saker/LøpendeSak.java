package no.nav.tjeneste.virksomhet.infotrygd.rest.saker;

import java.time.LocalDate;
import java.util.List;

public record LøpendeSak(LocalDate iverksatt,
                         List<Utbetaling>utbetalinger) {
}
