package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record ArenaVedtak(LocalDate kravMottattDato,
                          LocalDate fom,
                          LocalDate tom,
                          LocalDate vedtakDato,
                          VedtakStatus status,
                          BigDecimal dagsats,
                          List<ArenaMeldekort> meldekort) {

    public ArenaVedtak(LocalDate kravMottattDato, LocalDate fom, LocalDate tom, LocalDate vedtakDato, VedtakStatus status, BigDecimal dagsats, List<ArenaMeldekort> meldekort) {
        this.kravMottattDato = kravMottattDato;
        this.fom = fom;
        this.tom = tom;
        this.vedtakDato = vedtakDato;
        this.status = status;
        this.dagsats = dagsats;
        this.meldekort = Optional.ofNullable(meldekort).orElse(List.of());
    }
}
