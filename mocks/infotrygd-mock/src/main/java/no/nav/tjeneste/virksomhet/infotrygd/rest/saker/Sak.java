package no.nav.tjeneste.virksomhet.infotrygd.rest.saker;

import java.time.LocalDate;

public record Sak(LocalDate iverksatt,
                  SakResultat resultat,
                  Saksnummer sakId,
                  String status,
                  SakType type,
                  LocalDate vedtatt) {
}
