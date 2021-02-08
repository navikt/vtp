package no.nav.tjeneste.virksomhet.infotrygd.rest.saker;

import java.time.LocalDate;

public record IkkeStartetSak(LocalDate iverksatt,
                             LocalDate registrert) {
}
