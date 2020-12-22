package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import java.time.LocalDate;

public record Person(String id,
                     String type,
                     LocalDate fødselsdato) {
}
