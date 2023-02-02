package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Grunnlag(Status status,
                       Tema tema,
                       Prosent dekningsgrad,
                       @JsonAlias("foedselsdatoBarn") LocalDate fødselsdatoBarn,
                       @JsonAlias("arbeidskategori") Arbeidskategori kategori,
                       List<Arbeidsforhold> arbeidsforhold,
                       Periode periode,
                       Behandlingstema behandlingstema,
                       LocalDate identdato,
                       LocalDate iverksatt,
                       @JsonAlias("opphoerFom") LocalDate opphørFom,
                       Integer gradering,
                       LocalDate opprinneligIdentdato,
                       LocalDate registrert,
                       String saksbehandlerId,
                       List<Vedtak> vedtak) {
}
