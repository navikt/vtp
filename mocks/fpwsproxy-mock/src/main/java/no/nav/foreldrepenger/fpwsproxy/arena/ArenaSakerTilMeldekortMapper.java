package no.nav.foreldrepenger.fpwsproxy.arena;

import static no.nav.foreldrepenger.fpwsproxy.UtilKlasse.safeStream;

import java.util.Collection;
import java.util.List;

import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.request.ArenaRequestDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.BeløpDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.FagsystemDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.MeldekortUtbetalingsgrunnlagMeldekortDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.MeldekortUtbetalingsgrunnlagSakDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.YtelseStatusDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.respons.YtelseTypeDto;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaMeldekort;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaSak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.ArenaVedtak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena.VedtakStatus;

class ArenaSakerTilMeldekortMapper {

    private static final List<String> GYLDIG_TEMA = List.of("AAP", "DAG");

    private ArenaSakerTilMeldekortMapper() {
    }

    static List<MeldekortUtbetalingsgrunnlagSakDto> tilMeldekortUtbetalingsgrunnlagSakDto(ArenaRequestDto request, List<ArenaSak> saker) {
        return safeStream(saker)
                .filter(arenasak -> GYLDIG_TEMA.contains(arenasak.tema().name()))
                .map(arenasak -> tilMeldekortUtbetalingsgrunnlagSakDto(request, arenasak))
                .flatMap(Collection::stream)
                .toList();
    }


    private static List<MeldekortUtbetalingsgrunnlagSakDto> tilMeldekortUtbetalingsgrunnlagSakDto(ArenaRequestDto request, ArenaSak arenaSak) {
        return safeStream(arenaSak.vedtak())
                .filter(vedtak -> filtrerVedtak(request, vedtak))
                .map(vedtak -> tilMeldekortUtbetalingsgrunnlagSakDto(arenaSak, vedtak))
                .toList();
    }

    private static MeldekortUtbetalingsgrunnlagSakDto tilMeldekortUtbetalingsgrunnlagSakDto(ArenaSak arenaSak, ArenaVedtak vedtak) {
        return new MeldekortUtbetalingsgrunnlagSakDto.Builder()
                .type(oversettType(arenaSak))
                .kilde(FagsystemDto.ARENA)
                .kravMottattDato(vedtak.kravMottattDato())
                .saksnummer(arenaSak.saksnummer())
                .sakStatus(arenaSak.status().name())
                .tilstand(tilTilstand(vedtak.status()))
                .vedtakStatus(vedtak.status().name())
                .vedtattDato(vedtak.vedtakDato())
                .vedtaksPeriodeFom(vedtak.fom())
                .vedtaksPeriodeTom(vedtak.tom())
                .vedtaksDagsats(new BeløpDto(vedtak.dagsats()))
                .meldekortene(tilMeldekortDto(vedtak.meldekort()))
                .build();
    }

    private static YtelseStatusDto tilTilstand(VedtakStatus status) {
        return switch (status) {
            case AVSLU, INAKT -> YtelseStatusDto.AVSLU;
            case IVERK -> YtelseStatusDto.LOP;
            default -> YtelseStatusDto.UBEH;
        };
    }


    private static List<MeldekortUtbetalingsgrunnlagMeldekortDto> tilMeldekortDto(List<ArenaMeldekort> arenaMeldekort) {
        return safeStream(arenaMeldekort)
                .map(ArenaSakerTilMeldekortMapper::tilMeldekortDto)
                .toList();
    }


    private static MeldekortUtbetalingsgrunnlagMeldekortDto tilMeldekortDto(ArenaMeldekort arenaMeldekort) {
        return new MeldekortUtbetalingsgrunnlagMeldekortDto.Builder()
                .dagsats(arenaMeldekort.dagsats())
                .beløp(arenaMeldekort.beløp())
                .utbetalingsgrad(arenaMeldekort.utbetalingsgrad())
                .meldekortFom(arenaMeldekort.fom())
                .meldekortTom(arenaMeldekort.tom())
                .build();
    }

    private static YtelseTypeDto oversettType(ArenaSak sak) {
        if (YtelseTypeDto.AAP.name().equals(sak.tema().name())) {
            return YtelseTypeDto.AAP;
        } else if (YtelseTypeDto.DAG.name().equals(sak.tema().name())) {
            return YtelseTypeDto.DAG;
        } else {
            return null;
        }
    }

    private static boolean filtrerVedtak(ArenaRequestDto request, ArenaVedtak vedtak) {
        var fom = request.fom();
        var tom = request.tom();
        if (fom == null && tom == null) {
            return true;
        } else {
            return (fom == null || fom.isBefore(vedtak.tom()) || fom.isEqual(vedtak.tom()))
                    && (tom == null || tom.isAfter(vedtak.fom()) || tom.isEqual(vedtak.fom()));
        }
    }
}
