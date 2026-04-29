package no.nav.foreldrepenger.fpwsproxy.arena;

import java.math.BigDecimal;
import java.util.List;

import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.BeløpDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.FagsystemDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.MeldekortUtbetalingsgrunnlagMeldekortDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.MeldekortUtbetalingsgrunnlagSakDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.YtelseStatusDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.YtelseTypeDto;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

public class YtelseTilMeldekortMapper {

    private YtelseTilMeldekortMapper() {
        /* This utility class should not be instantiated */
    }


    public static MeldekortUtbetalingsgrunnlagSakDto tilMeldekort(Ytelse ytelse) {
        return new MeldekortUtbetalingsgrunnlagSakDto.Builder()
                .type(oversettType(ytelse.ytelse()))
                .kilde(FagsystemDto.ARENA)
                .saksnummer("999")
                .sakStatus("AKTIV")
                .vedtakStatus("IVERK")
                .tilstand(YtelseStatusDto.LOP)
                .kravMottattDato(ytelse.fom())
                .vedtattDato(ytelse.fom())
                .vedtaksPeriodeFom(ytelse.fom())
                .vedtaksPeriodeTom(ytelse.tom())
                .vedtaksDagsats(ytelse.dagsats() != null ? new BeløpDto(BigDecimal.valueOf(ytelse.dagsats())) : null)
                .meldekortene(List.of(new MeldekortUtbetalingsgrunnlagMeldekortDto.Builder()
                        .meldekortFom(ytelse.fom())
                        .meldekortTom(ytelse.tom())
                        .dagsats(ytelse.dagsats() != null ? BigDecimal.valueOf(ytelse.dagsats()) : null)
                        .beløp(ytelse.utbetalt() != null ? BigDecimal.valueOf(ytelse.utbetalt()) : null)
                        .utbetalingsgrad(BigDecimal.valueOf(100))
                        .build()))
                .build();
    }

    private static YtelseTypeDto oversettType(YtelseType ytelse) {
        return switch (ytelse) {
            case ARBEIDSAVKLARINGSPENGER -> YtelseTypeDto.AAP;
            case DAGPENGER -> YtelseTypeDto.DAG;
            default -> null;
        };
    }
}
