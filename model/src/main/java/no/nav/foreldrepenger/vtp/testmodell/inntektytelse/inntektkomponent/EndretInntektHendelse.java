package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import java.time.YearMonth;

public record EndretInntektHendelse(long sekvensnummer, String norskident, YearMonth maaned) {
}

