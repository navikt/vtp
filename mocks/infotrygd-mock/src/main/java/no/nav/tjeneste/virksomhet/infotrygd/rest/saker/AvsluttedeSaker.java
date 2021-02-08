package no.nav.tjeneste.virksomhet.infotrygd.rest.saker;

import java.time.LocalDate;
import java.util.List;

public record AvsluttedeSaker(LocalDate fraOgMed,
                              List<AvsluttetSak> saker) {
    public AvsluttedeSaker() {
        this(null, List.of());
    }
}
